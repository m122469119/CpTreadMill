package com.liking.treadmill.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.liking.treadmill.app.LikingThreadMillApplication;
import com.liking.treadmill.widget.IToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import static com.liking.treadmill.app.LikingThreadMillApplication.mLKAppSocketLogQueue;

/**
 * 描述: app安装操作
 *
 * @author 吴传龙
 *         Email:andywuchuanlong@sina.cn
 *         QQ: 3026862225
 * @version 最后修改时间:2015年3月6日 下午3:51:14     修改人:吴传龙
 */
public class ApkController {
    /**
     * 描述: 安装
     * 修改人: 吴传龙
     * 最后修改时间:2015年3月8日 下午9:07:50
     */
    public static boolean install(String apkPath) {
        // 先判断手机是否有root权限
        if (hasRootPerssion()) {
            // 有root权限，利用静默安装实现
            return clientInstall(apkPath);
        } else {
//            // 没有root权限，利用意图进行安装
//            File file = new File(apkPath);
//            if(!file.exists())
//                return false;
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//            context.startActivity(intent);
            IToast.show("无权限安装!!!!!!!!");
            return false;
        }
    }

    /**
     * 描述: 卸载
     * 修改人: 吴传龙
     * 最后修改时间:2015年3月8日 下午9:07:50
     */
    public static boolean uninstall(String packageName, Context context) {
        if (hasRootPerssion()) {
            // 有root权限，利用静默卸载实现
            return clientUninstall(packageName);
        } else {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
            return true;
        }
    }

    /**
     * 判断手机是否有root权限
     */
    private static boolean hasRootPerssion() {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 静默安装
     */
    private static boolean clientInstall(String apkPath) {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 " + apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r " + apkPath);
            PrintWriter.println("reboot"); //重启
//          PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 静默卸载
     */
    private static boolean clientUninstall(String packageName) {
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall " + packageName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 启动app
     * com.exmaple.client/.MainActivity
     * com.exmaple.client/com.exmaple.client.MainActivity
     */
    public static boolean startApp(String packageName, String activityName) {
        boolean isSuccess = false;
        String cmd = "am start -n " + packageName + "/" + activityName + " \n";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return isSuccess;
    }


    private static boolean returnResult(int value) throws IOException {
        // 代表成功  
        if (value == 0) {
//            Intent intent = new Intent();
//            intent.setAction("org.hy.test222");
//            startActivity(intent);
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }

    public static boolean execCommand(String... command) {
        boolean result;
        String message;
        try {
            Process process = new ProcessBuilder().command(command).start();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read;
            InputStream errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            InputStream inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            message = new String(baos.toByteArray());
            inIs.close();
            errIs.close();
            process.destroy();
            result = true;
        } catch (IOException e) {
            message = e.getMessage();
            result = false;
        }
        LogUtils.e("ApkController", " apk :" + message);
        mLKAppSocketLogQueue.put("ApkController", " apk :" + message);
        return result;
    }
}