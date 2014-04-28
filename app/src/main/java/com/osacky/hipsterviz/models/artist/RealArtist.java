package com.osacky.hipsterviz.models.artist;

import com.osacky.hipsterviz.models.ImageObject;
import com.osacky.hipsterviz.models.Tag;

import java.util.List;

public class RealArtist extends TrackArtist {
    List<ImageObject> image;
    Tags tags;

    public List<ImageObject> getImage() {
        return image;
    }

    public List<Tag> getTags() {
        return tags.tag;
    }

    class Tags {
        List<Tag> tag;
    }
}
