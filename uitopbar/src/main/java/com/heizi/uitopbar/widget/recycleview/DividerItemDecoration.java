package com.heizi.uitopbar.widget.recycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * describe 通用的RecyclerView LinearLayoutManager的Divider
 * authors lvuchenLiu
 * createTime 2018/1/26 14:23
 */
@SuppressWarnings("ALL")
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    public static final int BOTH = 2;

    private Drawable mDivider;

    private int mWidth;

    private int mHeight;

    private int mOrientation;

    private boolean mIncludeEdge;

//    private int topBottom = getDividerHeight();
//    private int leftRight = getDividerWidth();

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * 自定义dividerDrawable
     */
    public DividerItemDecoration(Context context, int orientation, Drawable dividerDrawable) {
        mDivider = dividerDrawable;
        setOrientation(orientation);
    }

    public DividerItemDecoration(Context context, int orientation, @DrawableRes int dividerRes) {
        mDivider = context.getResources().getDrawable(dividerRes);
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL && orientation != BOTH) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else if (mOrientation == HORIZONTAL) {
            drawHorizontal(c, parent);
        } else {
            drawBoth(c, parent);
        }
    }

    private void drawBoth(Canvas c, RecyclerView parent) {
//        drawVertical(c, parent);
//        drawHorizontal(c, parent);


        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();

            final int left = child.getRight() + params.rightMargin
                    + Math.round(ViewCompat.getTranslationX(child));
            final int right = left + getDividerWidth();

            final int top = child.getBottom() + params.bottomMargin
                    + Math.round(ViewCompat.getTranslationY(child));


            int column = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();

            final int bottom;

            if ((i + 1) % column == 0) {
                bottom = top + getDividerHeight();
            } else {
                bottom = top + getDividerHeight();
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount-1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();

            final int top = child.getBottom() + params.bottomMargin
                    + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + getDividerHeight();



            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount-1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin
                    + Math.round(ViewCompat.getTranslationX(child));

            final int right;
//            final int right = left + getDividerWidth();

            int column = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();

            if ((i + 1) % column == 0) {
                right = left + getDividerHeight();
            } else {
                right = top + getDividerWidth();
            }

//            if ((i + 1) % column == 0) {
//                bottom = top + getDividerHeight();
//            } else {
//                bottom = top + getDividerHeight();
//            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {

            if (parent.getLayoutManager().getPosition(view) == 0 && mIncludeEdge) {
                outRect.set(0, getDividerHeight(), 0, getDividerHeight());
            } else {
                outRect.set(0, 0, 0, getDividerHeight());
            }

        } else if (mOrientation == HORIZONTAL) {

            if (parent.getLayoutManager().getPosition(view) == 0 && mIncludeEdge) {
                outRect.set(getDividerWidth(), 0, getDividerWidth(), 0);
            } else {
                outRect.set(0, 0, getDividerWidth(), 0);
            }

        } else {

            int topBottom = getDividerHeight();
            int leftRight = getDividerWidth();

//            int column = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
//
//            if ((itemPosition + 1) % column == 0) {
//                outRect.set(0, 0, 0, getDividerHeight());
//            } else {
//                outRect.set(0, 0, getDividerWidth(), getDividerHeight());
//            }

            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            // 判断总的数量是否可以整除
            int totalCount = layoutManager.getItemCount();
            int surplusCount = totalCount % layoutManager.getSpanCount();
            int childPosition = parent.getChildAdapterPosition(view);
            if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {//竖直方向的
                if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                    // 后面几项需要bottom
                    outRect.bottom = topBottom;
                } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                    outRect.bottom = topBottom;
                }
                if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要右边
                    outRect.right = leftRight;
                }
                outRect.top = topBottom;
                outRect.left = leftRight;
            } else {
                if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                    // 后面几项需要右边
                    outRect.right = leftRight;
                } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                    outRect.right = leftRight;
                }
                if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要下边
                    outRect.bottom = topBottom;
                }
                outRect.top = topBottom;
                outRect.left = leftRight;
            }
        }
    }

    private int getDividerWidth() {
        return mWidth > 0 ? mWidth : mDivider.getIntrinsicWidth();
    }

    private int getDividerHeight() {
        return mHeight > 0 ? mHeight : mDivider.getIntrinsicHeight();
    }

    public boolean isIncludeEdge() {
        return mIncludeEdge;
    }

    public void setIncludeEdge(boolean includeEdge) {
        mIncludeEdge = includeEdge;
    }
}