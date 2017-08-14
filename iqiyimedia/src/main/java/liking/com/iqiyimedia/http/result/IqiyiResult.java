package liking.com.iqiyimedia.http.result;

import com.aaron.android.codelibrary.http.result.Result;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.google.gson.annotations.SerializedName;

import liking.com.iqiyimedia.http.IqiyiResponseCode;

/**
 * Created on 2017/07/22
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class IqiyiResult implements Result{

    @SerializedName("code")
    private String mCode;

    private String mReqMess;

    public String getReqMess() {
        return mReqMess;
    }

    public void setReqMess(String reqMess) {
        mReqMess = reqMess;
    }

    public String getCode() {
        return mCode;
    }

    @Override
    public boolean isSuccess() {
        return !StringUtils.isEmpty(mCode) &&
                (IqiyiResponseCode.IQIYI_OBTAIN_SUCCESS.equals(mCode)
                        || IqiyiResponseCode.IQIYI_REGISTER_SUCCESS.equals(mCode)
                        || IqiyiResponseCode.IQIYI_UPDATE_SUCCESS.equals(mCode));
    }
}
