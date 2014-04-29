package com.osacky.hipsterviz.models.artist;

public class RealArtistWithNoTags extends RealBaseArtist{
    String tags;

    @Override
    public String getTags() {
        return null;
    }
}
