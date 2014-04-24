package com.osacky.hipsterviz.models;

import com.google.gson.annotations.SerializedName;

public class TrackHistoryPage {

    @SerializedName("@attr")
    Attr attr;
    Track.List track;

    public Attr getAttr() {
        return attr;
    }

    public Track.List getTrack() {
        return track;
    }
}
