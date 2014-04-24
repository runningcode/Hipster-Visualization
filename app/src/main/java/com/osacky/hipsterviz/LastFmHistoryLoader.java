package com.osacky.hipsterviz;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.osacky.hipsterviz.api.ApiAdapter;
import com.osacky.hipsterviz.api.LastFmApi;
import com.osacky.hipsterviz.models.Track;
import com.osacky.hipsterviz.models.TrackHistoryPage;

import java.util.ArrayList;
import java.util.List;

public class LastFmHistoryLoader extends AsyncTaskLoader<List<Track>>{

    String mUsername;

    public LastFmHistoryLoader(Context context, String username) {
        super(context);
        mUsername = username;

    }

    @Override
    public List<Track> loadInBackground() {
        final LastFmApi lastFmApi = ApiAdapter.getLastFmApi();
        final List<Track> trackList = new ArrayList<Track>();
        final TrackHistoryPage firstPage = lastFmApi.getRecentTracks(mUsername, 1); // 1 indexed lol
        trackList.addAll(firstPage.getTrack());

        int totalPages = firstPage.getAttr().getTotalPages();

        // first page was already added, so we start at page 2
        for (int i = 2; i < totalPages; i ++) {
            final TrackHistoryPage page = lastFmApi.getRecentTracks(mUsername, i);
            trackList.addAll(page.getTrack());
        }

        return trackList;
    }
}
