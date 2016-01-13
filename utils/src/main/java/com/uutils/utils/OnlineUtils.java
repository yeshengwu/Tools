/**
 *
 */
package com.uutils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public class OnlineUtils {
     //region network
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = cm.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context == null) return false;
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();// wifi
        return info != null && info.isAvailable() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isMobileConnected(Context context) {
        if (context == null) return false;
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public static String getIp(Context context) {
        if (isWifiConnected(context)) {
            return getWifiIpAddress(context);
        }
        return getGPRSIpAddress();
    }

    public static String getGPRSIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMacAddress(Context context) {
        String address;
        address = loadAddress("wlan0");
        if (TextUtils.isEmpty(address)) {
            address = loadAddress("eth0");
        }
        if (TextUtils.isEmpty(address)) {
            try {
                WifiManager wifiManager = (WifiManager)
                        context.getSystemService(Context.WIFI_SERVICE);
                address = wifiManager.getConnectionInfo().getMacAddress();
            } catch (Exception e) {
            /* no-op */
            }
        }
        if (TextUtils.isEmpty(address)) {
            return address;
        }
        return address.toUpperCase(Locale.US).replaceAll("\\s", "");
    }

    private static String loadAddress(final String interfaceName) {
        try {
            final String filePath = "/sys/class/net/" + interfaceName + "/address";
            final StringBuilder fileData = new StringBuilder(1000);
            final BufferedReader reader = new BufferedReader(new FileReader(filePath), 1024);
            final char[] buf = new char[1024];
            int numRead;

            String readData;
            while ((numRead = reader.read(buf)) != -1) {
                readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }

            reader.close();
            return fileData.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public static String getWifiIpAddress(Context context) {
        String ip = null;
        try {
            final WifiManager wifimanage = (WifiManager)
                    context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiinfo = wifimanage.getConnectionInfo();
            int i = wifiinfo.getIpAddress();
            ip = (i & 0xFF) + "." + ((i >> 8) & 0xFF)
                    + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return ip;
    }
    //endregion

    //region http

    /***
     * @param url
     * @param ispost
     * @param timeout
     * @param propertys 头
     * @param datas     body
     * @return
     * @throws IOException
     */
    public static HttpURLConnection connect(
            String url, boolean ispost, int timeout,
            Map<String, String> datas, Map<String, String> propertys)
            throws IOException {
        URL _url = new URL(url);
        HttpURLConnection url_con = (HttpURLConnection) _url.openConnection();
        if (timeout > 0) {
            url_con.setConnectTimeout(timeout);
            url_con.setReadTimeout(timeout);
        }
        url_con.setRequestProperty("User-agent", System.getProperty("http.agent"));
        if (propertys != null) {
            for (Map.Entry<String, String> entry : propertys.entrySet()) {
                url_con.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        // 设置session
        if (ispost) {
            // 输入参数
            url_con.setRequestMethod("POST");
            String body = UriUtils.toString(datas);
            if (body != null && body.length() > 0) {
                url_con.setDoOutput(true);
                url_con.getOutputStream().write(body.getBytes());
            }
        } else {
            url_con.setRequestMethod("GET");
        }
        return url_con;
    }

    private static byte[] getContent(String url, boolean ispost, int timeout, Map<String, String> datas) {
        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        byte[] result = null;
        HttpURLConnection conn = null;
        try {
            conn = connect(url, ispost, 60 * 1000, datas, null);
            outputStream = new ByteArrayOutputStream();
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_PARTIAL) {
                inputStream = conn.getInputStream();
                byte[] data = new byte[4096];
                int len = 0;
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                result = outputStream.toByteArray();
            } else if (code < HttpURLConnection.HTTP_OK) {
                Logs.w("other:" + code);
            } else {
                Logs.w("err:" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.close(inputStream);
            FileUtils.close(outputStream);
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    public static byte[] get(String url) {
        return getContent(url, false, 60 * 1000, null);
    }

    // 使用POST方法提交到后台
    public static byte[] post(String url, Map<String, String> data) {
        return getContent(url, true, 60 * 1000, data);
    }
    //endregion
}
