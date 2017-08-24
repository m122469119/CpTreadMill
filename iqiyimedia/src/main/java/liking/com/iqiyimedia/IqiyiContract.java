package liking.com.iqiyimedia;

import android.content.Context;
import android.widget.Toast;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.BaseView;
import com.aaron.android.framework.utils.PopupUtils;

import liking.com.iqiyimedia.http.IqiyiApiService;
import liking.com.iqiyimedia.http.callback.BaseRequestCallback;
import liking.com.iqiyimedia.http.result.AlbumListResult;
import liking.com.iqiyimedia.http.result.CategoryListResult;
import liking.com.iqiyimedia.http.result.TopListResult;
import liking.com.iqiyimedia.http.result.VideoInfoResult;

/**
 * Created on 2017/07/24
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class IqiyiContract {

    public final static int IQIYI_RESPONSE_FAIL_CATEGORYLIST = 1;

    public final static int IQIYI_RESPONSE_FAIL_TOPLIST = 2;

    public final static int IQIYI_RESPONSE_FAIL_ALBUMLIST = 3;

    public final static int IQIYI_RESPONSE_FAIL_VIDEOINFO = 4;

    public interface IqiyiView extends BaseView {

        void setCategoryListView(CategoryListResult categoryResult);

        void setAlbumListView(AlbumListResult result);

        void setTopListView(TopListResult result);

        void setVideoInfoView(VideoInfoResult result);

        void showFailView(String message, int failType);
    }

    public static class Presenter extends BasePresenter<IqiyiView> {

        IqiyiModel mIqiyiModel;

        public Presenter(Context context, IqiyiView mainView) {
            super(context, mainView);
            mIqiyiModel = new IqiyiModel();
        }

        public void attachView(Object tag) {
            if(tag == null) return;
            IqiyiApiService.register(tag);
        }

        public void detachView(Object tag) {
            if(tag == null) return;
            IqiyiApiService.unregister(tag);
        }

        /**
         * 获取分类列表
         * @param tag
         */
        public void getCategoryList(Object tag) {
            mIqiyiModel.getCategoryList(tag, new BaseRequestCallback<CategoryListResult>() {
                @Override
                public void success(CategoryListResult result) {
                    mView.setCategoryListView(result);
                }

                @Override
                public void onFailure(RequestError error) {
                    mView.showFailView(error.getMessage(), IQIYI_RESPONSE_FAIL_CATEGORYLIST);
                }
            });
        }

        /**
         * 获取专辑列表
         * @param tag
         * @param categoryId
         */
        public void getAlbumList(Object tag, String categoryId) {
            mIqiyiModel.getAlbumList(tag, categoryId, new BaseRequestCallback<AlbumListResult>() {
                @Override
                public void success(AlbumListResult result) {
                    mView.setAlbumListView(result);
                }

                @Override
                public void onFailure(RequestError error) {
                    mView.showFailView(error.getMessage(), IQIYI_RESPONSE_FAIL_ALBUMLIST);
                }
            });
        }

        /**
         * 获取排行榜列表
         * @param tag
         * @param categoryId
         */
        public void getTopList(Object tag, String categoryId) {
            mIqiyiModel.getTopList(tag, categoryId, "4", new BaseRequestCallback<TopListResult>() {
                @Override
                public void success(TopListResult result) {
                    mView.setTopListView(result);
                }

                @Override
                public void onFailure(RequestError error) {
                    mView.showFailView(error.getMessage(), IQIYI_RESPONSE_FAIL_TOPLIST);
                }
            });
        }

        public void getVideoInfo(Object tag, String tvQipuId) {
            mIqiyiModel.getVideoInfo(tag, tvQipuId, new BaseRequestCallback<VideoInfoResult>() {
                @Override
                public void success(VideoInfoResult result) {
                    mView.setVideoInfoView(result);
                }

                @Override
                public void onFailure(RequestError error) {
                    mView.showFailView(error.getMessage(), IQIYI_RESPONSE_FAIL_VIDEOINFO);
                }
            });
        }

    }
}
