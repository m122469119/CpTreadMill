package com.liking.socket.utils;

import com.liking.socket.Constant;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    public static final String DEFAULT_KEY = Constant.KEY_DATA;

    private static byte[] encrypt(byte[] data, byte[] key, byte[] ivkey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivkey);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decrypt(byte[] data, byte[] key, byte[] ivkey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivkey);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decode(byte[] data) {
        byte[] ivKey = new byte[16];
        byte[] srcData = new byte[data.length - 16];
        System.arraycopy(data, 0, ivKey, 0, ivKey.length);
        System.arraycopy(data, ivKey.length, srcData, 0, srcData.length);
        return decrypt(srcData, DEFAULT_KEY.getBytes(), ivKey);

    }

    public static byte[] encode(byte[] data) {
        byte[] ivKey = generateKey();
        if (ivKey == null) {
            return null;
        }
        byte[] encrypt = encrypt(data, DEFAULT_KEY.getBytes(), ivKey);
        if (encrypt == null) {
            return null;
        }
        byte[] result = new byte[ivKey.length + encrypt.length];
        System.arraycopy(ivKey, 0, result, 0, ivKey.length);
        System.arraycopy(encrypt, 0, result, ivKey.length, encrypt.length);
        return result;
    }

    public static byte[] generateKey() {
        try {
            String srcKey = String.valueOf(System.currentTimeMillis());

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new SecureRandom(srcKey.getBytes()));
            SecretKey secretKey = keyGenerator.generateKey(); // 根据用户密码，生成一个密钥
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}