package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.app.ThreadMillConstant;
import com.liking.treadmill.fragment.base.SerialPortFragment;
import com.liking.treadmill.module.run.RunningFragment;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;
import com.liking.treadmill.widget.IToast;

import butterknife.ButterKnife;

/**
 * Created on 2017/09/02
 * desc: 马拉松活动展示页
 *
 * @author: chenlei
 * @version:1.0
 */

public class MarathonRunFragment extends SerialPortFragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_marathon, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        final HomeActivity homeActivity = (HomeActivity) getActivity();
        if(keyCode == LikingTreadKeyEvent.KEY_SET) {
            homeActivity.launchFragment(new StartFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            SerialPortUtil.getTreadInstance().reset();//清空数据
            homeActivity.setTitle("");
            homeActivity.launchFragment(new AwaitActionFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_START) {
            if(StringUtils.isEmpty(Preference.getBindUserGymId())) {//未绑定场馆
                startActiveMonitor();
                IToast.show("场馆未绑定，请联系管理员!");
                return;
            }
            RunningFragment runningFragment = new RunningFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(runningFragment.getRUNING_START_MODE_KEY(),
                    ThreadMillConstant.THREADMILL_MODE_SELECT_MARATHON);
            runningFragment.setArguments(bundle);
            homeActivity.launchFragment(runningFragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startActiveMonitor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopActiveMonitor();
    }
}
