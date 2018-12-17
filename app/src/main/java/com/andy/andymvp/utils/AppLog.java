package com.andy.andymvp.utils;

import android.text.TextUtils;
import android.util.Log;

public class AppLog {
    private static final boolean LOG = true;

    public static String logMsg = "stacktraceError";

    public static void i(String tag, String string) {
        if (LOG) Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (LOG && !TextUtils.isEmpty(string)) Log.e(tag, string);
    }

    public static void d(String tag, String string) {
        if (LOG) Log.d(tag, string);
    }

    public static void e(String tag, Exception e) {
        if (LOG) e.printStackTrace();
    }

    public static void v(String tag, String string) {
        if (LOG) Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (LOG) Log.w(tag, string);
    }
}
