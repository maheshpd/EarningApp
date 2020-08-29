package com.createsapp.earningapp.spin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.createsapp.earningapp.R;

import java.util.List;

public class WheelView extends RelativeLayout implements PieView.PieRotateListener {

    private int mBackgroundColor, mTextColor;
    private Drawable mCenterImage, mCursorImage;
    private PieView pieView;
    private ImageView imgCursor;

    private LuckyRoundItemSelectedListener itemSelectedListener;

    public WheelView(Context context) {
        super(context);
        inits(context, null);
    }

    private WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits(context, attrs);
    }

    @Override
    public void rotateDone(int index) {
        if (itemSelectedListener != null) {
            itemSelectedListener.LuckyRoundItemSelected(index);
        }
    }

    private void inits(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
            mBackgroundColor = typedArray.getColor(R.styleable.WheelView_BackgroundColor, 0xffcc0000);
            mTextColor = typedArray.getColor(R.styleable.WheelView_TextColor, 0xffffffff); //8 f

            mCursorImage = typedArray.getDrawable(R.styleable.WheelView_CursorImage);
            mCenterImage = typedArray.getDrawable(R.styleable.WheelView_CenterImage);

            typedArray.recycle();
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.wheel_layout, this, false);

        pieView = (PieView) frameLayout.findViewById(R.id.pieView);
        imgCursor = (ImageView) frameLayout.findViewById(R.id.cursorView);

        pieView.setPieRotateListener(this);
        pieView.setPieBackgroundColor(mBackgroundColor);
        pieView.setPieCenterImage(mCenterImage);
        pieView.setPieTextColot(mTextColor);

        imgCursor.setImageDrawable(mCursorImage);

        addView(frameLayout);
    }

    public void setWheelBackgroundColor(int color) {
        pieView.setPieBackgroundColor(color);
    }

    public void setWheelCursorImage(int drawable) {
        imgCursor.setBackgroundResource(drawable);
    }

    public void setWheelCenterImage(Drawable drawable) {
        pieView.setPieCenterImage(drawable);
    }

    public void setWheelTextColor(int color) {
        pieView.setPieTextColot(color);
    }

    public void setData(List<SpinItem> data) {
        pieView.setData(data);
    }

    public void setRound(int numberOfRound) {

        pieView.setRound(numberOfRound);
    }

    public void startWheelWithTargetIndex(int index) {
        pieView.rotateTo(index);
    }

    public interface LuckyRoundItemSelectedListener {
        void LuckyRoundItemSelected(int index);
    }


}
