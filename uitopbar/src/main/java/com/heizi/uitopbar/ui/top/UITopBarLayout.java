package com.heizi.uitopbar.ui.top;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heizi.uitopbar.R;
import com.heizi.uitopbar.util.top_ui.UIDisplayHelper;
import com.heizi.uitopbar.util.top_ui.UIDrawableHelper;

/**
 * describe: 这是一个对 {@link UITopBar} 的代理类，需要它的原因是：
 * 我们用 fitSystemWindows 实现沉浸式状态栏后，需要将 {@link UITopBar} 的背景衍生到状态栏后面，
 * 这个时候 fitSystemWindows 是通过更改 padding 实现的，而 {@link UITopBar} 是在高度固定的前提下做各种行为的，
 * 例如按钮的垂直居中，因此我们需要在外面包裹一层并消耗 padding
 * 这个类一般是配合 {@link UIWindowInsetLayout} 使用，并需要设置 fitSystemWindows 为 true
 * authors: heizi
 * email: 2867058917@qq.com
 * className: UITopBarLayout
 * createTime: 2018/11/20 17:34
 */

public class UITopBarLayout extends ConstraintLayout {
    private UITopBar mTopBar;
    private Drawable mTopBarBgWithSeparatorDrawableCache;

    private int mTopBarSeparatorColor;
    private int mTopBarBgColor;
    private int mTopBarSeparatorHeight;

    public UITopBarLayout(Context context) {
        this(context, null);
    }

    public UITopBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UITopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.UITopBar, 0, 0);
        mTopBarSeparatorColor = array.getColor(R.styleable.UITopBar_ui_topbar_separator_color,
                ContextCompat.getColor(context, R.color.c_fff));
        mTopBarSeparatorHeight = array.getDimensionPixelSize(R.styleable.UITopBar_ui_topbar_separator_height, UIDisplayHelper.dp2px(context, 44));
        mTopBarBgColor = array.getColor(R.styleable.UITopBar_ui_topbar_bg_color, Color.WHITE);
        boolean hasSeparator = array.getBoolean(R.styleable.UITopBar_ui_topbar_need_separator, true);

        // 构造一个透明的背景且无分隔线的TopBar，背景与分隔线有UITopBarLayout控制
        mTopBar = new UITopBar(context, true);
        mTopBar.getCommonFieldFormTypedArray(context, array);
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                mTopBarSeparatorHeight);
        addView(mTopBar, lp);

        array.recycle();

        setBackgroundDividerEnabled(hasSeparator);
    }

    public void setCenterView(View view) {
        mTopBar.setCenterView(view);
    }

    public TextView setTitle(int resId) {
        return mTopBar.setTitle(resId);
    }

    public TextView setTitle(String title) {
        return mTopBar.setTitle(title);
    }

    public TextView setEmojiTitle(String title) {
        return mTopBar.setEmojiTitle(title);
    }

    public void showTitlteView(boolean toShow) {
        mTopBar.showTitleView(toShow);
    }

    public void setSubTitle(int resId) {
        mTopBar.setSubTitle(resId);
    }

    public void setSubTitle(String subTitle) {
        mTopBar.setSubTitle(subTitle);
    }

    public void setTitleGravity(int gravity) {
        mTopBar.setTitleGravity(gravity);
    }

    public void addLeftView(View view, int viewId) {
        mTopBar.addLeftView(view, viewId);
    }

    public void addLeftView(View view, int viewId, RelativeLayout.LayoutParams layoutParams) {
        mTopBar.addLeftView(view, viewId, layoutParams);
    }

    public void addRightView(View view, int viewId) {
        mTopBar.addRightView(view, viewId);
    }

    public void addRightView(View view, int viewId, RelativeLayout.LayoutParams layoutParams) {
        mTopBar.addRightView(view, viewId, layoutParams);
    }

    public AppCompatImageButton addRightImageButton(int drawableResId, int viewId) {
        return mTopBar.addRightImageButton(drawableResId, viewId);
    }

    public AppCompatImageButton addLeftImageButton(int drawableResId, int viewId) {
        return mTopBar.addLeftImageButton(drawableResId, viewId);
    }

    public Button addLeftTextButton(int stringResId, int viewId) {
        return mTopBar.addLeftTextButton(stringResId, viewId);
    }

    public Button addLeftTextButton(String buttonText, int viewId) {
        return mTopBar.addLeftTextButton(buttonText, viewId);
    }

    public Button addRightTextButton(int stringResId, int viewId) {
        return mTopBar.addRightTextButton(stringResId, viewId);
    }

    public Button addRightTextButton(String buttonText, int viewId) {
        return mTopBar.addRightTextButton(buttonText, viewId);
    }

    public AppCompatImageButton addLeftBackImageButton() {
        return mTopBar.addLeftBackImageButton();
    }

    public void removeAllLeftViews() {
        mTopBar.removeAllLeftViews();
    }

    public void removeAllRightViews() {
        mTopBar.removeAllRightViews();
    }

    public void removeCenterViewAndTitleView() {
        mTopBar.removeCenterViewAndTitleView();
    }

    /**
     * 设置 TopBar 背景的透明度
     *
     * @param alpha 取值范围：[0, 255]，255表示不透明
     */
    public void setBackgroundAlpha(int alpha) {
        this.getBackground().setAlpha(alpha);
    }

    /**
     * 根据当前 offset、透明度变化的初始 offset 和目标 offset，计算并设置 Topbar 的透明度
     *
     * @param currentOffset     当前 offset
     * @param alphaBeginOffset  透明度开始变化的offset，即当 currentOffset == alphaBeginOffset 时，透明度为0
     * @param alphaTargetOffset 透明度变化的目标offset，即当 currentOffset == alphaTargetOffset 时，透明度为1
     */
    public int computeAndSetBackgroundAlpha(int currentOffset, int alphaBeginOffset, int alphaTargetOffset) {
        double alpha = (float) (currentOffset - alphaBeginOffset) / (alphaTargetOffset - alphaBeginOffset);
        alpha = Math.max(0, Math.min(alpha, 1)); // from 0 to 1
        int alphaInt = (int) (alpha * 255);
        this.setBackgroundAlpha(alphaInt);
        return alphaInt;
    }

    /**
     * 设置是否要 Topbar 底部的分割线
     *
     * @param enabled true 为显示底部分割线，false 则不显示
     */
    public void setBackgroundDividerEnabled(boolean enabled) {
        if (enabled) {
            if (mTopBarBgWithSeparatorDrawableCache == null) {
                mTopBarBgWithSeparatorDrawableCache = UIDrawableHelper.
                        createItemSeparatorBg(mTopBarSeparatorColor, mTopBarBgColor, mTopBarSeparatorHeight, false);
            }
           setBackgroundKeepingPadding(this, mTopBarBgWithSeparatorDrawableCache);
        } else {
            setBackgroundColorKeepPadding(this, mTopBarBgColor);
        }
    }

    private static int getAttrDimen(Context context, int attrRes){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, typedValue, true);
        return TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
    }

    public static void setBackgroundKeepingPadding(View view, Drawable drawable) {
        int[] padding = new int[]{view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        setBackground(view, drawable);
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    public static void setBackgroundColorKeepPadding(View view, @ColorInt int color) {
        int[] padding = new int[]{view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        view.setBackgroundColor(color);
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackground(View view, Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

}
