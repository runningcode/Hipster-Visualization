package com.osacky.hipsterviz.models.track;

import java.util.List;

public class RealTrackWithTags extends RealBaseTrack {
    TopTags toptags;

    class TopTags {
        List<Tag> tag;

        class Tag {
            String name;
            String url;
        }
    }
}
