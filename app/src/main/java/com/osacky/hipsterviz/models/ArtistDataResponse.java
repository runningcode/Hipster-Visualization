package com.osacky.hipsterviz.models;

import com.google.gson.annotations.SerializedName;

public class ArtistDataResponse {
    @SerializedName("artist_id")
    String artistId;

    int sum;
    int hipster;
    int notHipster;
    int unknown;
}
