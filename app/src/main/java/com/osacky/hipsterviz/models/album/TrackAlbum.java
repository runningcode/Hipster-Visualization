package com.osacky.hipsterviz.models.album;

import com.osacky.hipsterviz.models.ImageObject;

import java.util.List;

public class TrackAlbum extends BaseAlbum {
    String artist;
    String title;
    List<ImageObject> image;

    @Override
    public String getName() {
        return title;
    }

    public List<ImageObject> getImage() {
        return image;
    }
}
