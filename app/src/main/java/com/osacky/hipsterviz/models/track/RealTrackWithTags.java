package com.osacky.hipsterviz.models.track;

import java.io.StringWriter;
import java.util.List;

public class RealTrackWithTags extends RealBaseTrack {

    String tagStrings;
    TopTags toptags;

    public class TopTags {
        List<Tag> tag;

        public class Tag {
            String name;
            String url;
        }
    }

    public String getToptags() {
        if (tagStrings == null) {
            StringWriter stringWriter = new StringWriter();
            for(TopTags.Tag tag : toptags.tag) {
                stringWriter.append(tag.name);
                stringWriter.append(", ");
            }
            tagStrings = stringWriter.toString();
        }
        return tagStrings;
    }
}
