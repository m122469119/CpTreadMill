package com.liking.treadmill.utils;


import android.util.Base64;

import com.aaron.android.framework.utils.EnvironmentUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by fensan on 2017/7/11.
 */
public class AESUtils {

    public static final String DEFAULT_KEY = EnvironmentUtils.Config.isTestMode() ? "GmZgJ1990H6ds4yo":"Qzk69aGva5hq2sn6";//"2oN6nODf74IKF3JQ:GmZgJ1990H6ds4yo";
    private static final String TAG = "AESUtils";

    public static byte[] encrypt(byte[] data, byte[] key, byte[] ivkey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(ivkey);
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(byte[] data, byte[] key, byte[] ivkey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(ivkey);
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] decrypted = cipher.doFinal(data);
            return new String(decrypted).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String decode(String mima, String aDefault) {

        byte[] decode = Base64.decode(mima, Base64.NO_WRAP);
        byte[] ivkey = new byte[16];
        byte[] newDecode = new byte[decode.length - 16];
        for (int i = 0; i < decode.length; i++) {
            if (i < ivkey.length) {
                ivkey[i] = decode[i];
            } else {
                newDecode[i - ivkey.length] = decode[i];
            }
        }
        return decrypt(newDecode, aDefault.getBytes(), ivkey);

    }


    public static String encode(String data, String aDefault) {
        byte[] ivkey = generateKey();
        if (ivkey == null) {
            return "";
        }
        byte[] encrypt = encrypt(data.getBytes(), aDefault.getBytes(), ivkey);
        if (encrypt == null) {
            return "";
        }
        byte[] all = new byte[ivkey.length + encrypt.length];

        for (int i = 0; i < all.length; i++) {
            if (i < ivkey.length) {
                all[i] = ivkey[i];
            } else {
                all[i] = encrypt[i - ivkey.length];
            }
        }
        return Base64.encodeToString(all, Base64.NO_WRAP).trim();
    }

    public static byte[] generateKey() {
        try {
            KeyGenerator kgen = null;// 创建AES的Key生产者
            kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes()));// 利用用户密码作为随机数初始化出
            // 128位的key生产者
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            return secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;

    }

}
