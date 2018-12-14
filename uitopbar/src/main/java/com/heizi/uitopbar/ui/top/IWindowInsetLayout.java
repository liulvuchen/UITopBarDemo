package com.heizi.uitopbar.ui.top;

import android.graphics.Rect;

/**
 * describe: 适配19及19以上的问题
 * authors: heizi
 * email: 2867058917@qq.com
 * className: IWindowInsetLayout
 * createTime: 2018/11/20 16:59
 */

public interface IWindowInsetLayout {
    boolean applySystemWindowInsets19(Rect insets);

    boolean applySystemWindowInsets21(Object insets);
}
