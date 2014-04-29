package com.osacky.hipsterviz;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;


// original idea here :
// http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
public class RoundedTransformation implements com.squareup.picasso.Transformation {
    private final int radius;
    private final int margin;  // dp
    private final int borderColor;
    private final int borderWidth;

    /**
     * Constructor
     *
     * @param radius      corner radius (if you want a circle, make use image width/2
     * @param margin      margin?
     * @param borderColor if you don't want a border, pass borderColor = 0
     */
    public RoundedTransformation(
            final int radius, final int margin, int borderColor, int borderWidth) {
        this.radius = radius;
        this.margin = margin;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        // draw the border first, if necessary
        if (borderWidth > 0) {
            final Paint border = new Paint();
            border.setAntiAlias(true);
            border.setColor(borderColor);
            canvas.drawRoundRect(new RectF(
                            margin,
                            margin,
                            source.getWidth() - margin,
                            source.getHeight() - margin
                    ),
                    radius, radius, border
            );
        }
        // now draw the circular image on top
        canvas.drawRoundRect(
                new RectF(
                        margin + borderWidth,
                        margin + borderWidth,
                        source.getWidth() - margin - borderWidth,
                        source.getHeight() - margin - borderWidth
                ),
                radius - borderWidth, radius - borderWidth, paint
        );

        if (source != output) {
            source.recycle();
        }

        return output;
    }

    @Override
    public String key() {
        return "rounded";
    }
}
