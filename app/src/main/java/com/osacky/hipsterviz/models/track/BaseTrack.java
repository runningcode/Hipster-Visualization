package com.osacky.hipsterviz.models.track;

import android.content.Context;
import android.net.Uri;

import com.osacky.hipsterviz.models.album.BaseAlbum;
import com.osacky.hipsterviz.models.artist.FakeArtist;

import org.jetbrains.annotations.NotNull;

abstract class BaseTrack {

    String name;
    String mbid;
    String url;

    public abstract FakeArtist getArtist();

    public abstract Uri getImage(@NotNull Context context);

    public abstract BaseAlbum getAlbum();

    public String getName() {
        return name;
    }

    public String getMbid() {
        return mbid;
    }

    public Uri getUrl() {
        return Uri.parse(url);
    }

    @Override
    public String toString() {
        return getName();
    }
}
