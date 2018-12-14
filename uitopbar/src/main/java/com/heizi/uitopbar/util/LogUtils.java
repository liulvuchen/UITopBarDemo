package com.heizi.uitopbar.util;

import android.util.Log;

/**
 * describe 统一Log打印工具类，方便关闭log
 * authors liuyaolin
 * createTime 2017/9/13 14:20
 */
public class LogUtils {

    /**
     * 设置是否打印log
     */
    public static boolean DEBUG = true;

    public static void exception(Exception e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public static void i(String msg, int lastVisibleItemPosition) {
        if (DEBUG) {
            Log.i(getFunctionName(), msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void v(String msg) {
        if (DEBUG) {
            Log.v(getFunctionName(), msg);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    @SuppressWarnings("unused")
    public static void d(String msg) {
        if (DEBUG) {
            Log.d(getFunctionName(), msg);
        }
    }

    @SuppressWarnings("unused")
    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(getFunctionName(), msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    @SuppressWarnings("unused")
    public static void w(String msg) {
        if (DEBUG) {
            Log.w(getFunctionName(), msg);
        }
    }

    @SuppressWarnings("unused")
    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    private static String getFunctionName() {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        return caller.getFileName() + "." + caller.getMethodName() + "(" + caller.getLineNumber() + ")";
    }
}