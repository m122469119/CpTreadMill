/*
package liking.com.iqiyimedia.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.android.framework.base.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import liking.com.iqiyimedia.IqiyiContract;
import liking.com.iqiyimedia.IqiyiTestBean;
import liking.com.iqiyimedia.R;
import liking.com.iqiyimedia.http.result.AlbumListResult;
import liking.com.iqiyimedia.http.result.CategoryListResult;
import liking.com.iqiyimedia.http.result.TopListResult;

import static liking.com.iqiyimedia.IqiyiTestBean.DataType.ALBUMLIST;
import static liking.com.iqiyimedia.IqiyiTestBean.DataType.CATEGORYLIST;
import static liking.com.iqiyimedia.IqiyiTestBean.DataType.TOPLIST;

*/
/**
 * Created on 2017/07/24
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 *//*


public class IqiyiVideoListFragment extends BaseFragment implements IqiyiContract.IqiyiView{

    public static final int DATA_TYPE_CATEGORY = 0; //分类

    public static final int DATA_TYPE_ALBUM = 1;

    public static final int DATA_TYPE_TOPLIST = 2; //排行榜

    public static final String IQIYI_VIDEO_DATATYPE = "IQIYI_VIDEO_DATATYPE";

    public static final String IQIYI_VIDEO_CATEGORYID = "IQIYI_VIDEO_CATEGORYID";

    private ListView mListView = null;

    private List<IqiyiTestBean> mResultList = null;

    private IqiyiContract.Presenter mIqiyiPresenter;

    private VideoListAdapter mAdapter;

    public int data_type = DATA_TYPE_CATEGORY;

    public ProgressDialog mProgressDialog = null;


   public static IqiyiVideoListFragment getInstance(Bundle bundle) {
       IqiyiVideoListFragment fragment = new IqiyiVideoListFragment();
       fragment.setArguments(bundle);
       return fragment;
   }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setPresenter();
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_iqiyi_video_list, null);
        initView(mView);
        initDdata();
        return mView;
    }


    private void setPresenter() {
        mIqiyiPresenter = new IqiyiContract.Presenter(getContext(), this);
    }

    private void initView(View v) {
        if(mResultList == null) {
            mResultList = new ArrayList<>();
        }
        mListView = (ListView) v.findViewById(R.id.video_list);
        mAdapter = new VideoListAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IqiyiTestBean bean = mResultList.get(position);
                if(bean != null) {
                    switch (bean.beanType) {
                        case CATEGORYLIST:
                            Bundle bundle = new Bundle();
                            bundle.putInt(IQIYI_VIDEO_DATATYPE, DATA_TYPE_TOPLIST);
                            bundle.putInt(IQIYI_VIDEO_CATEGORYID, bean.beanId);
                            ((PlayBackActivity)getActivity()).launchFragment(bundle);
                            break;
                        case TOPLIST:
                            Bundle bundle_ = new Bundle();
                            bundle_.putInt("tvId", bean.beanTvid);
                            ((PlayBackActivity)getActivity()).launchFragment2(bundle_);
                            break;
                    }
                }
            }
        });
        mIqiyiPresenter.attachView(v);
    }

    private void initDdata() {
        Bundle bundle = getArguments();

        if(bundle != null) {
            int type = bundle.getInt(IQIYI_VIDEO_DATATYPE);
            if(type > -1) {
                data_type = type;
            }
        }

        switch (data_type) {
            case DATA_TYPE_CATEGORY:
                showProgressDialog();
                mIqiyiPresenter.getCategoryList(this);
                break;
            case DATA_TYPE_ALBUM:
                showProgressDialog();
                int categoryId = bundle.getInt(IQIYI_VIDEO_CATEGORYID);
                mIqiyiPresenter.getAlbumList(this, String.valueOf(categoryId));
                break;
            case DATA_TYPE_TOPLIST:
                showProgressDialog();
                int categoryId_ = bundle.getInt(IQIYI_VIDEO_CATEGORYID);
                mIqiyiPresenter.getTopList(this, String.valueOf(categoryId_));
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIqiyiPresenter.detachView(this);
    }

    */
/**
     * 分类列表
     * @param categoryResult
     *//*

    @Override
    public void setCategoryListView(CategoryListResult categoryResult) {
        dismissProgressDialog();
        if(mResultList != null) {
            mResultList.clear();
            mResultList.addAll(buildCategoryData(categoryResult.getData()));
            mAdapter.notifyDataSetChanged();
        }
    }

    */
/**
     * 专辑列表
     * @param result
     *//*

    @Override
    public void setAlbumListView(AlbumListResult result) {
        dismissProgressDialog();
        if(mResultList != null) {
            mResultList.clear();
            mResultList.addAll(buildAlbumListData(result.getData()));
            mAdapter.notifyDataSetChanged();
        }
    }

    */
/**
     * 排行榜列表
     * @param result
     *//*

    @Override
    public void setTopListView(TopListResult result) {
        dismissProgressDialog();
        if(mResultList != null) {
            mResultList.clear();
            mResultList.addAll(buildTopListData(result.getData()));
            mAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void showFailView(String message) {
        dismissProgressDialog();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    */
/**
     * 分类
     * @param data
     * @return
     *//*

    private List<IqiyiTestBean> buildCategoryData(List<CategoryListResult.DataBean> data) {
        List<IqiyiTestBean> result = new ArrayList<>();
        for (CategoryListResult.DataBean crdb: data) {
            IqiyiTestBean testBean = new IqiyiTestBean();
            testBean.beanType = CATEGORYLIST;
            testBean.beanTitle = crdb.getCategoryName();
            testBean.beanId = crdb.getCategoryId();
            result.add(testBean);
        }
        return result;
    }

    */
/**
     * 专辑
     * @param data
     * @return
     *//*

    private List<IqiyiTestBean> buildAlbumListData(List<AlbumListResult.DataBean> data) {
        List<IqiyiTestBean> result = new ArrayList<>();
        for (AlbumListResult.DataBean album: data) {
            IqiyiTestBean testBean = new IqiyiTestBean();
            testBean.beanType = ALBUMLIST;
            testBean.beanTitle = album.getAlbumName();
            testBean.beanId = album.getAlbumId();
            result.add(testBean);
        }
        return result;
    }

    */
/**
     * 排行榜
     * @param data
     * @return
     *//*

    private List<IqiyiTestBean> buildTopListData(List<TopListResult.DataBean> data) {
        List<IqiyiTestBean> result = new ArrayList<>();
        for (TopListResult.DataBean top: data) {
            IqiyiTestBean testBean = new IqiyiTestBean();
            testBean.beanType = TOPLIST;
            testBean.beanTitle = top.getAlbumName();
            testBean.beanId = top.getAlbumId();
            testBean.beanUrl = top.getPlayUrl();
            if(top.getTvIds().size() > 0) {
                testBean.beanTvid = top.getTvIds().get(0);
            }
            result.add(testBean);
        }
        return result;
    }

    private void showProgressDialog() {
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载中...");
        }
        if(!mProgressDialog.isShowing() && !getActivity().isFinishing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing() && !getActivity().isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

    class VideoListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(mResultList != null) {
                return mResultList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(mResultList != null) {
                return mResultList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if(convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_iqiyi_video_item, null);
                viewHolder.mVideoName = (TextView) convertView.findViewById(R.id.video_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mVideoName.setText(mResultList.get(position).beanTitle);

            return convertView;
        }

        class ViewHolder {
            public TextView mVideoName;
        }
    }
}
*/
