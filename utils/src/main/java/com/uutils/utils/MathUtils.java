package com.uutils.utils;

import static android.text.TextUtils.isEmpty;

public class MathUtils {
    public static double str2double(String str) {
        if (isEmpty(str))
            return 0d;
        double i = 0d;
        try {
            i = Double.parseDouble(str);
        } catch (NumberFormatException ex) {

        }
        return i;
    }

    public static long str2long(String str) {
        if (isEmpty(str))
            return 0l;
        long i = 0l;
        try {
            if (str.startsWith("0x")) {
                i = Long.parseLong(str, 16);
            } else {
                i = Long.parseLong(str);
            }
        } catch (NumberFormatException ex) {

        }
        return i;
    }
}
