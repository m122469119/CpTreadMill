package liking.com.iqiyimedia.http;

import android.support.v4.util.ArrayMap;

import com.aaron.android.framework.library.http.retrofit.RetrofitRequest;
import com.aaron.android.framework.library.http.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import liking.com.iqiyimedia.http.callback.BasePagerRequestCallback;
import liking.com.iqiyimedia.http.result.AlbumListResult;
import liking.com.iqiyimedia.http.result.CategoryListResult;
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

    public static void getCategoryList(Object tag, BasePagerRequestCallback<CategoryListResult> callback) {
        new RetrofitRequest<CategoryListResult>
                (addRequestCalls(tag, getIqiyiApi().getCategoryList())).execute(callback);
    }

    public static void getAlbumList(Object tag, String categoryId, BasePagerRequestCallback<AlbumListResult> callback) {
        new RetrofitRequest<AlbumListResult>
                (addRequestCalls(tag, getIqiyiApi().getAlbumList(categoryId))).execute(callback);
    }

    private static Call addRequestCalls(Object tag, Call call) {
        if(tag == null) return call;

        if (requestCalls == null) {
            requestCalls = new ArrayMap<>();
        }
        List<Call> calls = requestCalls.get(tag);
        if (calls == null) {
            calls = new ArrayList<>();
            requestCalls.put(tag, calls);
        }
        if (!calls.contains(call)) {
            calls.add(call);
        }
        return call;
    }

    public static void removeRequestAllCalls(Object tag) {
        List<Call> calls = requestCalls.get(tag);
        if (calls != null) {
            calls.clear();
            requestCalls.remove(tag);
        }
    }


}
