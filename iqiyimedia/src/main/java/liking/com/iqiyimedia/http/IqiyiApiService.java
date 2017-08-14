package liking.com.iqiyimedia.http;

import android.support.v4.util.ArrayMap;

import com.aaron.android.framework.library.http.retrofit.RetrofitRequest;

import java.util.ArrayList;
import java.util.List;

import liking.com.iqiyimedia.http.callback.BaseRequestCallback;
import liking.com.iqiyimedia.http.result.AlbumListResult;
import liking.com.iqiyimedia.http.result.CategoryListResult;
import liking.com.iqiyimedia.http.result.TopListResult;
import retrofit2.Call;

/**
 * Created on 2017/07/22
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class IqiyiApiService {

    private static IqiyiApi mIqiyiApi;

    private static ArrayMap<Object, List<Call>> requestCalls = null;

    private static IqiyiApi getIqiyiApi() {
        if (mIqiyiApi == null) {
            mIqiyiApi = IQYServiceGenerator.createService(IqiyiApi.class, new IqiyiApiCommonInterceptor());
        }
        return mIqiyiApi;
    }

    private static Call addRequestCalls(Object tag, Call call) {
        if (tag == null) return call;

        List<Call> calls = register(tag);

        if (!calls.contains(call)) {
            calls.add(call);
        }
        return call;
    }

    public static List<Call> register(Object tag) {

        if (tag == null) return null;

        if (requestCalls == null) {
            requestCalls = new ArrayMap<>();
        }
        List<Call> calls = requestCalls.get(tag);

        if (calls == null) {
            calls = new ArrayList<>();
            requestCalls.put(tag, calls);
        }
        return calls;
    }

    public static void unregister(Object tag) {
        List<Call> calls = requestCalls.get(tag);
        if (calls != null) {
            calls.clear();
            requestCalls.remove(tag);
        }
    }

    public static void getCategoryList(Object tag, BaseRequestCallback<CategoryListResult> callback) {
        new RetrofitRequest<CategoryListResult>
                (addRequestCalls(tag, getIqiyiApi().getCategoryList())).execute(callback);
    }

    public static void getAlbumList(Object tag, String categoryId, BaseRequestCallback<AlbumListResult> callback) {
        new RetrofitRequest<AlbumListResult>
                (addRequestCalls(tag, getIqiyiApi().getAlbumList(categoryId))).execute(callback);
    }

    public static void getTopList(Object tag, String categoryId, String topType, BaseRequestCallback<TopListResult> callback) {
        new RetrofitRequest<TopListResult>
                (addRequestCalls(tag, getIqiyiApi().getTopList(categoryId, topType))).execute(callback);
    }
}
