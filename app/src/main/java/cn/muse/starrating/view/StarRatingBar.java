package cn.muse.starrating.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.muse.starrating.R;


/**
 * author: muse
 * created on: 2018/7/18 下午8:08
 * description:
 */

public class StarRatingBar extends View {

    private Drawable defaultStar;
    private Drawable star;
    private int defaultStarColor;
    private int starColor;
    private int starNum;
    private float starStep;
    private int starGap;
    private int starSize;
    private float rating;
    private boolean isIndicator;

    private Paint starPaint;

    private OnStarChangeListener onStarChangeListener;

    public StarRatingBar(Context context) {
        this(context, null);
    }

    public StarRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StarRatingBar);
        defaultStar = typedArray.getDrawable(R.styleable.StarRatingBar_defaultStar);
        star = typedArray.getDrawable(R.styleable.StarRatingBar_star);
        defaultStarColor = typedArray.getColor(R.styleable.StarRatingBar_defaultStarColor, Color.GREEN);
        starColor = typedArray.getColor(R.styleable.StarRatingBar_starColor, Color.YELLOW);
        starNum = typedArray.getInteger(R.styleable.StarRatingBar_starNum, 5);
        starStep = typedArray.getFloat(R.styleable.StarRatingBar_starStep,  0.2f);
        starGap = typedArray.getDimensionPixelOffset(R.styleable.StarRatingBar_starGap, 10);
        starSize = typedArray.getDimensionPixelOffset(R.styleable.StarRatingBar_starSize, 80);
        rating = typedArray.getFloat(R.styleable.StarRatingBar_rating, 1.5f);
        isIndicator = typedArray.getBoolean(R.styleable.StarRatingBar_isIndicator, true);
        typedArray.recycle();

        starPaint = new Paint();
        starPaint.setAntiAlias(true);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = starSize / 2;
        canvas.translate(radius, radius);
        if (defaultStar != null) {
            drawStarDrawable(canvas, defaultStar, starNum);
        } else {
            starPaint.setColor(defaultStarColor);
            int gap = 0;
            for (int i = 0; i < starNum; i++) {
                drawStar(canvas, starPaint, i * starSize + gap, radius);
                gap += starGap;
            }
        }

        // 设置可视区域
        int size = Math.round(rating);
        float decimal = 0;
        // 根据步长获取小数位
        if (size > rating) {
            decimal = (rating - size + 1);
            int rate = (int) (decimal / starStep);
            decimal = rate * starStep;
        }
        int right = (int) (((int) rating) * (starSize + starGap) + decimal * starSize - radius);
        canvas.clipRect(-radius, -radius, right, starSize - radius);
        if (star != null) {
            drawStarDrawable(canvas, star, size);
        } else {
            Paint newPaint = new Paint();
            newPaint.setAntiAlias(true);
            newPaint.setColor(starColor);
            int gap = 0;
            for (int i = 0; i < size; i++) {
                drawStar(canvas, newPaint, i * starSize + gap, radius);
                gap += starGap;
            }
        }
    }

    private void drawStarDrawable(Canvas canvas, Drawable starDrawable, int starNum) {
        Bitmap bitmap = ((BitmapDrawable)starDrawable).getBitmap();
        int gap = 0;
        int radius = starSize / 2;
        for (int i = 0; i < starNum; i++) {
            Rect desRect = new Rect(i * starSize - radius + gap , -radius, (i+1) * starSize - radius + gap, starSize - radius);
            canvas.drawBitmap(bitmap, null, desRect, starPaint);
            gap += starGap;
        }
    }

    private void drawStar(Canvas canvas, Paint paint, int startX, int radius) {
        Point[] points = new Point[5];
        for (int i = 0; i < 5; i++) {
            points[i] = new Point();
            points[i].x = startX + (int) (radius * Math.cos(Math.toRadians(72 * i - 18)));
            points[i].y = (int) (radius * Math.sin(Math.toRadians(72 * i - 18)));
        }
        Path path = new Path();
        path.moveTo(points[0].x, points[0].y);
        int i = 2;
        while (i != 5) {
            if (i >= 5) {
                i %= 5;
            }
            path.lineTo(points[i].x, points[i].y);
            i += 2;
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isIndicator) {
            return super.onTouchEvent(event);
        }

        rating = event.getX() / (starSize + starGap) + 1;
        if (rating <= 1f) {
            rating = 1f;
        }
        if (rating > starNum) {
            rating = starNum;
        }
        invalidate();
        if (onStarChangeListener != null) {
            onStarChangeListener.onStarChange(rating);
        }
        return true;
    }

    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener) {
        this.onStarChangeListener = onStarChangeListener;
    }

    public interface OnStarChangeListener {
        void onStarChange(float rating);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = getPaddingLeft() + getPaddingRight();
            if (starNum > 0) {
                widthSize += starNum * starSize + (starNum - 1) * starGap;
            }
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = getSuggestedMinimumWidth();
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = getPaddingTop() + getPaddingBottom() + starSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = getSuggestedMinimumHeight();
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    public int getDefaultStarColor() {
        return defaultStarColor;
    }

    public void setDefaultStarColor(int defaultStarColor) {
        this.defaultStarColor = defaultStarColor;
    }

    public int getStarColor() {
        return starColor;
    }

    public void setStarColor(int starColor) {
        this.starColor = starColor;
    }

    public int getStarNum() {
        return starNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public float getStarStep() {
        return starStep;
    }

    public void setStarStep(float starStep) {
        this.starStep = starStep;
    }

    public int getStarGap() {
        return starGap;
    }

    public void setStarGap(int starGap) {
        this.starGap = starGap;
    }

    public int getStarSize() {
        return starSize;
    }

    public void setStarSize(int starSize) {
        this.starSize = starSize;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isIndicator() {
        return isIndicator;
    }

    public void setIndicator(boolean indicator) {
        this.isIndicator = indicator;
    }
}
