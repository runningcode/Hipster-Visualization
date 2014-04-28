package com.osacky.hipsterviz.models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class ImageObject {
    @SerializedName("#text")
    String url;
    String size;

    public Uri getUrl() {
        return Uri.parse(url);
    }
}
