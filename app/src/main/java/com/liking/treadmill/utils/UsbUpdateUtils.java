package com.liking.treadmill.utils;

import android.content.Context;

import com.aaron.android.codelibrary.utils.FileUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.liking.treadmill.app.ThreadMillConstant;
import com.liking.treadmill.widget.IToast;

/**
 * Created on 2017/06/07
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class UsbUpdateUtils {

    public static void checkUSBUpdate(Context context) {
        LogUtils.i("LikingThreadMillApplication", "检测U盘：" + FileUtils.exists(ThreadMillConstant.USB_FILE_PATH_APK));
        if(FileUtils.exists(ThreadMillConstant.USB_FILE_PATH_APK)
                && ApkFileSVUtils.checkPackageSV(context, ThreadMillConstant.USB_FILE_PATH_APK)) {
            //IToast.show("检测到U盘，更新中...");
            LogUtils.i("LikingThreadMillApplication", "检测到U盘，更新中...");
            if(!ApkController.execCommand("pm", "install", "-r", ThreadMillConstant.USB_FILE_PATH_APK)){
                IToast.show("更新失败...");
                LogUtils.i("LikingThreadMillApplication", "更新失败...");
            }
        } else {
            IToast.show("无更新...");
            LogUtils.i("LikingThreadMillApplication", "无更新...");
        }
    }

    public static boolean isNeedUSBUpdate() {
        return FileUtils.exists(ThreadMillConstant.USB_FILE_PATH_APK);
    }
}
