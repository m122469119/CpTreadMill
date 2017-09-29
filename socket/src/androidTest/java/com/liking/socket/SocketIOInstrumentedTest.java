package com.liking.socket;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.liking.socket.model.HeaderAssemble;
import com.liking.socket.model.HeaderResolver;
import com.liking.socket.model.message.MessageData;
import com.liking.socket.model.message.PingPongMsg;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
@RunWith(AndroidJUnit4.class)
public class SocketIOInstrumentedTest {
    // 异构命令字在App内部定义
    private static final byte CMD_LOGIN = (byte) 0x97; // 登录
    private static final byte CMD_TIMESTAMP = (byte) 0x66; // 时间戳

    private SocketIO mClient;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();

        SocketIO.Builder builder = new SocketIO.Builder();
//        builder.connect("192.168.2.2", 17919);
        builder.connect("120.24.177.134", 17919);
        builder.bind(context);
        builder.headerAssemble(new HeaderAssemble());
        builder.headerResolver(new HeaderResolver());
        builder.addPingPongMsg(new PingPongMsg());
        builder.addDefaultSend(getDeviceInfo());
        mClient = builder.build();
    }

    @Test
    public void testNeedFeedback() throws Exception {
        int count = 1;
        final CountDownLatch downLatch = new CountDownLatch(count + 1);

        MessageData msg = new MessageData() {
            @Override
            public byte cmd() {
                return CMD_TIMESTAMP;
            }

            @Override
            public byte[] getData() {
                JSONObject object = new JSONObject();

                return object.toString().getBytes();
            }

            @Override
            public boolean needFeedback() {
                return true;
            }

            @Override
            public void callBack(boolean isSuccess, String message) {
                System.out.println(">>>>>>>>>>callback: " + message);

                downLatch.countDown();
            }
        };
        mClient.send(msg);

        downLatch.await();
    }

    @After
    public void tearDown() {
        mClient.close(true);
    }

    public MessageData getDeviceInfo() {
        MessageData msg = new MessageData() {
            @Override
            public byte cmd() {
                return Constant.CMD_DEVICE_INFO;
            }

            @Override
            public byte[] getData() {
                JSONObject object = new JSONObject();
                try {
                    object.put("device_id", "ttdevs");
                    object.put("gateway_id", "a000000100010000");
                    object.put("device_name", "ttdevs");
                    object.put("device_type", 1);
                    object.put("control_num", 10);
                    object.put("online_status", 0);
                    object.put("battery_status", 1);
                    object.put("device_status", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return object.toString().getBytes();
            }

            @Override
            public boolean needFeedback() {
                return false;
            }

            @Override
            public void callBack(boolean isSuccess, String message) {
                // TODO: 2017/9/28
            }
        };
        return msg;
    }
}
