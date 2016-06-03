package com.uutils.crypto;


import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private volatile static MD5 sMD5;

    private MD5() {
    }

    private final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};

    public static MD5 getIntance() {
        if (sMD5 == null) {
            synchronized (MD5.class) {
                if (sMD5 == null) {
                    sMD5 = new MD5();
                }
            }
        }
        return sMD5;
    }

    private String toHexString(byte[] bytes) {
        if (bytes == null)
            return "";
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(hexDigits[(b >> 4) & 0x0F]);
            hex.append(hexDigits[b & 0x0F]);
        }
        return hex.toString();
    }

    /***
     * @param file 文件
     * @return md5
     */
    public String md5(File file) {
        if (file == null || !file.exists())
            return "";
        MessageDigest messagedigest = null;
        FileInputStream in = null;
        FileChannel ch = null;
        byte[] encodeBytes = null;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messagedigest.update(byteBuffer);
            encodeBytes = messagedigest.digest();
        } catch (NoSuchAlgorithmException neverHappened) {
            return file.getAbsolutePath();
        } catch (Exception e) {
            return file.getAbsolutePath();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {

                }
            }
            if (ch != null) {
                try {
                    ch.close();
                } catch (Exception e) {

                }
            }
        }

        return toHexString(encodeBytes);
    }

    /***
     * @param string 字符串md5
     * @return md5
     */
    public String md5(String string) {
        byte[] encodeBytes = null;
        try {
            encodeBytes = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException neverHappened) {
            return string;
        } catch (UnsupportedEncodingException neverHappened) {
            return string;
        }

        return toHexString(encodeBytes);
    }
}
