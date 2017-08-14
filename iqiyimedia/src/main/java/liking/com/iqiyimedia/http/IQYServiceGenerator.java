package liking.com.iqiyimedia.http;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IQYServiceGenerator {

    private final static String BASE_URL = "http://expand.video.iqiyi.com/api/";
    private static final int CONNECT_TIMEOUT = 10; //秒
    private static OkHttpClient.Builder sOkHttpClientBuilder = new OkHttpClient
            .Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);

    private static Retrofit.Builder sRetrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());

    /**
     *
     * @param serviceClass api class<>
     * @param interceptor 添加公共参数
     * @param <S> api class
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, Interceptor interceptor) {
        Retrofit retrofit = sRetrofitBuilder.client(sOkHttpClientBuilder.addInterceptor(interceptor).build()).build();
        return retrofit.create(serviceClass);
    }
}