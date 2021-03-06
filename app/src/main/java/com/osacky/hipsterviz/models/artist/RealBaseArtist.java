package com.osacky.hipsterviz.models.artist;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.osacky.hipsterviz.models.ImageObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class RealBaseArtist extends TrackArtist {
    List<ImageObject> image;
    private int density;

    public Uri getImage(@NotNull Context context) {
        if (density == 0) {
            density = context.getResources().getDisplayMetrics().densityDpi;
        }
        if (density >= DisplayMetrics.DENSITY_XXHIGH) {
            return image.get(image.size() - 1).getUrl();
        } else if (density >= DisplayMetrics.DENSITY_XHIGH) {
            return image.get(image.size() - 2).getUrl();
        } else if (density >= DisplayMetrics.DENSITY_HIGH) {
            return image.get(image.size() - 3).getUrl();
        } else {
            return image.get(0).getUrl();
        }
    }

    public abstract String getTags();


}
