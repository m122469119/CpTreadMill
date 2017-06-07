package com.liking.treadmill.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.aaron.android.codelibrary.utils.FileUtils;
import com.aaron.android.codelibrary.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created on 2017/06/06
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class ApkFileSVUtils {

    public static long getFileSize(String path){
        return FileUtils.getFileSize(path);
    }

    private static byte[] createChecksum(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);          //<span style="color: rgb(51, 51, 51); font-family: arial; font-size: 13px; line-height: 20px;">将流类型字符串转换为String类型字符串</span>

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5"); //如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
        int numRead;

        do {
            numRead = fis.read(buffer);    //从文件读到buffer，最多装满buffer
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);  //用读到的字节进行MD5的计算，第二个参数是偏移量
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring(1);//加0x100是因为有的b[i]的十六进制只有1位
        }
        return result;
    }

    public static boolean checkPackageSV(Context context, String apkPath) {
        try {
            PackageManager pm = context.getPackageManager();
            //当前apk版本信息
            String currPackageName = context.getPackageName();
            int currVersionCode = pm.getPackageInfo(currPackageName, 0).versionCode;

            LogUtils.i("LikingThreadMillApplication", "curr :" + currPackageName + "---" + currVersionCode);

            PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            ApplicationInfo appInfo;
            if (info != null) {
                appInfo = info.applicationInfo;
                String packageName = appInfo.packageName;
                int versionCode = info.versionCode;
                LogUtils.i("LikingThreadMillApplication", "u盘 :" + packageName + "---" + versionCode);
                return currPackageName.equals(packageName) && versionCode > currVersionCode;
            }
        }catch (Exception e) {
            LogUtils.i("LikingThreadMillApplication", e.getMessage());
        }
        return false;
    }
}
