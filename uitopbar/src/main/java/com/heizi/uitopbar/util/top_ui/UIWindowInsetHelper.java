package com.heizi.uitopbar.util.top_ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import com.heizi.uitopbar.ui.top.IWindowInsetLayout;

import java.lang.ref.WeakReference;

/**
 * describe: 视图适配
 * authors: heizi
 * email: 2867058917@qq.com
 * className: UIWindowInsetHelper
 * createTime: 2018/11/20 17:36
 */

public class UIWindowInsetHelper {
    private final int KEYBOARD_HEIGHT_BOUNDARY;
    private final WeakReference<IWindowInsetLayout> mWindowInsetLayoutWR;
    private int sApplySystemWindowInsetsCount = 0;

    public UIWindowInsetHelper(ViewGroup viewGroup, IWindowInsetLayout windowInsetLayout) {
        mWindowInsetLayoutWR = new WeakReference<>(windowInsetLayout);
        KEYBOARD_HEIGHT_BOUNDARY = dp2px(viewGroup.getContext(), 100);

        if (UINotchHelper.isNotchOfficialSupport()) {
            setOnApplyWindowInsetsListener28(viewGroup);
        } else {
            // some rom crash with WindowInsets...
            ViewCompat.setOnApplyWindowInsetsListener(viewGroup,
                    (v, insets) -> {
                        if (Build.VERSION.SDK_INT >= 21 && mWindowInsetLayoutWR.get() != null) {
                            if (mWindowInsetLayoutWR.get().applySystemWindowInsets21(insets)) {
                                return insets.consumeSystemWindowInsets();
                            }
                        }
                        return insets;
                    });
        }
    }

    @TargetApi(28)
    private void setOnApplyWindowInsetsListener28(ViewGroup viewGroup) {
        // WindowInsetsCompat does not exist DisplayCutout stuff...
        viewGroup.setOnApplyWindowInsetsListener((view, windowInsets) -> {
            if (mWindowInsetLayoutWR.get() != null &&
                    mWindowInsetLayoutWR.get().applySystemWindowInsets21(windowInsets)) {
                windowInsets = windowInsets.consumeSystemWindowInsets();
                DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                if (displayCutout != null) {
                    windowInsets = windowInsets.consumeDisplayCutout();
                }
                return windowInsets;
            }
            return windowInsets;
        });
    }

    @SuppressWarnings("deprecation")
    @TargetApi(19)
    public boolean defaultApplySystemWindowInsets19(ViewGroup viewGroup, Rect insets) {
        boolean consumed = false;
        if (insets.bottom >= KEYBOARD_HEIGHT_BOUNDARY) {
           setPaddingBottom(viewGroup, insets.bottom);
            insets.bottom = 0;
        } else {
            setPaddingBottom(viewGroup, 0);
        }

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (jumpDispatch(child)) {
                continue;
            }

            Rect childInsets = new Rect(insets);
            computeInsetsWithGravity(child, childInsets);

            if (!isHandleContainer(child)) {
                child.setPadding(childInsets.left, childInsets.top, childInsets.right, childInsets.bottom);
            } else {
                if (child instanceof IWindowInsetLayout) {
                    boolean output = ((IWindowInsetLayout) child).applySystemWindowInsets19(childInsets);
                    consumed = consumed || output;
                } else {
                    boolean output = defaultApplySystemWindowInsets19((ViewGroup) child, childInsets);
                    consumed = consumed || output;
                }
            }
        }

