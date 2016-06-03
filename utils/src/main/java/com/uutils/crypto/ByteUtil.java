package com.uutils.crypto;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteUtil {

    private static char[] hexChar = "0123456789abcdef".toCharArray();

    /***
     *
     * @param s 16进制字符
     * @return byte数组
     */
    public static final byte[] hexStrToBytes(String s) {

        byte[] bytes;

        bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }

    /***
     *
     * @param b 数组
     * @return 显示为16进制
     */
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /***
     * byte 数组与 int 的相互转换
     * @param b bytes
     * @return 数
     */
    public static int bytesToInt(byte[] b) {
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }

    /***
     * int转byte
     * @param a 数
     * @return bytes
     */
    public static byte[] intToBytes(int a) {
        return new byte[]{(byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)};
    }

    /***
     * 调整byte的长度为真实内容
     * @param data 数组
     * @return 调整后的数组
     */
    public static byte[] resize(byte[] data) {
        int len = data.length;
        int count = 0;
        for (int i = len-1; i >= 0; i--) {
            if (data[i] <= 0) {
                break;
            }
            count++;
        }
        return Arrays.copyOfRange(data, data.length-count, data.length);
    }

    /***
     * byte 数组与 long 的相互转换
     * @param x long
     * @return bytes
     */
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, x);
        return buffer.array();
    }

    /***
     * byte转long
     * @param bytes 数组
     * @return long
     */
    public static long bytesToLong(byte[] bytes) {
        long l = 0;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.put(bytes, 0, bytes.length);
            buffer.flip();// need flip
            l = buffer.getLong();
        } catch (Exception e) {

        }
        return l;
    }

    /**
     * bytes转string
     * @param data 数组
     * @return 内容
     */
    public static String toString(byte[] data) {
        String str = null;
        try {
            str = new String(data);
        } catch (Exception e) {

        }
        return str;
    }
}
