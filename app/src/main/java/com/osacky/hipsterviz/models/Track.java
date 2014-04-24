package com.osacky.hipsterviz.models;

import java.util.ArrayList;

public class Track {

    public static class List extends ArrayList<Track> {
    }

    Artist artist;
    String name;
    String mbid;
    String url;
    Date date;

    public Date getDate() {
        return date;
    }

    public Artist getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public String getMbid() {
        return mbid;
    }

    public String getUrl() {
        return url;
    }


}
