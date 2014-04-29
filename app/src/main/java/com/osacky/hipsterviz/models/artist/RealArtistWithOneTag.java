package com.osacky.hipsterviz.models.artist;

import com.osacky.hipsterviz.models.Tag;

public class RealArtistWithOneTag extends RealBaseArtist {
    Tag tags;

    @Override
    public String getTags() {
        return tags.getName();
    }
}
