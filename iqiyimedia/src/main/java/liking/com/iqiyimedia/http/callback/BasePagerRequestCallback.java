package liking.com.iqiyimedia.http.callback;

import android.support.v4.util.ArrayMap;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.http.helper.VerifyResultUtils;
import com.aaron.android.framework.utils.ResourceUtils;

import liking.com.iqiyimedia.R;
import liking.com.iqiyimedia.http.IQIYIRequestError;
import liking.com.iqiyimedia.http.IqiyiApiHelper;
import liking.com.iqiyimedia.http.result.IqiyiResult;


public abstract class BasePagerRequestCallback<T extends IqiyiResult> extends RequestCallback<T> {

    private static final String TAG = "BasePagerRequestCallback";

    @Override
    public void onSuccess(T result) {
        if (isValid(result)) {
            success(result);
        }
    }

    private boolean isValid(IqiyiResult result) {
        if (VerifyResultUtils.checkResultSuccess(result)) {
            return true;
        }

        IQIYIRequestError serverError = new IQIYIRequestError();
        serverError.setErrorType(RequestError.ErrorType.SERVER_ERROR);

        if (result != null) {
            final String errorCode = result.getCode();
            LogUtils.e(TAG, "request server error: " + errorCode);

            if(!StringUtils.isEmpty(errorCode)) {
                serverError.setServerCode(errorCode);
//                serverError.setMessage(ResourceUtils.getString(IqiyiApiHelper.eCodeAndMess.get(errorCode)));
            } else {
//                serverError.setMessage(ResourceUtils.getString(R.string.network_anomaly_text));
            }
        } else {
//            serverError.setMessage(ResourceUtils.getString(R.string.network_anomaly_text));
        }
        onFailure(serverError);
        return false;
    }

    public abstract void success(T result);

}
