package com.liking.socket;

import com.liking.socket.model.HeaderAssemble;
import com.liking.socket.model.HeaderResolver;
import com.liking.socket.resolver.PingPong;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ttdevs
 * 2017-09-18 (Socket)
 * https://github.com/ttdevs
 */
public class SocketIOTest {

    private SocketIO mClient;

    @Before
    public void setUp() {
        SocketIO.Builder builder = new SocketIO.Builder();
//        builder.connect("192.168.2.2", 17919);
//        builder.connect("192.168.32.43", 17919);
        builder.connect("120.24.177.134", 17919);
        builder.addDefaultParse(new PingPong());
        builder.headerResolver(new HeaderResolver());
        builder.headerAssemble(new HeaderAssemble());
        mClient = builder.build();
    }

    @Test
    public void testSocketClient() throws Exception {
        final CountDownLatch downLatch = new CountDownLatch(1);
//        for (int i = 0; i < 3; i++) {
//            mClient.getSender().sendMessage(new PingPongMsg());
//            Thread.sleep(2000);
//        }
        downLatch.await();
    }

    @After
    public void tearDown() {
        mClient.close(true);
    }
}
