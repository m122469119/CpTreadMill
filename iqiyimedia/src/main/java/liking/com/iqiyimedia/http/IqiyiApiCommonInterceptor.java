package liking.com.iqiyimedia.http;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;

import java.io.IOException;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 16/11/29.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class IqiyiApiCommonInterceptor implements Interceptor {

    private static final String PLATFORM_ANDROID = "android";
    private static final String KEY_PLATFORM = "platform";
    private static final String TAG = "CommonInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();
        HttpUrl.Builder httpUrlBuilder = oldRequest.url().newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter("apiKey", IqiyiApiHelper.IQIYI_APIKEY);

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(httpUrlBuilder.build())
                .build();

        Set<String> queryKeys =  newRequest.url().queryParameterNames();
        String queryValue;
        for (String params : queryKeys) {
            queryValue = newRequest.url().queryParameter(params);
            LogUtils.d(TAG, "queryKey: " + params + " queryValue: " + queryValue);
        }
        LogUtils.d(TAG, "request url: " + newRequest.url().url().toString());
        return chain.proceed(newRequest);
    }
}
