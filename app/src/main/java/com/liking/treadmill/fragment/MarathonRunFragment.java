package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.liking.treadmill.R;
import com.liking.treadmill.activity.HomeActivity;
import com.liking.treadmill.app.ThreadMillConstant;
import com.liking.treadmill.fragment.base.SerialPortFragment;
import com.liking.treadmill.message.MarathonUserInfoMessage;
import com.liking.treadmill.message.MembersDeleteMessage;
import com.liking.treadmill.module.run.RunningFragment;
import com.liking.treadmill.socket.LKProtocolsHelperKt;
import com.liking.treadmill.socket.result.MarathonRunResult;
import com.liking.treadmill.socket.result.MarathonUserInfoResult;
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

    private String marathonId = "";
    private int marathonDistance = 0;
    private long marathonTime;
    private String marathonName = "";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_marathon, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        return mRootView;
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            marathonId = bundle.getString("marathonId");
            marathonDistance = Integer.parseInt(bundle.getString("marathonDistance", "0"));
            marathonTime = Long.parseLong(bundle.getString("marathonTime", "0"));
            marathonName = bundle.getString("marathonName", "");
        }
    }

    @Override
    public void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        final HomeActivity homeActivity = (HomeActivity) getActivity();
        if (keyCode == LikingTreadKeyEvent.KEY_SET) {
            homeActivity.launchFragment(new StartFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_RETURN) {
            SerialPortUtil.getTreadInstance().reset();//清空数据
            homeActivity.setTitle("");
            homeActivity.launchFragment(new AwaitActionFragment());
        } else if (keyCode == LikingTreadKeyEvent.KEY_START) {
            startActiveMonitor();
            if (StringUtils.isEmpty(Preference.getBindUserGymId())) {//未绑定场馆
                IToast.show("场馆未绑定，请联系管理员!");
                return;
            }
            String bid = SerialPortUtil.getTreadInstance().getCardNo();
            if (!StringUtils.isEmpty(bid) && !StringUtils.isEmpty(marathonId)) {
                try {
                    homeActivity.iBackService.reportedUserMarathon(bid, marathonId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    LogUtils.e(TAG, "reportedUserMarathon ERROT!" + e.getMessage());
                }
            }

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

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MarathonUserInfoMessage message) {
        if (message != null && message.mDataBean != null) {

            final HomeActivity homeActivity = (HomeActivity) getActivity();
            RunningFragment runningFragment = new RunningFragment();
            Bundle bundle = new Bundle();
            bundle.putString("marathonName", marathonName);
            bundle.putString("marathonId", marathonId);
            bundle.putInt("marathonDistance", marathonDistance);

            MarathonUserInfoResult.DataBean dataBean = message.mDataBean;

            if (!dataBean.isFirst() && dataBean.getLastData() != null) { //非首次
                int dist = dataBean.getLastData().getDistance();//已经跑距离
                Long use_time = dataBean.getLastData().getUseTime(); //使用时间
                Float cal = dataBean.getLastData().getCal();//

                Long end_time = dataBean.getLastData().getEndTime(); //结束时间

                if (dist >= marathonDistance) {
                    IToast.show("活动已完成!");
                    return;
                } else if (DateUtils.currentDataSeconds() >= end_time) {
                    IToast.show("活动已超时!");
                    return;
                }

                //剩余距离
                int d = marathonDistance - dist;
                //剩余时间
                long t = end_time - DateUtils.currentDataSeconds();

                //剩余距离
                bundle.putInt("lastDist", dist);
                bundle.putLong("lastUseTime", use_time);
                bundle.putFloat("lastCal", cal);

                bundle.putInt("surplusDistance", d);
                bundle.putLong("surplusTime", t);
            } else {
                bundle.putInt("surplusDistance", marathonDistance);
                bundle.putLong("surplusTime", marathonTime);
            }

            runningFragment.setArguments(bundle);
            homeActivity.launchFragment(runningFragment);
        } else {
            IToast.show("活动参加失败,请重新刷卡进入！");
        }
    }
}
