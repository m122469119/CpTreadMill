package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.liking.treadmill.R;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/12/12.
 * 绑定场馆
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class BindGymFragment extends SerialPortFragment {
    @BindView(R.id.qrcode_imageView)
    HImageView mQrcodeImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_gym, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        String url = Preference.getQCodeUrl();
        LogUtils.d(TAG,url);
        HImageLoaderSingleton.getInstance().loadImage(mQrcodeImageView,url);
    }

    @Override
    public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);

    }
}
