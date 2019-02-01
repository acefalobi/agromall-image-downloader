package com.agromall.android.imagedownloader.ui.main.images.downloaded;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.agromall.android.imagedownloader.R;
import com.agromall.android.imagedownloader.data.model.Image;
import com.agromall.android.imagedownloader.ui.main.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

public class DownloadedImagesRecyclerAdapter extends RecyclerView.Adapter<DownloadedImagesRecyclerAdapter.ItemHolder> {

    private Context context;

    private List<Image> images;

    DownloadedImagesRecyclerAdapter(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
    }

    void replaceData(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_downloaded, viewGroup, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        final Image image = images.get(i);
        Glide.with(context).asBitmap()
                .load(image.getImageUrl())
                .thumbnail(.2f)
                .apply(new RequestOptions().placeholder(android.R.color.darker_gray))
                .into(itemHolder.image);

        itemHolder.textCheckSum.setText(image.getImageCheckSum());
        switch (image.getDownloadStatus()) {
            case Image.DOWNLOAD_ONGOING: {
                itemHolder.textStatus.setText("DOWNLOADING");
                itemHolder.textStatus.setTextColor(Color.GREEN);
                break;
            }
            case Image.DOWNLOAD_COMPLETED: {
                itemHolder.textStatus.setText("COMPLETED");
                itemHolder.textStatus.setTextColor(Color.RED);
                break;
            }
        }

        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "AgroMall");
                File file = new File(directory, image.getImageId() + ".jpg");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "image/jpeg");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView textCheckSum, textStatus;
        AppCompatImageView image;

        ItemHolder(@NonNull View itemView) {
            super(itemView);

            this.textStatus = itemView.findViewById(R.id.text_status);
            this.textCheckSum = itemView.findViewById(R.id.text_checksum);
            this.image = itemView.findViewById(R.id.image);
        }
    }

}
