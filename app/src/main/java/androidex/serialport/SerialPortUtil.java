package androidex.serialport;

import android.os.Handler;
import android.os.Looper;

import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortReceiveUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 串口操作类
 *
 * @author Jerome
 */
public class SerialPortUtil {
    private String TAG = SerialPortUtil.class.getSimpleName();
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private final static String DEVICE = "/dev/ttyS3";
    private final static int BAUDRATE = 57600;
    private static SerialPortUtil portUtil;
    private SerialPortCallback mSerialPortCallback = null;
    private boolean isStop = false;

    public interface SerialPortCallback {
        void onTreadKeyDown(String keyCode, LikingTreadKeyEvent event);
    }

    public void setSerialPortCallback(
            SerialPortCallback dataReceiveListener) {
        mSerialPortCallback = dataReceiveListener;
    }

    public static SerialPortUtil getInstance() {
        if (null == portUtil) {
            portUtil = new SerialPortUtil();
            portUtil.onCreate();
        }
        return portUtil;
    }

    /**
     * 初始化串口信息
     */
    public void onCreate() {
        try {
            mSerialPort = new SerialPort(new File(DEVICE), BAUDRATE, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            mReadThread = new ReadThread();
            isStop = false;
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送指令到串口
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds(String cmd) {
        boolean result = true;
        byte[] mBuffer = (cmd + "\r\n").getBytes();
//注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer  
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
        String tail = "\r\n";
        byte[] tailBuffer = tail.getBytes();
        byte[] mBufferTemp = new byte[mBuffer.length + tailBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBufferTemp);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (true) {
                int size;
                try {
                    byte[] buffer = new byte[25];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    final byte[] serialPortData = buildBuffer(size, buffer);
                    if (mSerialPortCallback != null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                mSerialPortCallback.onTreadKeyDown(
                                        SerialPortReceiveUtil.getKeyCodeFromSerialPort(serialPortData), new LikingTreadKeyEvent());
                            }
                        });

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private final static int BUFFER_LENGTH = 40;
    private byte[] mBuffer = new byte[BUFFER_LENGTH];

    private byte[] buildBuffer(int size, byte[] buffer) {
        byte[] cacheBuffer = new byte[25];
        if (size > 0) {
            for (int i = 0; i < BUFFER_LENGTH; i++) {
                if (i < BUFFER_LENGTH - size) {
                    mBuffer[i] = mBuffer[i + size];
                } else {
                    for (int j = 0; j < size; j++) {
                        mBuffer[BUFFER_LENGTH - size + j] = buffer[j];
                    }
                }
            }
        }
        for (int i = 15; i < BUFFER_LENGTH; i++) {
            cacheBuffer[i - 15] = mBuffer[i];
        }
        return cacheBuffer;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        isStop = true;
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
        }
    }

}  