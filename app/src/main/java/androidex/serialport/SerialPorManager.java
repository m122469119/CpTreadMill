package androidex.serialport;

import android.os.Handler;
import android.os.Looper;

import com.liking.treadmill.treadcontroller.LikingTreadKeyEvent;
import com.liking.treadmill.treadcontroller.SerialPortUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 串口操作类
 *
 * @author Jerome
 */
public class SerialPorManager {
    private String TAG = SerialPorManager.class.getSimpleName();
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private final static String DEVICE = "/dev/ttyS3";
    private final static int BAUDRATE = 57600;
    private static SerialPorManager portUtil;
    private SerialPortCallback mSerialPortCallback = null;
    private boolean isStop = false;
    private final static int RESP_BYTE_LENGTH = 11;

    public interface SerialPortCallback {
        void onTreadKeyDown(int keyCode, LikingTreadKeyEvent event);

        void handleTreadData(SerialPortUtil.TreadData treadData);
    }

    public void setSerialPortCallback(
            SerialPortCallback dataReceiveListener) {
        mSerialPortCallback = dataReceiveListener;
    }

    public static SerialPorManager getInstance() {
        if (null == portUtil) {
            portUtil = new SerialPorManager();
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

    public void sendMessage(byte[] buffer) {
        int index = 0;
        if (buffer == null) {
            return;
        }
        if (buffer.length < RESP_BYTE_LENGTH) {
            return;
        }
        byte[] message = new byte[RESP_BYTE_LENGTH + 5];
        message[index++] = (byte) 0xAA;
        message[index++] = (byte) 0x55;
        message[index++] = buffer[0];
        message[index++] = buffer[1];
        message[index++] = buffer[2];
        message[index++] = buffer[3];
        message[index++] = buffer[4];
        message[index++] = buffer[5];
        message[index++] = buffer[6];
        message[index++] = buffer[7];
        message[index++] = buffer[8];
        message[index++] = buffer[9];
        message[index++] = buffer[10];
        message[index++] = checkSum(2, 12, message);
        message[index++] = (byte) 0xC3;
        message[index++] = (byte) 0x3C;
        if (mOutputStream != null) {
            try {
                mOutputStream.write(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte checkSum(final int start, final int mEnd, byte[] bytes) {
        int sum = 0;
        if (bytes == null) return 0;
        int length = bytes.length;
        if (start < 0 || mEnd < 0 || start > mEnd || start >= length || mEnd >= length) {
            return 0;
        }
        for (int i = start; i <= mEnd; i++) {
            sum += byteToInt(bytes[i]);
        }
        int n = (sum >> 0) & 0xFF;
        byte b = (byte) n;
        return b;
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
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
                                if (mSerialPortCallback != null) {
                                    mSerialPortCallback.onTreadKeyDown(
                                            SerialPortUtil.getKeyCodeFromSerialPort(serialPortData), new LikingTreadKeyEvent());
                                    SerialPortUtil.updateTreadDataFromSerialPort(serialPortData);
                                    if (SerialPortUtil.getTreadInstance() != null) {
                                        mSerialPortCallback.handleTreadData(SerialPortUtil.getTreadInstance());
                                    }
                                }
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