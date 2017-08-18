package com.liking.treadmill.module.run

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liking.treadmill.R
import com.liking.treadmill.fragment.base.SerialPortFragment

/**
 * Created on 2017/08/16
 * desc:

 * @author: chenlei
 * *
 * @version:1.0
 */

class RunningFragment : SerialPortFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_running, container, false)

        return rootView
    }

}
