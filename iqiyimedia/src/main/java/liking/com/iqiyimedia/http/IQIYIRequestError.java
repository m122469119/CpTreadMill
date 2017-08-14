package liking.com.iqiyimedia.http;

import com.aaron.android.codelibrary.http.RequestError;

/**
 * Created on 2017/07/22
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class IQIYIRequestError extends RequestError{

    private String serverCode;

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }
}
