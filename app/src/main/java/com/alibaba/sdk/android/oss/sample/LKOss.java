package com.alibaba.sdk.android.oss.sample;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.liking.treadmill.BuildConfig;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created on 2017/5/3
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class LKOss {
    private LKOss mInstance;

    private static final String TAG = "LKOss";
    private OSS oss;
    private static Executor mExecutor = Executors.newFixedThreadPool(10);

    // 运行sample前需要配置以下字段为有效的值
    private static final String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String accessKeyId = "LTAIf0FoSk5P6lns";
    private static final String accessKeySecret = "ljIueos2VONahInV2ycQ6LHR6Iwkpk";
    private static final String testBucket = "hud-log";

    private static final String uploadObject = EnvironmentUtils.Config.isDebugMode() ? "logs_test/" : "logs/";

    private LKOss() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        if (BuildConfig.LOGGER) {
            OSSLog.enableLog();
        } else {
            OSSLog.disableLog();
        }
        oss = new OSSClient(BaseApplication.getInstance(), endpoint, credentialProvider, conf);
    }
    public static LKOss getInstance() {
        return LKOssHolder.INSTANCE;
    }
    private static class LKOssHolder {
        private static final LKOss INSTANCE = new LKOss();
    }
    public void putObjectSamples(final FileHelper fileHelper, final String uploadObjectDir) {
        File[] allFileName = fileHelper.getAllFileName();
        if (allFileName == null) {
            LogUtils.e(TAG, "allFile is null ");
            return;
        }
        for (File file : allFileName) {
            if (!file.exists()) {
                return;
            }
            if (!file.isFile()) {
                fileHelper.deleteFile(file);
            }
            final String object = uploadObject + uploadObjectDir + "/" + file.getName();
            final String filePath = file.getAbsolutePath();
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    new PutObjectSamples(oss, testBucket, object, filePath)
                            .asyncPutObjectFromLocalFile(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                                @Override
                                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                    LogUtils.d("PutObject", "UploadSuccess");
                                    LogUtils.d("ETag", result.getETag());
                                    LogUtils.d("RequestId", result.getRequestId());
                                    //上传完成删除源文件 如果当前文件没有过期，就不删除
                                    if (!fileHelper.getDefaultFile().equals(filePath)) {
                                        fileHelper.deleteFile(uploadObject);
                                    }
                                }

                                @Override
                                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                    // 请求异常
                                    if (clientExcepion != null) {
                                        // 本地异常如网络异常等
                                        clientExcepion.printStackTrace();
                                    }
                                    if (serviceException != null) {
                                        // 服务异常
                                        LogUtils.e("ErrorCode", serviceException.getErrorCode());
                                        LogUtils.e("RequestId", serviceException.getRequestId());
                                        LogUtils.e("HostId", serviceException.getHostId());
                                        LogUtils.e("RawMessage", serviceException.getRawMessage());
                                    }
                                }
                            });
                }
            });
        }
    }


    public interface DownLoadCallback{
        void onSuccess(String downloadPath);
        void onFailed();
    }


    public void getObjectSamples(final FileHelper fileHelper,
                                 OSS oss,
                                 String bucket,
                                 final String downloadObject, final DownLoadCallback callback) {
        new GetObjectSamples(oss, bucket, downloadObject).asyncGetObjectSample(
                new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                InputStream inputStream = result.getObjectContent();
                String downloadPath = fileHelper.downloadFile(inputStream);

                if (StringUtils.isEmpty(downloadPath)){
                    callback.onFailed();
                } else {
                    callback.onSuccess(downloadPath);
                }

            }
            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtils.e("ErrorCode", serviceException.getErrorCode());
                    LogUtils.e("RequestId", serviceException.getRequestId());
                    LogUtils.e("HostId", serviceException.getHostId());
                    LogUtils.e("RawMessage", serviceException.getRawMessage());
                }

                callback.onFailed();
            }
        });
    }

}
