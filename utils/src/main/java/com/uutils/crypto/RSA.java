package com.uutils.crypto;


import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    private static final String ALGORITHM = "RSA";

    private RSA() {

    }

    /**
     * 私钥加密过程
     *
     * @param privateKey    私钥
     * @param plainTextData 明文数据
     * @return 加密内容
     */
    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) {
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥解密过程
     *
     * @param publicKey  公钥
     * @param cipherData 密文数据
     * @return 明文
     */
    public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) {
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance(ALGORITHM);
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 生成key
     *
     * @param seed 值
     * @return 私钥，公钥
     * @throws NoSuchAlgorithmException 没有提供者
     */
    public static String[] generateKeyPair(String seed) throws NoSuchAlgorithmException {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom random = new SecureRandom();
        random.setSeed(seed.getBytes());
        // 初始加密，512位已被破解，用1024位,最好用2048位
        keygen.initialize(1024, random);
        // 取得密钥对
        KeyPair kp = keygen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
        String privateKeyString = Base64.encodeToString(privateKey.getEncoded());
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        String publicKeyString = Base64.encodeToString(publicKey.getEncoded());
        return new String[]{privateKeyString, publicKeyString};
    }

    /***
     * @param privateKey 16进制字符串
     * @return 私钥
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) {
        byte[] arrayOfByte = Base64.decode(privateKey);
        RSAPrivateKey ak = null;
        PKCS8EncodedKeySpec localX509EncodedKeySpec = new PKCS8EncodedKeySpec(arrayOfByte);
        try {
            ak = (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM).generatePrivate(localX509EncodedKeySpec);
        } catch (InvalidKeySpecException localInvalidKeySpecException) {
            localInvalidKeySpecException.printStackTrace();
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localNoSuchAlgorithmException.printStackTrace();
        }
        return ak;
    }

    /***
     *
     * @param pulicKey 16进制字符串
     * @return 公钥
     */
    public static RSAPublicKey getPublicKey(String pulicKey) {
        byte[] arrayOfByte = Base64.decode(pulicKey);
        RSAPublicKey ak = null;
        X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(arrayOfByte);
        try {
            ak = (RSAPublicKey) KeyFactory.getInstance(ALGORITHM).generatePublic(localX509EncodedKeySpec);
        } catch (InvalidKeySpecException localInvalidKeySpecException) {
            localInvalidKeySpecException.printStackTrace();
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localNoSuchAlgorithmException.printStackTrace();
        }
        return ak;
    }
}