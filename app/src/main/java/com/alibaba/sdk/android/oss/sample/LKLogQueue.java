package com.alibaba.sdk.android.oss.sample;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.aaron.android.codelibrary.utils.DateUtils;
import com.liking.treadmill.app.LikingThreadMillApplication;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created on 2017/5/3
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class LKLogQueue {
    private BlockingQueue<String> mQueue;
    private Handler mHandler;
    private FileOutConsumer mFileOutConsumer;
    private boolean isLoop = false;
    private  HandlerThread mFileInProducer;
    private FileHelper mFileHelper;
    private LKReportTimer mTimer;
    private String mFileDir;

    private LKLogQueue(Context context, int queueCount, String fileDir, LKReportTimer mTimer) {
        this.mFileDir = fileDir;
        mQueue = new LinkedBlockingQueue<>(queueCount);
        mFileHelper = new FileHelper(context, fileDir);
        mFileOutConsumer = new FileOutConsumer(mQueue);
        mFileInProducer = new HandlerThread("FileIn" + UUID.randomUUID());
        mFileInProducer.start();
        mHandler = new Handler(mFileInProducer.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                try {
                    String[] logs = (String[]) msg.obj;
                    StringBuilder sb = new StringBuilder();
                    sb.append(DateUtils.formatDate("yyyy.MM.dd HH:mm:ss",
                            new Date(System.currentTimeMillis())))
                            .append("-");
                    for (String log : logs) {
                        sb.append(log);
                    }
                    mQueue.put(sb.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        isLoop = true;
        mFileOutConsumer.start();

        if (mTimer != null) {
            this.mTimer = mTimer;
        } else {
            this.mTimer = new LKReportTimer(8, 0, 0, new LKReportTimer.CallBack() {
                @Override
                public void doLoopTh() {
                    LKOss.getInstance().putObjectSamples(mFileHelper, mFileDir);
                }

                @Override
                public void doOnceTh() {
                    LKOss.getInstance().putObjectSamples(mFileHelper, mFileDir);
                }
            });
        }
        this.mTimer.start();
    }

    public void putOnce(){
        mTimer.putOnce();
    }


    public void put(String... logs){
        Message msg = mHandler.obtainMessage();
        msg.obj = logs;
        mHandler.sendMessage(msg);
    }

    public void release(){
        mHandler.removeCallbacksAndMessages(null);
        mQueue = null;
        isLoop = false;
        mFileOutConsumer = null;
        mFileInProducer.quit();
        mTimer.stop();
    }

    /**
     * 写入文件
     */
    private class FileOutConsumer extends Thread {
        private final BlockingQueue<String> shareQueue;
        public FileOutConsumer(BlockingQueue<String> shareQueue) {
            this.shareQueue = shareQueue;
        }
        @Override
        public void run() {
            while (isLoop) {
                try {
                    mFileHelper.save(shareQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class LKLogQueueBuilder {
        private final static int DEFAULT_QUEUE_COUNT = 20;
        private int mQueueCount = DEFAULT_QUEUE_COUNT;
        private LKReportTimer mTimer;
        private String mFileDir = "logs";
        private Context mContext;

        public LKLogQueueBuilder setQueueCount(int mQueueCount) {
            this.mQueueCount = mQueueCount;
            return this;
        }

        public LKLogQueueBuilder setFileDir(String mFileDir) {
            this.mFileDir = mFileDir;
            return this;
        }

        public void setTimer(LKReportTimer mTimer) {
            this.mTimer = mTimer;
        }

        public LKLogQueue build(){
            return new LKLogQueue(mContext, mQueueCount, mFileDir, mTimer);
        }

        public LKLogQueueBuilder setApplicationContext(LikingThreadMillApplication applicationContext) {
            this.mContext = applicationContext;
            return this;
        }
    }

}
