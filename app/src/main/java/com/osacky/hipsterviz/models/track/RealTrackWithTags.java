package com.osacky.hipsterviz.models.track;

import com.osacky.hipsterviz.models.Tag;

import java.io.StringWriter;
import java.util.List;

public class RealTrackWithTags extends RealBaseTrack {

    String tagStrings;
    TopTags toptags;

    public class TopTags {
        List<Tag> tag;
    }

    public String getToptags() {
        if (tagStrings == null) {
            StringWriter stringWriter = new StringWriter();
            for(Tag tag : toptags.tag) {
                stringWriter.append(tag.getName());
                stringWriter.append(", ");
            }
            tagStrings = stringWriter.toString();
        }
        return tagStrings;
    }
}
