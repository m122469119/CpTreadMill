package liking.com.iqiyimedia.http;

import liking.com.iqiyimedia.http.result.AlbumListResult;
import liking.com.iqiyimedia.http.result.CategoryListResult;
import liking.com.iqiyimedia.http.result.TopListResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created on 2017/07/22
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface IqiyiApi {



    @GET(Urls.IQIYI_CATEGORY_LIST)
    Call<CategoryListResult> getCategoryList();

    @GET(Urls.IQIYI_ALBUM_LIST)
    Call<AlbumListResult> getAlbumList(@Query("categoryId") String categoryId);

    @GET(Urls.IQIYI_TOP_LIST)
    Call<TopListResult> getTopList(@Query("categoryId") String categoryId,
                                   @Query("topType") String topType);

    class Urls {
        public static final String IQIYI_CATEGORY_LIST = "category/list.json" ;

        public static final String IQIYI_ALBUM_LIST = "album/list.json" ;

        public static final String IQIYI_TOP_LIST = "top/list.json" ;
    }
}
