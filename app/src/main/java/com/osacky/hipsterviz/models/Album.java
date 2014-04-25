package com.osacky.hipsterviz.models;

import com.google.gson.annotations.SerializedName;

public class Album {
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
