package com.aaron.android.codelibrary.http;


/**
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public class RequestError {
    private ErrorType mErrorType;
    private NetworkErrorResponse mErrorNetworkErrorResponse;
    private String mMessage;
    private long networkTimeMs;
    private String mUrl;
    private Object mRequestParams;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public ErrorType getErrorType() {
        return mErrorType;
    }

    public void setErrorType(ErrorType errorType) {
        mErrorType = errorType;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Object getRequestParams() {
        return mRequestParams;
    }

    public void setRequestParams(Object requestParams) {
        mRequestParams = requestParams;
    }

    public NetworkErrorResponse getErrorNetworkErrorResponse() {
        return mErrorNetworkErrorResponse;
    }

    public void setErrorNetworkErrorResponse(NetworkErrorResponse errorNetworkErrorResponse) {
        mErrorNetworkErrorResponse = errorNetworkErrorResponse;
    }

    public enum ErrorType {
        PARSE_ERROR, /**数据解析失败*/
        TIMEOUT, /**请求超时*/
        NO_CONNECTION, /**请求失败,没有任何Http返回*/
        SERVER_ERROR, /**请求异常,有错误的Http状态码返回*/
        NETWORK_ERROR, /**网络异常*/
        AUTH_FAILURE_ERROR, /**授权验证失败*/
    }

    public long getNetworkTimeMs() {
        return networkTimeMs;
    }

    public void setNetworkTimeMs(long networkTimeMs) {
        this.networkTimeMs = networkTimeMs;
    }

    public static RequestError buildRequestError(ErrorType errorType, String message) {
        RequestError requestError = new RequestError();
        requestError.setMessage(message);
        requestError.setErrorType(errorType);
        return requestError;
    }
}
