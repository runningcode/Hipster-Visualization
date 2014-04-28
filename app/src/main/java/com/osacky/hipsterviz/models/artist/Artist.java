package com.osacky.hipsterviz.models.artist;

public class Artist extends FakeArtist {

    String name;
    String url;

    @Override
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
