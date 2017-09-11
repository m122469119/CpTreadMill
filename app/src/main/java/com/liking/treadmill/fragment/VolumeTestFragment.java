package com.liking.treadmill.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.fragment.base.SerialPortFragment;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/09/11
 * desc: 声音测试界面
 *
 * @author: chenlei
 * @version:1.0
 */

public class VolumeTestFragment extends SerialPortFragment {

    private View mRootView;
    private HomeActivity mActivity = null;

    @BindView(R.id.volume_test_max_value)
    TextView volumeTestMaxValue;

    @BindView(R.id.volume_test_current_value)
    TextView volumeTestCurrentValue;

    @BindView(R.id.volume_test_playstate_value)
    TextView volumeTestPlaystateValue;

    private boolean isPlay = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context instanceof HomeActivity) {
            mActivity = (HomeActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_volume_test, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    private void initView() {
        if (mActivity == null) return;
        int max = mActivity.getAudioManager().getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 音乐音量
        volumeTestMaxValue.setText(String.valueOf(max));
        setCurrvolumeValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mActivity != null) {
            mActivity.setTitle("音频播放测试");
        }
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        volumeControl(keyCode);

        if (keyCode == LikingTreadKeyEvent.KEY_PLAY_PAUSE_MEDIA) {
            volumeTestPlaystateValue.setText("播放中...");
            if (isPlay) return;
            playRingtone(getContext(), RingtoneManager.TYPE_NOTIFICATION);
        } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            if(mActivity != null) {
                mActivity.launchInit();
            }
        }
    }

    public void volumeControl(int keyCode) {
        if (mActivity == null) return;
        if (keyCode == LikingTreadKeyEvent.KEY_VOL_PLUS) {// +
            mActivity.volumeAdd();
            setCurrvolumeValue();
        } else if (keyCode == LikingTreadKeyEvent.KEY_VOL_REDUCE) {
            mActivity.volumeSubtract();
            setCurrvolumeValue();
        }
    }

    private void setCurrvolumeValue() {
        volumeTestCurrentValue.setText(String.valueOf(mActivity.getAudioManager().getStreamVolume(AudioManager.STREAM_MUSIC)));
    }

    /**
     * @param context
     * @param type    RingtoneManager.TYPE_ALARM,   TYPE_NOTIFICATION
     */
    public void playRingtone(Context context, int type) {
        try {
            isPlay = true;
            Uri notification = RingtoneManager.getDefaultUri(type);
            MediaPlayer player = MediaPlayer.create(context, notification);
            player.setLooping(false);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    volumeTestPlaystateValue.setText("播放结束！");
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    isPlay = false;
                }
            });
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
