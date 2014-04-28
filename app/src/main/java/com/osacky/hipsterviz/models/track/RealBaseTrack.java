package com.osacky.hipsterviz.models.track;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.osacky.hipsterviz.models.ImageObject;
import com.osacky.hipsterviz.models.album.BaseAlbum;
import com.osacky.hipsterviz.models.album.TrackAlbum;
import com.osacky.hipsterviz.models.artist.FakeArtist;
import com.osacky.hipsterviz.models.artist.TrackArtist;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RealBaseTrack extends BaseTrack {

    private static int density;
    String id;
    String duration;
    String listeners;
    String playCount;
    Wiki wiki;
    TrackAlbum album;
    TrackArtist artist;

    @Override
    public FakeArtist getArtist() {
        return artist;
    }

    public Uri getImage(@NotNull Context context) {
        if(album == null) {
            return null;
        }
        if (density == 0) {
            density = context.getResources().getDisplayMetrics().densityDpi;
        }
        List<ImageObject> image = album.getImage();
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

    public Wiki getWiki() {
        return wiki;
    }

    @Override
    public BaseAlbum getAlbum() {
        return album;
    }

    public class Wiki {
        public String published;
        public String summary;
        public String content;
    }

}
