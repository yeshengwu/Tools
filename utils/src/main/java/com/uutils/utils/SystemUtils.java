package com.uutils.utils;

import android.app.Service;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.Locale;

/**
 * Created by Administrator on 2015/12/17.
 */
public class SystemUtils {
    // 判断手机是否有手机卡
    public static String getSimCountryIso(Context context) {
        if (context == null) {
            return "86";
        }
        String iso = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (hasSIM(context)) {
            iso = tm.getNetworkCountryIso();
        }
        return iso;
    }

    public static String getLanguage() {
        Locale l = Locale.getDefault();
        // language = l.getLanguage();
        String language = l.toString();
        return language == null ? "en-US" : language;
    }

    public static boolean hasSIM(Context context) {
        if (context == null) {
            return false;
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        int state = tm.getSimState();
        switch (state) {
            case TelephonyManager.SIM_STATE_READY:// 准备
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:// PIN解锁
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:// PUK解锁
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED://
                return true;
            case TelephonyManager.SIM_STATE_UNKNOWN:// 未知
            case TelephonyManager.SIM_STATE_ABSENT:// 未插卡
                return false;
            default:
                // 无效
                break;
        }
        return false;
    }

    public static String getAndroidId(Context context) {
        String sAndroidId = null;
        try {
            sAndroidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {

        }
        return sAndroidId;
    }

    public static String getSIMName(Context context) {
        String simname = null;
        try {
            TelephonyManager tm = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            // 运营商
            simname = tm.getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simname;
    }
}
