package com.aaron.android.framework.library.http.retrofit;

import com.aaron.android.codelibrary.http.NetworkErrorResponse;
import com.aaron.android.codelibrary.http.Request;
import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.Result;
import com.aaron.android.codelibrary.utils.LogUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 16/3/25.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class RetrofitRequest<T extends Result> implements Request<T> {

    public static final String TAG = "RetrofitRequest";
    private Call<T> mCall;

    public RetrofitRequest(Call<T> call) {
        mCall = call;
    }

    @Override
    public void execute(final RequestCallback requestCallback) {
        if (mCall == null) {
            throw new NullPointerException("retrofit call must be not null");
        }
        mCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "----request success----");
                    if (requestCallback != null) {
                        requestCallback.onSuccess(response.body());
                    }
                } else {
                    okhttp3.Response rawResponse = response.raw();
                    RequestError requestError = new RequestError();
                    requestError.setNetworkTimeMs(rawResponse.receivedResponseAtMillis() - rawResponse.sentRequestAtMillis());
                    requestError.setErrorType(RequestError.ErrorType.SERVER_ERROR);
                    requestError.setMessage(rawResponse.message());
                    requestError.setUrl(call.request().url().url().toString());
                    requestError.setRequestParams(call.request().url().queryParameterNames());
                    NetworkErrorResponse networkErrorResponse = null;
//                    try {
//                        networkErrorResponse = new NetworkErrorResponse(rawResponse.code(), rawResponse.body().bytes(),
//                                rawResponse.headers(), false, rawResponse.receivedResponseAtMillis() - rawResponse.sentRequestAtMillis());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    requestError.setErrorNetworkErrorResponse(networkErrorResponse);
                    requestCallback.onFailure(requestError);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                RequestError requestError = new RequestError();
                requestError.setErrorType(RequestError.ErrorType.NO_CONNECTION);
                requestError.setMessage(t.getMessage());
                requestError.setNetworkTimeMs(0);
                requestError.setUrl(call.request().url().url().toString());
                requestError.setRequestParams(call.request().url().queryParameterNames());
                requestCallback.onFailure(requestError);
            }
        });
    }

    @Override
    public T execute() {
        if (mCall == null) {
            throw new NullPointerException("retrofit call must be not null");
        }
        Response<T> response = null;
        try {
            response = mCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            return response.body();
        }
        return null;
    }
}
