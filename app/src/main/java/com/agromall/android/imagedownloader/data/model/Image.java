package com.agromall.android.imagedownloader.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "images")
public class Image {

    @PrimaryKey @NonNull
    private String imageId;
    private String imageUrl;
    private String imageCheckSum;
    private int downloadStatus = DOWNLOAD_NA;

    public static final int DOWNLOAD_NA = -1;
    public static final int DOWNLOAD_ONGOING = 0;
    public static final int DOWNLOAD_COMPLETED = 1;

    public Image(String imageId, String imageUrl, String imageCheckSum) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.imageCheckSum = imageCheckSum;
    }


    public String getImageId() {
        return imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageCheckSum() {
        return imageCheckSum;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }
}
