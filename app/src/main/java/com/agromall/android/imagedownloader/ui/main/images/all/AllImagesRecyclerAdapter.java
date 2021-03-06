package com.agromall.android.imagedownloader.ui.main.images.all;

import android.content.Context;
import android.content.DialogInterface;
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

import java.util.List;

public class AllImagesRecyclerAdapter extends RecyclerView.Adapter<AllImagesRecyclerAdapter.ItemHolder> {

    private Context context;

    private List<Image> images;

    AllImagesRecyclerAdapter(Context context, List<Image> images) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, viewGroup, false);
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

        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure you want to download this image?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context, "Starting download...", Toast.LENGTH_SHORT).show();
                        ((MainActivity) context).downloadImage(image);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView textCheckSum;
        AppCompatImageView image;

        ItemHolder(@NonNull View itemView) {
            super(itemView);

            this.textCheckSum = itemView.findViewById(R.id.text_checksum);
            this.image = itemView.findViewById(R.id.image);
        }
    }

}
