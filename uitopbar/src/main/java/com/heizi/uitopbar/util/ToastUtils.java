package com.heizi.uitopbar.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.heizi.uitopbar.R;


/**
 * describe Toast 自定义，Application Context 显示Toast
 * authors liuyaolin
 * createTime 2017/10/19 16:55
 */

public class ToastUtils {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private static Toast toast = null;

    private static final Object synObj = new Object();

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    private static void showMessage(final String msg, final int len) {
        new Thread(() -> handler.post(() -> {
            synchronized (synObj) {
                try {
                    if (toast == null) {
                        toast = createToast(sContext);
                    }
                    setToastText(msg);
                    toast.setDuration(len);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })).start();
    }

    private static void showMessage1(final String msg, final int len, final int gravity) {
        new Thread(() -> handler.post(() -> {
            synchronized (synObj) {
                try {
                    if (toast == null) {
                        toast = createToast(sContext);
                    }
                    setToastText(msg);
                    toast.setDuration(len);
                    toast.setGravity(gravity, Gravity.CENTER_HORIZONTAL, 222);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })).start();
    }

    private static void showMessage(@StringRes final int msg, final int len) {
        String toastMsg = sContext.getResources().getString(msg);
        showMessage(toastMsg, len);
    }

    public static void showMessageShort(final String msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    @SuppressWarnings("unused")
    public static void showMessageLong(final String msg) {
        showMessage(msg, Toast.LENGTH_LONG);
    }

    @SuppressWarnings("unused")
    public static void showMessageLong(final String msg, int gravity) {
        showMessage1(msg, Toast.LENGTH_LONG, gravity);
    }

    public static void showMessageShort(final int msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    @SuppressWarnings("unused")
    public static void showMessageLong(final int msg) {
        showMessage(msg, Toast.LENGTH_LONG);
    }

    private static Toast createToast(Context context) {
        Toast toast = new Toast(context.getApplicationContext());
        @SuppressLint("InflateParams")
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_custom_layout, null, false);
        toast.setView(toastView);
        return toast;
    }

    private static void setToastText(String msg) {
        TextView tvToast = toast.getView().findViewById(R.id.tv_toast_text);
        tvToast.setText(msg);
    }

    @SuppressWarnings("unused")
    private static void setToastText(@StringRes int msg) {
        TextView tvToast = toast.getView().findViewById(R.id.tv_toast_text);
        tvToast.setText(msg);
    }

    public static void showError(String response) {
        showMessageShort(response);
    }

}
