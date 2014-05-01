package com.osacky.hipsterviz.models;

import com.google.gson.annotations.SerializedName;
import com.osacky.hipsterviz.models.artist.RealBaseArtist;

import java.util.ArrayList;

public class ArtistDataResponse {

    public static final String HIPSTER = "hipster";
    public static final String MAINSTREAM = "mainstream";
    public static final String NEITHER = "neither";
    public static final String DEFAULT = "n/a";

    @SerializedName("artist_id")
    String artistId;
    int sum;
    int hipster;
    int notHipster;
    int unknown;
    String classification;
    RealBaseArtist realBaseArtist;

    public String getArtistId() {
        return artistId;
    }

    public int getSum() {
        return sum;
    }

    public int getHipster() {
        return hipster;
    }

    public int getNotHipster() {
        return notHipster;
    }

    public int getUnknown() {
        return unknown;
    }

    public String getClassification() {
        return classification;
    }

    public RealBaseArtist getRealBaseArtist() {
        return realBaseArtist;
    }

    public static class ArtistList extends ArrayList<ArtistDataResponse> {
    }
}
