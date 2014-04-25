package com.osacky.hipsterviz.models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class Track {

    public static class List extends ArrayList<Track> {
    }

    FakeArtist artist;
    Album album;
    String name;
    String mbid;
    String url;
    Date date;
    ArrayList<ImageObject> imageObjects;

    public DateTime getDateTime() {
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

    public ArrayList<ImageObject> getImageObjects() {
        return imageObjects;
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
}
