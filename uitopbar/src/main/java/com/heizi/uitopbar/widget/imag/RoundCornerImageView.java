package com.heizi.uitopbar.widget.imag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.heizi.uitopbar.R;


/**
 * describe: 圆角ImageView
 * authors: heizi
 * email: 2867058917@qq.com
 * className: RoundCornerImageView
 * createTime: 2018/9/27 17:35
 */
public class RoundCornerImageView extends AppCompatImageView {

    private boolean leftBottom;
    private boolean leftTop;
    private boolean rightBottom;
    private boolean rightTop;

    /**
     * dp
     */
    private float rect_radius;
    private final RectF roundRect = new RectF();
    private final RectF roundRightTopRect = new RectF();
    private final RectF roundRightBottomRect = new RectF();
    private final RectF roundLeftTopRect = new RectF();
    private final RectF roundLeftBottomRect = new RectF();
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();


    public RoundCornerImageView(Context context) {
        this(context, null);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView, 0, 0);
        leftBottom = typedArray.getBoolean(R.styleable.RoundCornerImageView_leftBottom_angle, true);
        leftTop = typedArray.getBoolean(R.styleable.RoundCornerImageView_leftTop_angle, true);
        rightBottom = typedArray.getBoolean(R.styleable.RoundCornerImageView_rightBottom_angle, true);
        rightTop = typedArray.getBoolean(R.styleable.RoundCornerImageView_rightTop_angle, true);
        rect_radius = typedArray.getDimension(R.styleable.RoundCornerImageView_corner_radius, getResources().getDimensionPixelOffset(R.dimen.dp_3));
        init();
    }

    private void init() {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
    }

    /**
     * @param radius dp
     */
    public void setRectRadius(float radius) {
        rect_radius = dip2px(radius);
        invalidate();
    }

    public void setAngle(boolean leftTop, boolean leftBottom, boolean rightTop, boolean rightBottom) {
        this.leftTop = leftTop;
        this.leftBottom = leftBottom;
        this.rightTop = rightTop;
        this.rightBottom = rightBottom;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        roundRect.set(0, 0, w, h);
        roundRightTopRect.set((float) (w * 1.0 / 2.0), 0, w, (float) (h * 1.0 / 2.0));
        roundRightBottomRect.set((float) (w * 1.0 / 2.0), (float) (h * 1.0 / 2.0), w, h);
        roundLeftTopRect.set(0, 0, (float) (w * 1.0 / 2.0), (float) (h * 1.0 / 2.0));
        roundLeftBottomRect.set(0, (float) (h * 1.0 / 2.0), (float) (w * 1.0 / 2.0), h);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, rect_radius, rect_radius, zonePaint);
        if (!leftTop) {// 左上角非圆角
            canvas.drawRect(roundLeftTopRect, zonePaint);
        }
        if (!leftBottom) {
            canvas.drawRect(roundLeftBottomRect, zonePaint);
        }
        if (!rightTop) {
            canvas.drawRect(roundRightTopRect, zonePaint);
        }
        if (!rightBottom) {
            canvas.drawRect(roundRightBottomRect, zonePaint);
        }
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

    public float dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        setImageDrawable(null);
    }

}
