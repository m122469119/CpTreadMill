package com.liking.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.ui.BaseFragment;
import com.liking.treadmill.R;

/**
 * Created on 16/12/12.
 * 绑定场馆
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class BindGymFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_gym, container, false);
        return view;
    }
}
