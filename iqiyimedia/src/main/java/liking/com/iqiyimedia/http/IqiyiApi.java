package liking.com.iqiyimedia.http;

import liking.com.iqiyimedia.http.result.AlbumListResult;
import liking.com.iqiyimedia.http.result.CategoryListResult;
import liking.com.iqiyimedia.http.result.TopListResult;
import liking.com.iqiyimedia.http.result.VideoInfoResult;
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
                                   @Query("topType") String topType,
                                   @Query("limit") String limit);

    @GET(Urls.IQIYI_VIDEO_INFO)
    Call<VideoInfoResult> getVideoInfo(@Query("qipuId") String tvQipuId);

    class Urls {
        static final String IQIYI_CATEGORY_LIST = "category/list.json";

        static final String IQIYI_ALBUM_LIST = "album/list.json";

        static final String IQIYI_TOP_LIST = "top/list.json";

        static final String IQIYI_VIDEO_INFO = "video/info.json";
    }
}
