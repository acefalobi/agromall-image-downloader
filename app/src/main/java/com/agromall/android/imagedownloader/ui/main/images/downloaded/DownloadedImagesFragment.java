package com.agromall.android.imagedownloader.ui.main.images.downloaded;


import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.agromall.android.imagedownloader.R;
import com.agromall.android.imagedownloader.data.local.AppDatabase;
import com.agromall.android.imagedownloader.data.local.dao.ImagesDao;
import com.agromall.android.imagedownloader.data.model.Image;
import com.agromall.android.imagedownloader.util.AppExecutors;

import java.util.Collections;
import java.util.List;

public class DownloadedImagesFragment extends Fragment {

    private ImagesDao imagesDao;

    private View rootView;

    private DownloadedImagesRecyclerAdapter recyclerAdapter;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    public DownloadedImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_download_images, container, false);

        imagesDao = AppDatabase.getInstance(getContext()).imagesDao();

        setupUI();

        setupObserver();

        return rootView;
    }

    private void setupObserver() {

        final Observer<List<Image>> observer = new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> images) {
                updateImages(images);
            }
        };

        new AppExecutors().diskIO.execute(new Runnable() {
            @Override
            public void run() {
                imagesDao.getImages().observe(DownloadedImagesFragment.this, observer);
            }
        });
    }

    private void setupUI() {
        progressBar = rootView.findViewById(R.id.progress_bar);
        recyclerView = rootView.findViewById(R.id.recycler_all_images);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void updateImages(List<Image> images) {
        Collections.reverse(images);
        if (recyclerAdapter != null) {
            recyclerAdapter.replaceData(images);
        } else {
            recyclerAdapter = new DownloadedImagesRecyclerAdapter(getContext(), images);
            recyclerView.setAdapter(recyclerAdapter);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}
