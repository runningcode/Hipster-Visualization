package com.osacky.hipsterviz.models;

import com.google.gson.annotations.SerializedName;
import com.osacky.hipsterviz.models.track.TrackListTrack;

public class TrackHistoryPage {

    @SerializedName("@attr")
    Attr attr;
    TrackListTrack.BaseTrackList track;

    public Attr getAttr() {
        return attr;
    }

    public TrackListTrack.BaseTrackList getTrack() {
        return track;
    }
}
