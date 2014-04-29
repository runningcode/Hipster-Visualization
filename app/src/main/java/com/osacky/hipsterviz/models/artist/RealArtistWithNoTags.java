package com.osacky.hipsterviz.models.artist;

public class RealArtistWithNoTags extends RealBaseArtist {
    @Override
    public String getTags() {
        return null;
    }
}
