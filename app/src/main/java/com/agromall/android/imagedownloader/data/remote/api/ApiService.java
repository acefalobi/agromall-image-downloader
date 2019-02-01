package com.agromall.android.imagedownloader.data.remote.api;

import com.agromall.android.imagedownloader.data.model.GetImagesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("get-sample-images")
    Call<GetImagesResponse> getImages(
            @Query("limit") int limit,
            @Query("page") int page,
            @Query("lastImageId") int lastImageId
    );

    @GET("get-sample-images")
    Call<GetImagesResponse> getImages(
                    @Query("limit") int limit,
                    @Query("page") int page
            );

}
