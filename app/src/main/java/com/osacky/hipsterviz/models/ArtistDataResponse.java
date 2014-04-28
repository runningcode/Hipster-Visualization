package com.osacky.hipsterviz.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArtistDataResponse {

    public static class ArtistList extends ArrayList<ArtistDataResponse> {
    }

    @SerializedName("artist_id")
    String artistId;

    int sum;
    int hipster;
    int notHipster;
    int unknown;

    public String getArtistId() {
        return artistId;
    }
}
