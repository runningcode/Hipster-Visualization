package com.osacky.hipsterviz.models.artist;

public class TrackArtist extends FakeArtist {

    String name;
    String url;

    @Override
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String getIdentifier() {
        if (name != null && !name.isEmpty()) {
            return name;
        }
        return super.getIdentifier();
    }
}
