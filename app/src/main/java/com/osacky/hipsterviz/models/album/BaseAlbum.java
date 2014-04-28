package com.osacky.hipsterviz.models.album;

import com.google.gson.annotations.SerializedName;

public class BaseAlbum {
    @SerializedName("#text")
    String name;
    String mbid;

    public String getName() {
        return name;
    }

    public String getMbid() {
        return mbid;
    }
}
