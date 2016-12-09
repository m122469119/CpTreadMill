package com.aaron.android.framework.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Environment utility class
 *
 * @author hu.cao
 * @version 1.0.0
 */
public class EnvironmentUtils {
    private static final String TAG = "EnvironmentUtils";

    private static String mPackageName;

    /**
     * 初始化系统环境
     *
     * @param context 系统环境上下文
     */
    public static void init(Context context, Config.ConfigData configData) {
        if (configData != null) {
            Config.init(configData);
        }
        Network.init(context);
//        GeneralParameters.init(context);
        mPackageName = context.getPackageName();
//        resetAsyncTaskDefaultExecutor();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void resetAsyncTaskDefaultExecutor() {
        try {
            ThreadPoolExecutor.DiscardOldestPolicy discardOldestPolicy = new ThreadPoolExecutor.DiscardOldestPolicy();
            final Field defaultHandler = ThreadPoolExecutor.class.getDeclaredField("defaultHandler");
            defaultHandler.setAccessible(true);
            defaultHandler.set(null, discardOldestPolicy);
            if (SDKVersionUtils.hasHoneycomb()) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) AsyncTask.THREAD_POOL_EXECUTOR;
                threadPoolExecutor.setRejectedExecutionHandler(discardOldestPolicy);
                Method setDefaultExecutorMethod = AsyncTask.class.getMethod("setDefaultExecutor", Executor.class);
                setDefaultExecutorMethod.invoke(null, threadPoolExecutor);
            } else {
                Field sExecutor = AsyncTask.class.getDeclaredField("sExecutor");
                sExecutor.setAccessible(true);
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) sExecutor.get(null);
                threadPoolExecutor.setRejectedExecutionHandler(discardOldestPolicy);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取包名
     *
     * @return 包名
     */
    public static String getPackageName() {
        return mPackageName;
    }

    /**
     * @param packageName 包名
     */
    public static void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    /**
     * @author ran.huang
     * @version 7.0.0
     *          配置信息，来自与assets/config，obtainBuild，channel文件
     */
    public static class Config {
        private static final String TAG = "Config";
        private static String sAppFlag = "";
        private static int sAppVersionCode;
        private static String sAppVersionName = "";
        private static String sHttpRequestUrlHost = "";
        private static boolean sTestMode = false;

        /**
         * 初始化配置文件
         */
        public static void init(ConfigData configData) {
            sAppFlag = configData.getAppFlag();
            sAppVersionCode = configData.getAppVersionCode();
            sAppVersionName = configData.getAppVersionName();
            sTestMode = configData.isTestMode();
            sHttpRequestUrlHost = configData.getUrlHost();
            LogUtils.i(TAG, "AppKey:" + sAppFlag);
            LogUtils.i(TAG, "AppVersionCode:" + sAppVersionCode);
            LogUtils.i(TAG, "AppVersionName:" + sAppVersionName);
            LogUtils.i(TAG, "TestMode:" + sTestMode);
            LogUtils.i(TAG, "HttpRequestUrlHost:" + sHttpRequestUrlHost);
        }


        /**
         * @return 获取应用请求Host
         */

        public static String getHttpRequestUrlHost() {
            return sHttpRequestUrlHost;
        }

        /**
         * 获取程序版本信息
         *
         * @return 程序版本信息
         */
        public static int getAppVersionCode() {
            return sAppVersionCode;
        }

        /**
         * 获取版本类型名称，alpha,beta,release
         *
         * @return 版本类型名称
         */
        public static String getAppVersionName() {
            return sAppVersionName;
        }


        /**
         * 是否让程序在测试模式下运行
         *
         * @return 是否让程序在测试模式下运行
         */
        public static boolean isTestMode() {
            return sTestMode;
        }

        /**
         * 是否处于测试模式，由于gradle的BuildConfig.DEBUG无法正确获取，使用这个函数代替，当解决了这个问题之后则改回BuildConfig.DEBUG
         *
         * @return true/false
         */
        public static boolean isDebugMode() {
            return sTestMode;
        }

        public static String getAppFlag() {
            return sAppFlag;
        }

        public static class ConfigData {
            private String mAppFlag; //应用标识
            private int mAppVersionCode; //应用版本Code,对应AndroidManifest.xml中的versionCode
            private String mAppVersionName; //应用版本名称,对应AndroidManifest.xml中的versionName
            private boolean isTestMode; //是否为测试模式
            private String mUrlHost; //Http请求host url

            public final String getAppFlag() {
                return mAppFlag;
            }

            public final void setAppFlag(String appFlag) {
                mAppFlag = appFlag;
            }

            public final int getAppVersionCode() {
                return mAppVersionCode;
            }

            public final void setAppVersionCode(int appVersionCode) {
                mAppVersionCode = appVersionCode;
            }

            public final String getAppVersionName() {
                return mAppVersionName;
            }

            public final void setAppVersionName(String appVersionName) {
                mAppVersionName = appVersionName;
            }

            public final boolean isTestMode() {
                return isTestMode;
            }

            public final void setIsTestMode(boolean isTestMode) {
                this.isTestMode = isTestMode;
            }

            public final String getUrlHost() {
                return mUrlHost;
            }

            public final void setUrlHost(String urlHost) {
                mUrlHost = urlHost;
            }
        }

    }


    /**
     * 网络信息
     */
    public static class Network {
        /**
         * 无网络
         */
        public static final int NETWORK_INVALID = -1;
        /**
         * 2G网络
         */
        public static final int NETWORK_2G = 0;
        /**
         * wap网络
         */
        public static final int NETWORK_WAP = 1;
        /**
         * wifi网络
         */
        public static final int NETWORK_WIFI = 2;
        /**
         * 3G网络
         */
        public static final int NETWORK_3G = 3;
        /**
         * 4G网络
         */
        public static final int NETWORK_4G = 4;

