package com.osacky.hipsterviz.models.track;

public class RealTrackWithOneTag extends RealBaseTrack {
    Tag toptags;
    String tagStrings;

    private class Tag {
        String name;
        String url;
    }

    public String getToptags() {
        if (tagStrings == null) {
            tagStrings = toptags.name;
        }
        return tagStrings;
    }
}
