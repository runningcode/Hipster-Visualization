package com.osacky.hipsterviz.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackHistoryPage {
    @SerializedName("@attr")
    Attr attr;

    List<Track> track;

    public Attr getAttr() {
        return attr;
    }

    public List<Track> getTrack() {
        return track;
    }
}
