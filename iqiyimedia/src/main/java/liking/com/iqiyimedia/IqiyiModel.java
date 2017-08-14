package liking.com.iqiyimedia;

import liking.com.iqiyimedia.http.IqiyiApiService;
import liking.com.iqiyimedia.http.callback.BaseRequestCallback;
import liking.com.iqiyimedia.http.result.AlbumListResult;
import liking.com.iqiyimedia.http.result.CategoryListResult;
import liking.com.iqiyimedia.http.result.TopListResult;

/**
 * Created on 2017/07/24
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class IqiyiModel {

    public void getCategoryList(Object tag, BaseRequestCallback<CategoryListResult> callback) {
        IqiyiApiService.getCategoryList(tag, callback);
    }

    public void getAlbumList(Object tag, String categoryId, BaseRequestCallback<AlbumListResult> callback) {
        IqiyiApiService.getAlbumList(tag, categoryId, callback);
    }

    public void getTopList(Object tag, String categoryId, String topType, BaseRequestCallback<TopListResult> callback) {
        IqiyiApiService.getTopList(tag, categoryId, topType, callback);
    }

}
