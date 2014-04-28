package com.osacky.hipsterviz.models.track;

import com.osacky.hipsterviz.models.Tag;

public class RealTrackWithOneTag extends RealBaseTrack {
    Tag toptags;
    String tagStrings;

    public String getToptags() {
        if (tagStrings == null) {
            tagStrings = toptags.getName();
        }
        return tagStrings;
    }
}
