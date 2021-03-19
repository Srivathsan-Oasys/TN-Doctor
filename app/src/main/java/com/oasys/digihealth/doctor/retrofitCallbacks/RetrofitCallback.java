package com.oasys.digihealth.doctor.retrofitCallbacks;

import retrofit2.Response;

public interface RetrofitCallback<T> {
     void onSuccessfulResponse(Response<T> responseBody);

     void onBadRequest(Response<T> errorBody);

     void onServerError(Response response);

     void onUnAuthorized();

     void onForbidden();

     void onFailure(String s);

     void onEverytime();
}
