package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.message.GymBindSuccessMessage;
import com.liking.treadmill.message.GymUnBindSuccessMessage;
import com.liking.treadmill.message.QrCodeMessage;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liking.treadmill.app.ThreadMillConstant.THREADMILL_SYSTEMSETTING;

/**
 * Created on 16/12/12.
 * 绑定场馆
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class BindGymFragment extends SerialPortFragment {

    @BindView(R.id.bind_gym_step_txt)
    LinearLayout mLayoutBindGymStep;
    @BindView(R.id.qrcode_imageView)
    HImageView mQrcodeImageView;
    @BindView(R.id.bind_gym_hint_step3)
    TextView bindGymHintStep;
    @BindView(R.id.bind_gym_hint_bind)
    TextView bindGymHintBind;
    @BindView(R.id.bind_gym_layout_qrcode)
    RelativeLayout mlayoutQrcode;
    @BindView(R.id.bind_gym_hint_skip)
    TextView bindGymHintSkip;
    @BindView(R.id.bind_gym_hint)
    TextView bindGymHit;
    @BindView(R.id.network_setting_hint)
    TextView netWorkSettingHint;

    @BindView(R.id.qrcode_hint1)
    TextView mQrcodeHint1;
    @BindView(R.id.qrcode_hint2)
    TextView mQrcodeHint2;
    @BindView(R.id.qrcode_hint3)
    TextView mQrcodeHint3;

    private boolean isBindGym; //是否绑定场馆
    private boolean isSetting = true; //系统设置进入

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_gym, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    @Override
    public boolean isInViewPager() {
        return !isSetting;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d(TAG, "------setUserVisibleHint():" + isVisibleToUser);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if(bundle !=null ) {
            isSetting = bundle.getBoolean(THREADMILL_SYSTEMSETTING, false);
        }

        isBindGym = !StringUtils.isEmpty(Preference.getBindUserGymId());

        HomeActivity homeActivity = (HomeActivity)getActivity();
        if(isBindGym) { //已绑定
            try {
                if(homeActivity.iBackService != null) {
                    homeActivity.iBackService.unBind();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else { //未绑定
            String url = Preference.getQCodeUrl();
            LogUtils.d(TAG, url);
            if(!StringUtils.isEmpty(url)) {
                HImageLoaderSingleton.getInstance().loadImage(mQrcodeImageView, url);
            } else {
                try {
                    if(homeActivity.iBackService != null) {
                        homeActivity.iBackService.bind();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void initView() {
        String state = "";
        if(isBindGym) { //已绑定
            state = "解绑";
        } else {
            state = "绑定";
        }
        bindGymHit.setText("完成" + state + "后，即可完成设置");
        mQrcodeHint1.setText("通过手机扫码,安全" + state );
        mQrcodeHint2.setText(state + "健身房");
        mQrcodeHint3.setText("打开手机微信\n扫一扫" + state);

        if(isSetting) {
            ((HomeActivity)getActivity()).setTitle(state + "场馆");
            mLayoutBindGymStep.setVisibility(View.GONE);
            netWorkSettingHint.setVisibility(View.VISIBLE);
            SpannableStringBuilder ssbh = new SpannableStringBuilder(ResourceUtils.getString(R.string.threadmill_system_update_operate_txt));
            ImageSpan imageSpanBack = new ImageSpan(getActivity(), R.drawable.key_back);
            ssbh.setSpan(imageSpanBack, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            netWorkSettingHint.setText(ssbh);
        } else {
            mLayoutBindGymStep.setVisibility(View.VISIBLE);
            netWorkSettingHint.setVisibility(View.GONE);
        }

    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            if(isSetting) {
                ((HomeActivity) getActivity()).setTitle("");
                ((HomeActivity) getActivity()).launchFragment(new SettingFragment());
            }
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    /**
     * 绑定成功
     * @param message
     */
    public void onEvent(GymBindSuccessMessage message) {
        showFinishView();
    }

    /**
     * 解绑成功
     *  @param message
     */
    public void onEvent(GymUnBindSuccessMessage message) {
        showFinishView();
    }

    /**
     * 二维码显示
     * @param message
     */
    public void onEvent(QrCodeMessage message) {
        String url = Preference.getQCodeUrl();
        LogUtils.d(TAG, url);
        if(!StringUtils.isEmpty(url)) {
            HImageLoaderSingleton.getInstance().loadImage(mQrcodeImageView, url);
        }
    }

    public void showFinishView() {
        bindGymHintStep.setVisibility(View.INVISIBLE);
        bindGymHit.setVisibility(View.INVISIBLE);
        bindGymHintBind.setVisibility(View.INVISIBLE);
        mlayoutQrcode.setVisibility(View.INVISIBLE);
        bindGymHintSkip.setVisibility(View.VISIBLE);
        bindGymHit.setVisibility(View.INVISIBLE);
        Preference.setQcodeUrl("");
    }
}
