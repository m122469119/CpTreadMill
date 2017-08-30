package com.liking.treadmill;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.google.gson.Gson;
import com.liking.treadmill.socket.LKProtocolsHelperKt;
import com.liking.treadmill.socket.SocketService;
import com.liking.treadmill.socket.data.request.PingData;
import com.liking.treadmill.socket.result.MemberListResult;
import com.liking.treadmill.storge.Preference;
import com.liking.treadmill.test.IBackService;
import com.liking.treadmill.utils.AESUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static android.content.Context.BIND_AUTO_CREATE;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    public IBackService iBackService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iBackService = IBackService.Stub.asInterface(iBinder);
            LogUtils.d(SocketService.TAG, "service is connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.d(SocketService.TAG, "service is disconnected");
            iBackService = null;
        }
    };
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

//        assertEquals("com.liking.treadmill", appContext.getPackageName());

//        System.out.println(LKProtocolsHelperKt.INSTANCE.getPingRequest());
//        System.out.println(LKProtocolsHelperKt.INSTANCE.getReportDevicesRequest());

//        System.out.println(LKProtocolsHelperKt.INSTANCE.getBindQrcodeRequest());
//        System.out.println(LKProtocolsHelperKt.INSTANCE.getConfirmRequest());
//        System.out.println(LKProtocolsHelperKt.INSTANCE.getUserLoginRequest(27777788382L));
//
//        System.out.println(LKProtocolsHelperKt.INSTANCE.getUserLogoutRequest(37777788382L));

    }

}
