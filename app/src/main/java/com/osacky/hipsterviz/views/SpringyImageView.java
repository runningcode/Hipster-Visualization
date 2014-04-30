package com.osacky.hipsterviz.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringUtil;
import com.osacky.hipsterviz.R;
import com.osacky.hipsterviz.utils.Utils;

public class SpringyImageView extends ImageView implements SpringListener {

    private Spring animSpring = Utils.springSystem.createSpring()
            .setSpringConfig(Utils.ORIGAMI_SPRING_CONFIG)
            .addListener(this);

    private final float mStartX;
    private final float mStartY;

    public SpringyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SpringyButton);

        assert a != null;
        mStartX = a.getDimension(R.styleable.SpringyButton_startX, 0);
        mStartY = a.getDimension(R.styleable.SpringyButton_startY, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        setVisibility(View.VISIBLE);
        double value = spring.getCurrentValue();

        float selectedImage = (float) SpringUtil.mapValueFromRangeToRange(
                value, 0, 1, 0, 1);
        float titleTranslateX = (float) SpringUtil.mapValueFromRangeToRange(
                value, 0, 1, mStartX, 0);
        float titleTranslateY = (float) SpringUtil.mapValueFromRangeToRange(
                value, 0, 1, mStartY, 0);

        setScaleX(selectedImage);
        setScaleY(selectedImage);
        setTranslationX(titleTranslateX);
        setTranslationY(titleTranslateY);
    }

    @Override
    public void onSpringAtRest(Spring spring) {
    }

    @Override
    public void onSpringActivate(Spring spring) {
    }

    @Override
    public void onSpringEndStateChange(Spring spring) {
    }

    public void setEndValue(double endValue) {
        animSpring.setEndValue(endValue);
    }
}
