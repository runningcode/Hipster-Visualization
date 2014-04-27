package com.osacky.hipsterviz.models;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import java.util.ArrayList;

public class Track {

    public static class List extends ArrayList<Track> {
    }

    private static int density;

    FakeArtist artist;
    Album album;
    String name;
    String mbid;
    String url;
    Date date;

    @SerializedName("@attr")
    @Nullable
    IsNowPlaying isNowPlaying;

    @SerializedName("image")
    ArrayList<ImageObject> imageObjects;

    public DateTime getDateTime() {
        if (isNowPlaying != null && isNowPlaying.nowPlaying != null && isNowPlaying.nowPlaying.equals("true")) {
            return new DateTime();
        }
        return date.getDate();
    }

    public FakeArtist getArtist() {
        return artist;
    }

    public Album getAlbum() {
        return album;
    }

    public String getName() {
        return name;
    }

    public String getMbid() {
        return mbid;
    }

    public Uri getUrl() {
        return Uri.parse(url);
    }

    public Uri getImage(@NotNull Context context) {
        if (density == 0) {
            density = context.getResources().getDisplayMetrics().densityDpi;
        }
        if (density >= DisplayMetrics.DENSITY_XXHIGH) {
            return imageObjects.get(imageObjects.size() - 1).getUrl();
        } else if (density >= DisplayMetrics.DENSITY_XHIGH) {
            return imageObjects.get(imageObjects.size() - 2).getUrl();
        } else if (density >= DisplayMetrics.DENSITY_HIGH) {
            return imageObjects.get(imageObjects.size() - 3).getUrl();
        } else {
            return imageObjects.get(0).getUrl();
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    public class ImageObject {
        @SerializedName("#text")
        String url;
        String size;

        public Uri getUrl() {
            return Uri.parse(url);
        }
    }

    public class FakeArtist {
        @SerializedName("#text")
        public String name;
        public String mbid;
    }

    public class IsNowPlaying {
        @SerializedName("nowplaying")
        @Nullable
        String nowPlaying;
    }
}
