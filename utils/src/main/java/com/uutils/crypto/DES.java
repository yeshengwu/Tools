package com.uutils.crypto;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {
    private final static String DES = "DES";
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    private DES() {

    }

    private static byte[] dealKey(byte[] keys) {
        int len = DESKeySpec.DES_KEY_LEN;
        if (keys.length >= len) return keys;
        byte[] key = new byte[len];
        for (int i = 0; i < keys.length; i++) {
            key[i] = keys[i];
        }
        for (int i = keys.length; i < len; i++) {
            key[i] = ' ';
        }
        return key;
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data 内容
     * @param key  加密键byte数组
     * @return 加密内容共
     */
    public static String encrypt(String data, String key) {
        if (data == null)
            return null;
        byte[] keys = key.getBytes();
        byte[] bt = encrypt(data.getBytes(), keys);
        byte[] encodeBase64 = Base64.encode(bt);
        return new String(encodeBase64);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data 内容
     * @param key  加密键byte数组
     * @return 解密内容
     */
    public static String decrypt(String data, String key) {
        if (data == null)
            return null;
        byte[] encodeBase64 = Base64.decode(data.getBytes());
        byte[] keys = key.getBytes();
        byte[] bt = decrypt(encodeBase64, keys);
        return new String(bt);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data 内容
     * @param key  加密键byte数组
     * @return 加密内容
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        // 从原始密钥数据创建DESKeySpec对象
        byte[] result = data;
        try {
            key = dealKey(key);
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, new SecureRandom());
            result = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data 加密内容
     * @param key  加密键byte数组
     * @return 内容
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        // 生成一个可信任的随机数源
        byte[] result = data;
        try {
            key = dealKey(key);
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, new SecureRandom());
            result = cipher.doFinal(data);
        } catch (Exception e) {

        }
        return result;
    }
}
