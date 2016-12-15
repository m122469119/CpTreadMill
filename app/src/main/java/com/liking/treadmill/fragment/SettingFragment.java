package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.adapter.TabFragmentPagerAdapter;
import com.liking.treadmill.R;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/12/11.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class SettingFragment extends SerialPortFragment {
    private static final int INDEX_BIND_GYM = 0;
    private static final int INDEX_SETUP = 1;
    private static final int INDEX_NETWORK = 2;
    @BindView(R.id.setting_viewPager)
    ViewPager mSettingViewPager;

    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        initViewPager();
        return view;
    }

    @Override
    public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {
        super.onTreadKeyDown(keyCode, event);
        if (keyCode.equals(LikingTreadKeyEvent.KEY_RETURN)) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "------onResume()");
    }



    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "------onPause()");

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "------onStop()");
    }

    private void initViewPager() {
        TabFragmentPagerAdapter tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getActivity(), getChildFragmentManager(), getMainFragmentList());
        mSettingViewPager.setAdapter(tabFragmentPagerAdapter);
        mSettingViewPager.setOffscreenPageLimit(1);
    }

    private List<TabFragmentPagerAdapter.FragmentBinder> getMainFragmentList() {
        List<TabFragmentPagerAdapter.FragmentBinder> fragmentBinders = new ArrayList<>();
        fragmentBinders.add(buildNetworkSettingFragmentBinder());
        fragmentBinders.add(buildTreadmillSetupSettingFragmentBinder());
        fragmentBinders.add(buildBindGymSettingFragmentBinder());
        return fragmentBinders;
    }

    private TabFragmentPagerAdapter.FragmentBinder buildBindGymSettingFragmentBinder() {
        return new TabFragmentPagerAdapter.FragmentBinder(INDEX_BIND_GYM,
                getString(R.string.tab_bind_gym), 0,
                BindGymFragment.instantiate(getActivity(), BindGymFragment.class.getName()));
    }

    private TabFragmentPagerAdapter.FragmentBinder buildTreadmillSetupSettingFragmentBinder() {
        return new TabFragmentPagerAdapter.FragmentBinder(INDEX_SETUP,
                getString(R.string.tab_setup), 0,
                TreadmillSetupFragment.instantiate(getActivity(), TreadmillSetupFragment.class.getName()));
    }

    private TabFragmentPagerAdapter.FragmentBinder buildNetworkSettingFragmentBinder() {
        return new TabFragmentPagerAdapter.FragmentBinder(INDEX_NETWORK,
                getString(R.string.tab_network), 0,
                NetworkSettingFragment.instantiate(getActivity(), NetworkSettingFragment.class.getName()));
    }
}
