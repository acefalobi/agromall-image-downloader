package com.agromall.android.imagedownloader.data.model;

public class GetImagesResponse {

    private boolean status;
    private String message;
    private ImageListResponse data;

    public boolean status() {
        return status;
    }

    public ImageListResponse getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

