package com.osacky.hipsterviz;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringUtil;
import com.osacky.hipsterviz.utils.Utils;

import org.androidannotations.annotations.EViewGroup;

@EViewGroup(R.layout.hip_button)
public class SpringyButton extends FrameLayout implements SpringListener {

    private Spring animSpring = Utils.springSystem.createSpring()
            .setSpringConfig(Utils.ORIGAMI_SPRING_CONFIG)
            .addListener(this);
    private Spring buttonSpring = Utils.springSystem.createSpring()
            .setSpringConfig(Utils.BUTTON_SPRING_CONFIG)
            .addListener(new TouchListener());

    private final float mStartX;
    private final float mStartY;
    private final String text;

    public SpringyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SpringyButton);

        assert a != null;
        mStartX = a.getDimension(R.styleable.SpringyButton_startX, 0);
        mStartY = a.getDimension(R.styleable.SpringyButton_startY, 0);
        text = a.getString(R.styleable.SpringyButton_text);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ((TextView) findViewById(R.id.circle_button_text)).setText(text);
        setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        setVisibility(View.VISIBLE);
        double value = spring.getCurrentValue();

        float selectedTitleScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0,
                1);
        setScaleX(selectedTitleScale);
        setScaleY(selectedTitleScale);

        float titleTranslateX = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1,
                mStartX, 0);
        float titleTranslateY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1,
                mStartY, 0);
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

    public void setAnimValue(double endValue) {
        animSpring.setEndValue(endValue);
    }

    public void setTouchValue(double endValue) {
        buttonSpring.setEndValue(endValue);
    }

    private class TouchListener implements SpringListener {

        @Override
        public void onSpringUpdate(Spring spring) {
            float value = (float) spring.getCurrentValue();
            float scale = 1f - (value * 0.2f);
            setScaleX(scale);
            setScaleY(scale);
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
    }
}
