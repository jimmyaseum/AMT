package com.app.amtcust.retrofit;

public interface ResponseListner<T> {

    void onResponse(ApiResponse<T> it);
}
