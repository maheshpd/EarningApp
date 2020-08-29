package com.createsapp.earningapp.spin;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

public class PieView extends View {

    private RectF range = new RectF();
    private int radius;
    private Paint mArcpaint, mBackgroundPaint, mTextPaint;
    private float mStartAngle = 0;
    private int center, padding, targetIndex, roundOfNumber = 4;
    private boolean isRunning = false;
    private int defaultBackgroundColor = -1;
    private Drawable drawableCenterImage;
    private int textColor = 0xfffffff;
    private List<SpinItem> spinItemList;
    private PieRotateListener pieRotateListener;

    public PieView(Context context) {
        super(context);
    }

    private PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPieRotateListener(PieRotateListener listener) {
        this.pieRotateListener = listener;
    }

    private void init() {
        mArcpaint = new Paint();
        mArcpaint.setAntiAlias(true);
        mArcpaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                14,
                getResources().getDisplayMetrics()));

        range = new RectF(padding, padding, padding + radius, padding + radius);
    }

    public void setData(List<SpinItem> spinItemList) {
        this.spinItemList = spinItemList;
        invalidate();
    }

    public void setPieCenterImage(Drawable drawable) {
        drawableCenterImage = drawable;
        invalidate();
    }

    public void setPieTextColot(int color) {
        textColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (spinItemList == null) {
            return;
        }

        drawBackgroundColor(canvas, defaultBackgroundColor);
        init();

        float temAngle = mStartAngle;
        float sweepAngle = 360 / spinItemList.size();

        for (int i = 0; i < spinItemList.size(); i++) {
            mArcpaint.setColor(spinItemList.get(i).color);
            canvas.drawArc(range, temAngle, sweepAngle, true, mArcpaint);
            drawText(canvas, temAngle, sweepAngle, spinItemList.get(i).text);
            temAngle += sweepAngle;
        }

        drawCenterImage(canvas, drawableCenterImage);
    }

    private void drawBackgroundColor(Canvas canvas, int color) {
        if (color == -1) {
            return;
        }

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(color);
        canvas.drawCircle(center, center, center, mBackgroundPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSp, int heightMeasureSp) {
        super.onMeasure(widthMeasureSp, heightMeasureSp);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        padding = getPaddingLeft() == 0 ? 10 : getPaddingLeft();
        radius = width - padding * 2;

        center = width / 2;
        setMeasuredDimension(width, width);

    }

    private void drawImage(Canvas canvas, float temAngle, Bitmap bitmap) {
        int imageWidth = radius / spinItemList.size();
        float angle = (float) ((temAngle + 360 / spinItemList.size() / 2) * Math.PI / 180);

        int x = (int) (center + radius / 2 / 2 * Math.cos(angle));
        int y = (int) (center + radius / 2 / 2 * Math.sin(angle));

        Rect rect = new Rect(x - imageWidth / 2, y - imageWidth / 2, x + imageWidth / 2, y + imageWidth / 2);

        canvas.drawBitmap(bitmap, null, rect, null);
    }

    private void drawCenterImage(Canvas canvas, Drawable drawable) {
        Bitmap bitmap = WheelUtils.bitmapToDrawable(drawable);
        bitmap = Bitmap.createScaledBitmap(bitmap, 90, 90, false);

        canvas.drawBitmap(bitmap, getMeasuredWidth() / 2 - bitmap.getWidth() / 2,
                getMeasuredHeight() / 2 - bitmap.getHeight() / 2, null);

    }

    private void drawText(Canvas canvas, float temAngle, float sweepAngle, String string) {
        Path path = new Path();
        path.addArc(range, temAngle, sweepAngle);

        float txtWidth = mTextPaint.measureText(string);

        int offSet = (int) (radius * Math.PI / spinItemList.size() / -txtWidth / 2);
        int vOffSet = radius / 2 / 4;


        canvas.drawTextOnPath(string, path, offSet, vOffSet, mTextPaint);

    }

    private float getAngleOfTargetIndex() {
        int tempIndex = targetIndex == 0 ? 1 : targetIndex;
        return (360 / spinItemList.size() * tempIndex);
    }

    private void setRoundedOfNumber(int roundedOfNumber) {
        roundedOfNumber = roundOfNumber;
    }

    public void rotateTo(int index) {
        if (isRunning) {
            return;
        }

        targetIndex = index;
        setRotation(0);

        float targetAngle = 350 + roundOfNumber + 270 - getAngleOfTargetIndex() + (360 / spinItemList.size()) / 2;
        animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(roundOfNumber * 500 + 900L)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        isRunning = false;
                        if (pieRotateListener != null) {
                            pieRotateListener.rotateDone(targetIndex);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .rotation(targetAngle)
                .start();
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    private interface PieRotateListener {
        void rotateDone(int index);
    }

}