        private static final int[] NETWORK_MATCH_TABLE = {NETWORK_2G // NETWORK_TYPE_UNKNOWN
                , NETWORK_2G // NETWORK_TYPE_GPRS
                , NETWORK_2G // NETWORK_TYPE_EDGE
                , NETWORK_3G // NETWORK_TYPE_UMTS
                , NETWORK_2G // NETWORK_TYPE_CDMA
                , NETWORK_3G // NETWORK_TYPE_EVDO_O
                , NETWORK_3G // NETWORK_TYPE_EVDO_A
                , NETWORK_2G // NETWORK_TYPE_1xRTT
                , NETWORK_3G // NETWORK_TYPE_HSDPA
                , NETWORK_3G // NETWORK_TYPE_HSUPA
                , NETWORK_3G // NETWORK_TYPE_HSPA
                , NETWORK_2G // NETWORK_TYPE_IDEN
                , NETWORK_3G // NETWORK_TYPE_EVDO_B
                , NETWORK_4G // NETWORK_TYPE_LTE
                , NETWORK_3G // NETWORK_TYPE_EHRPD
                , NETWORK_3G // NETWORK_TYPE_HSPAP
        };

        private static String mIMEI = "";
        private static String mIMSI = "";
        private static String mWifiMac = "";

        private static int mDefaultNetworkType;
        private static ConnectivityManager mConnectManager;

        /**
         * 初始化默认网络参数
         *
         * @param context 上下文环境
         */
        public static void init(Context context) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mIMEI = telephonyManager.getDeviceId();
            if (mIMEI == null) {
                mIMEI = "";
            }

            mIMSI = telephonyManager.getSubscriberId();
            if (mIMSI == null) {
                mIMSI = "";
            }

            try {
                mWifiMac = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mWifiMac == null) {
                mWifiMac = "";
            }

            mDefaultNetworkType = NETWORK_MATCH_TABLE[telephonyNetworkType(context)];
            mConnectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }


        /**
         * 获取IMEI串号
         *
         * @return IMEI串号。<b>有可能为空值</b>
         */
        public static String imei() {
            return mIMEI;
        }

        /**
         * 获取IMSI移动用户识别码
         *
         * @return IMSI移动用户识别码。<b>有可能为空值</b>
         */
        public static String imsi() {
            return mIMSI;
        }

        /**
         * 获取Wifi Mac地址
         *
         * @return Wifi Mac地址
         */
        public static String wifiMac() {
            return mWifiMac;
        }

        /**
         * 获取网络类型
         *
         * @return 网络类型
         */
        public static int type() {
            if (mConnectManager == null) {
                //当还未来得及初始化时，另一线程请求网络时通用参数中取此值时先运行到这儿，那么如不做处理将崩溃
                return NETWORK_WAP;
            }

            int networkType = mDefaultNetworkType;
            NetworkInfo networkInfo = mConnectManager.getActiveNetworkInfo();
            if (!networkConnected(networkInfo)) {
                networkType = NETWORK_INVALID;
            } else if (isWifiNetwork(networkInfo)) {
                networkType = NETWORK_WIFI;
            } else if (isWapNetwork(networkInfo)) {
                networkType = NETWORK_WAP;
            }

            return networkType;
        }

        /**
         * 是否存在有效的网络连接.
         *
         * @return 存在有效的网络连接返回true，否则返回false
         */
        public static boolean isNetWorkAvailable() {
            return mConnectManager != null && networkConnected(mConnectManager.getActiveNetworkInfo());
        }

        /**
         * 获取本机IPv4地址
         *
         * @return 本机IPv4地址
         */
        public static String ipv4() {
            return ipAddress(true);
        }

        /**
         * 获取本机IPv6地址
         *
         * @return 本机IPv6地址
         */
        public static String ipv6() {
            return ipAddress(false);
        }

        private static String ipAddress(boolean useIPv4) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface netInterface = en.nextElement();
                    for (Enumeration<InetAddress> iNetEnum = netInterface.getInetAddresses(); iNetEnum.hasMoreElements(); ) {
                        InetAddress inetAddress = iNetEnum.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            if (useIPv4 && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            } else if (!useIPv4 && inetAddress instanceof Inet6Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        /**
         * 直接从系统函数里得到的network type
         *
         * @param context context
         * @return net type
         */
        private static int telephonyNetworkType(Context context) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = telephonyManager.getNetworkType();
            if (networkType < 0 || networkType >= NETWORK_MATCH_TABLE.length) {
                networkType = TelephonyManager.NETWORK_TYPE_UNKNOWN;
            }
            return networkType;
        }

        private static boolean networkConnected(NetworkInfo networkInfo) {
            return networkInfo != null && networkInfo.isConnected();
        }

        private static boolean isMobileNetwork(NetworkInfo networkInfo) {
            return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }

        @SuppressWarnings("deprecation")
        private static boolean isWapNetwork(NetworkInfo networkInfo) {
            return isMobileNetwork(networkInfo) && !StringUtils.isEmpty(android.net.Proxy.getDefaultHost());
        }

        private static boolean isWifiNetwork(NetworkInfo networkInfo) {
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }

        /**
         * 通过WIFI获取手机ip地址
         *
         * @param context 上下文对象
         * @return
         */
        public static String getLocalIpAddressByWIFI(Context context) {
            // 获取wifi服务
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            // 判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        }

        private static String intToIp(int i) {
            return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                    + "." + (i >> 24 & 0xFF);
        }
    }
}
