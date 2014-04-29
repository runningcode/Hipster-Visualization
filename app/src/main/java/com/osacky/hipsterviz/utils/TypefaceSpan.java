package com.osacky.hipsterviz.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Style a {@link android.text.Spannable} with a custom {@link android.graphics.Typeface}.
 * <p/>
 * http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
 *
 * @author Tristan Waddington
 */
public class TypefaceSpan extends MetricAffectingSpan {
    public static final int MAX_SIZE = 12;

    /**
     * An <code>LruCache</code> for previously loaded typefaces.
     */
    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<String, Typeface>(MAX_SIZE);

    private Typeface mTypeface;

    /**
     * Load the {@link android.graphics.Typeface} and apply to a {@link android.text.Spannable}.
     */
    public TypefaceSpan(Context context, String typefaceName) {
        mTypeface = sTypefaceCache.get(typefaceName);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getApplicationContext()
                    .getAssets(), String.format("fonts/%s", typefaceName));

            // Cache the loaded Typeface
            sTypefaceCache.put(typefaceName, mTypeface);
        }
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
