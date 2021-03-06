package com.osacky.hipsterviz.views;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringUtil;
import com.facebook.rebound.ui.Util;
import com.osacky.hipsterviz.utils.Utils;

public class SpringyFontFitTextView extends FontFitTextView implements SpringListener {

    private Spring titleSpring = Utils.springSystem.createSpring()
            .setSpringConfig(Utils.ORIGAMI_SPRING_CONFIG)
            .addListener(this);

    @SuppressWarnings("unused")
    public SpringyFontFitTextView(Context context) {
        super(context);
    }

    @SuppressWarnings("unused")
    public SpringyFontFitTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        Resources resources = getResources();
        double value = spring.getCurrentValue();

        float selectedTitleScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0,
                1);
        float titleTranslateY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1,
                Util.dpToPx(-150f, resources), 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setScaleX(selectedTitleScale);
            setScaleY(selectedTitleScale);
            setTranslationY(titleTranslateY);
        }
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

    public void setEndValue(double value) {
        titleSpring.setEndValue(value);
    }
}