        return consumed;
    }

    @TargetApi(21)
    public boolean defaultApplySystemWindowInsets21(ViewGroup viewGroup, Object insets) {
        if (UINotchHelper.isNotchOfficialSupport()) {
            return defaultApplySystemWindowInsets(viewGroup, (WindowInsets) insets);
        } else {
            return defaultApplySystemWindowInsetsComapt(viewGroup, (WindowInsetsCompat) insets);
        }
    }

    @TargetApi(21)
    public boolean defaultApplySystemWindowInsetsComapt(ViewGroup viewGroup, WindowInsetsCompat insets) {
        if (!insets.hasSystemWindowInsets()) {
            return false;
        }
        boolean consumed = false;
        boolean showKeyboard = false;
        if (insets.getSystemWindowInsetBottom() >= KEYBOARD_HEIGHT_BOUNDARY) {
            showKeyboard = true;
            UIViewHelper.setPaddingBottom(viewGroup, insets.getSystemWindowInsetBottom());
        } else {
            UIViewHelper.setPaddingBottom(viewGroup, 0);
        }

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);

            if (jumpDispatch(child)) {
                continue;
            }

            int insetLeft =  insets.getSystemWindowInsetLeft();
            int insetRight = insets.getSystemWindowInsetRight();
            if(UINotchHelper.needFixLandscapeNotchAreaFitSystemWindow(viewGroup) &&
                    viewGroup.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                insetLeft = Math.max(insetLeft,UINotchHelper.getSafeInsetLeft(viewGroup));
                insetRight = Math.max(insetRight, UINotchHelper.getSafeInsetRight(viewGroup));
            }

            Rect childInsets = new Rect(
                    insetLeft,
                    insets.getSystemWindowInsetTop(),
                    insetRight,
                    showKeyboard ? 0 : insets.getSystemWindowInsetBottom());

            computeInsetsWithGravity(child, childInsets);
            WindowInsetsCompat windowInsetsCompat = ViewCompat.dispatchApplyWindowInsets(child, insets.replaceSystemWindowInsets(childInsets));
            consumed = consumed || (windowInsetsCompat != null && windowInsetsCompat.isConsumed());
        }

        return consumed;
    }

    @TargetApi(28)
    public boolean defaultApplySystemWindowInsets(ViewGroup viewGroup, WindowInsets insets) {
        sApplySystemWindowInsetsCount++;
        if (UINotchHelper.isNotchOfficialSupport()) {
            if (sApplySystemWindowInsetsCount == 1) {
                // avoid dispatching multiple times
                dispatchNotchInsetChange(viewGroup);
            }
            // always consume display cutout!!
            insets = insets.consumeDisplayCutout();
        }

        boolean consumed = false;
        if (insets.hasSystemWindowInsets()) {
            boolean showKeyboard = false;
            if (insets.getSystemWindowInsetBottom() >= KEYBOARD_HEIGHT_BOUNDARY) {
                showKeyboard = true;
                UIViewHelper.setPaddingBottom(viewGroup, insets.getSystemWindowInsetBottom());
            } else {
                UIViewHelper.setPaddingBottom(viewGroup, 0);
            }
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);

                if (jumpDispatch(child)) {
                    continue;
                }

                Rect childInsets = new Rect(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        showKeyboard ? 0 : insets.getSystemWindowInsetBottom());
                computeInsetsWithGravity(child, childInsets);
                WindowInsets childWindowInsets = insets.replaceSystemWindowInsets(childInsets);
                WindowInsets windowInsets = child.dispatchApplyWindowInsets(childWindowInsets);
                consumed = consumed || windowInsets.isConsumed();
            }
        }
        sApplySystemWindowInsetsCount--;
        return consumed;
    }

    private void dispatchNotchInsetChange(View view) {
        if (view instanceof INotchInsetConsumer) {
            boolean stop = ((INotchInsetConsumer) view).notifyInsetMaybeChanged();
            if (stop) {
                return;
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                dispatchNotchInsetChange(viewGroup.getChildAt(i));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(19)
    public static boolean jumpDispatch(View child) {
        return !child.getFitsSystemWindows() && !isHandleContainer(child);
    }

    public static boolean isHandleContainer(View child) {
        return child instanceof IWindowInsetLayout ||
                child instanceof CoordinatorLayout;
    }

    @SuppressLint("RtlHardcoded")
    private void computeInsetsWithGravity(View view, Rect insets) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int gravity = -1;
        if (lp instanceof FrameLayout.LayoutParams) {
            gravity = ((FrameLayout.LayoutParams) lp).gravity;
        }

        /**
         * 因为该方法执行时机早于 FrameLayout.layoutChildren，
         * 而在 {FrameLayout#layoutChildren} 中当 gravity == -1 时会设置默认值为 Gravity.TOP | Gravity.LEFT，
         * 所以这里也要同样设置
         */
        if (gravity == -1) {
            gravity = Gravity.TOP | Gravity.LEFT;
        }

        if (lp.width != FrameLayout.LayoutParams.MATCH_PARENT) {
            int horizontalGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
            switch (horizontalGravity) {
                case Gravity.LEFT:
                    insets.right = 0;
                    break;
                case Gravity.RIGHT:
                    insets.left = 0;
                    break;
            }
        }

        if (lp.height != FrameLayout.LayoutParams.MATCH_PARENT) {
            int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
            switch (verticalGravity) {
                case Gravity.TOP:
                    insets.bottom = 0;
                    break;
                case Gravity.BOTTOM:
                    insets.top = 0;
                    break;
            }
        }
    }

    /**
     * 单位转换: dp -> px
     *
     * @param dp
     * @return
     */
    private static int dp2px(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5);
    }

    /**
     * 对 View 设置 paddingBottom
     *
     * @param view  需要被设置的 View
     * @param value 设置的值
     */
    private static void setPaddingBottom(View view, int value) {
        if(value != view.getPaddingBottom()){
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), value);
        }
    }
}
