/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.liking.treadmill.activity;

import android.os.Bundle;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;

import androidex.serialport.SerialPortUtil;


public abstract class SerialPortActivity extends LikingTreadmillBaseActivity implements SerialPortUtil.SerialPortCallback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtils.d("SerialPortActivity", "------onResume()");
		SerialPortUtil.getInstance().setSerialPortCallback(this);
	}

    @Override
    protected void onPause() {
        super.onPause();
		LogUtils.d("SerialPortActivity", "------onPause()");
        SerialPortUtil.getInstance().setSerialPortCallback(null);
    }


	@Override
	public void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event) {

	}
}
