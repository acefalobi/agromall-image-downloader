package com.agromall.android.imagedownloader.data.remote.api;

import retrofit2.Call;
import retrofit2.Response;

public interface ApiCallback<T> {

    void onFail(Call<T> call, Throwable t);

    void onResponse(Call<T> call, Response<T> response);

}
