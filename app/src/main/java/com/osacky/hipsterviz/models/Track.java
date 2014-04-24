package com.osacky.hipsterviz.models;

public class Track {
    Artist artist;
    String name;
    String mbid;
    String url;

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

    Date date;

}
