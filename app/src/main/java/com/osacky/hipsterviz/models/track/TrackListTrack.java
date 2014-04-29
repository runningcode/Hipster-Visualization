package com.osacky.hipsterviz.models.track;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.google.gson.annotations.SerializedName;
import com.osacky.hipsterviz.models.Date;
import com.osacky.hipsterviz.models.ImageObject;
import com.osacky.hipsterviz.models.album.BaseAlbum;
import com.osacky.hipsterviz.models.artist.FakeArtist;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import java.util.ArrayList;

public class TrackListTrack extends BaseTrack {

    private static int density;
    BaseAlbum album;
    FakeArtist artist;
    ArrayList<ImageObject> image;
    Date date;
    @SerializedName("@attr")
    @Nullable
    IsNowPlaying isNowPlaying;

    @Override
    public FakeArtist getArtist() {
        return artist;
    }

    public DateTime getDateTime() {
        if (isNowPlaying != null && isNowPlaying.nowPlaying != null && isNowPlaying.nowPlaying.equals("true")) {
            return new DateTime();
        }
        return date.getDate();
    }

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

    @Override
    public BaseAlbum getAlbum() {
        return album;
    }

    public static class BaseTrackList extends ArrayList<TrackListTrack> {
    }

    private class IsNowPlaying {
        @SerializedName("nowplaying")
        @Nullable
        String nowPlaying;
    }
}
