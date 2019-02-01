package com.agromall.android.imagedownloader.data.remote.api;

import android.support.annotation.Nullable;

import com.agromall.android.imagedownloader.data.model.GetImagesResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String apiBaseUrl ="https://theagromall.com/api/v1/";

    private ApiService apiService;

    public ApiClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit =builder.client(httpClient.build()).build();

        apiService = retrofit.create(ApiService.class);
    }

    public void getImages(int limit, int page, int lastImageId, final ApiCallback<GetImagesResponse> callback) {

        Call<GetImagesResponse> requestCall = apiService.getImages(limit, page, lastImageId);
        requestCall.enqueue(new Callback<GetImagesResponse>() {
            @Override
            public void onResponse(@Nullable Call<GetImagesResponse> call,
                                   @Nullable Response<GetImagesResponse> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@Nullable Call<GetImagesResponse> call,
                                  @Nullable Throwable t) {
                callback.onFail(call, t);
            }
        });

    }

    public void getImages(int limit, int page, final ApiCallback<GetImagesResponse> callback) {

        Call<GetImagesResponse> requestCall = apiService.getImages(limit, page);
        requestCall.enqueue(new Callback<GetImagesResponse>() {
            @Override
            public void onResponse(@Nullable Call<GetImagesResponse> call,
                                   @Nullable Response<GetImagesResponse> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@Nullable Call<GetImagesResponse> call,
                                  @Nullable Throwable t) {
                callback.onFail(call, t);
            }
        });

    }

}
