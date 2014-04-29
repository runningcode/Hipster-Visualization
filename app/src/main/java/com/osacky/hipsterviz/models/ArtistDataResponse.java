package com.osacky.hipsterviz.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArtistDataResponse {

    @SerializedName("artist_id")
    String artistId;
    int sum;
    int hipster;
    int notHipster;
    int unknown;

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

    public static class ArtistList extends ArrayList<ArtistDataResponse> {
    }
}
