package com.agromall.android.imagedownloader.ui.main.images.all;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.agromall.android.imagedownloader.R;
import com.agromall.android.imagedownloader.data.model.GetImagesResponse;
import com.agromall.android.imagedownloader.data.remote.api.ApiCallback;
import com.agromall.android.imagedownloader.data.remote.api.ApiClient;

import retrofit2.Call;
import retrofit2.Response;

public class AllImagesFragment extends Fragment {

    private View rootView;

    private ApiClient apiClient;

    private AllImagesRecyclerAdapter recyclerAdapter;

    private AppCompatButton btnGo;
    private AppCompatEditText editLimit, editPage;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    public AllImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_images, container, false);

        apiClient = new ApiClient();

        setupUI();

        loadImages();

        return rootView;
    }

    private void setupUI() {
        btnGo = rootView.findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImages();
            }
        });

        editLimit = rootView.findViewById(R.id.edit_limit);
        editPage = rootView.findViewById(R.id.edit_page);
        progressBar = rootView.findViewById(R.id.progress_bar);
        recyclerView = rootView.findViewById(R.id.recycler_all_images);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void loadImages() {
        int limit = 0, page = 0;
        try {
            limit = Integer.parseInt(editLimit.getText().toString());
            page = Integer.parseInt(editPage.getText().toString());
        } catch (NumberFormatException e) {
            Snackbar.make(btnGo, "Only numeric input is allowed", Snackbar.LENGTH_LONG).show();
        }

        if (limit > 0 && page > 0) {
            progressBar.setVisibility(View.VISIBLE);
            apiClient.getImages(limit, page, new ApiCallback<GetImagesResponse>() {
                @Override
                public void onFail(Call<GetImagesResponse> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(recyclerView, "Sorry, could not fetch data", Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Call<GetImagesResponse> call, Response<GetImagesResponse> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    GetImagesResponse responseBody = response.body();
                    if (responseBody != null) {
                        if (responseBody.status()) {
                            if (recyclerAdapter != null) {
                                recyclerAdapter.replaceData(responseBody.getData().getImagesList());
                            } else {
                                recyclerAdapter = new AllImagesRecyclerAdapter(getContext(),
                                        responseBody.getData().getImagesList());
                                recyclerView.setAdapter(recyclerAdapter);
                            }
                        } else {
                            Snackbar.make(recyclerView, responseBody.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

}
