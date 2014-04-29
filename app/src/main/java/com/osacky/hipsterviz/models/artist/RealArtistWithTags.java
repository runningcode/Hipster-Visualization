package com.osacky.hipsterviz.models.artist;

import com.osacky.hipsterviz.models.Tag;

import java.io.StringWriter;
import java.util.List;

public class RealArtistWithTags extends RealBaseArtist {
    Tags tags;
    String tagStrings;

    @Override
    public String getTags() {
        if (tagStrings == null) {
            StringWriter stringWriter = new StringWriter();
            for (Tag tag : tags.tag) {
                stringWriter.append(tag.getName());
                stringWriter.append(", ");
            }
            tagStrings = stringWriter.toString();
        }
        return tagStrings;
    }

    class Tags {
        List<Tag> tag;
    }
}
