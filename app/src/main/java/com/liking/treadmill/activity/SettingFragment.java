package com.liking.treadmill.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.adapter.TabFragmentPagerAdapter;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.liking.treadmill.R;
import com.liking.treadmill.fragment.BindGymFragment;
import com.liking.treadmill.fragment.NetworkSettingFragment;
import com.liking.treadmill.fragment.TreadmillSetupFragment;

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

public class SettingFragment extends BaseFragment {
    private static final int INDEX_BIND_GYM = 0;
    private static final int INDEX_SETUP = 1;
    private static final int INDEX_NETWORK = 2;
    @BindView(R.id.setting_viewPager)
    ViewPager mSettingViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        initViewPager();
        return view;
    }

    private void initViewPager() {
        TabFragmentPagerAdapter tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getActivity(), getChildFragmentManager(), getMainFragmentList());
        mSettingViewPager.setAdapter(tabFragmentPagerAdapter);
        mSettingViewPager.setOffscreenPageLimit(1);
    }

    private List<TabFragmentPagerAdapter.FragmentBinder> getMainFragmentList() {
        List<TabFragmentPagerAdapter.FragmentBinder> fragmentBinders = new ArrayList<>();
        fragmentBinders.add(buildBindGymSettingFragmentBinder());
        fragmentBinders.add(buildNetworkSettingFragmentBinder());
        fragmentBinders.add(buildTreadmillSetupSettingFragmentBinder());
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
