package com.osacky.hipsterviz;

import android.util.Log;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.api.LastFmApi;
import com.osacky.hipsterviz.models.Track;
import com.osacky.hipsterviz.models.TrackHistoryPage;

public class UserHistorySpiceRequest extends RetrofitSpiceRequest<Track.List, LastFmApi> {

    private static final String TAG = "UserHistorySpiceRequest";
    private final String mUsername;

    public UserHistorySpiceRequest(String username) {
        super(Track.List.class, LastFmApi.class);
        mUsername = username;
    }

    @Override
    public Track.List loadDataFromNetwork() throws Exception {
        final LastFmApi lastFmApi = getService();
        final Track.List trackList = new Track.List();
        TrackHistoryPage page = lastFmApi.getRecentTracks(mUsername, 1); // 1 indexed lol
        Log.i(TAG, "name is " + page.getAttr().getUser());
        Log.i(TAG, "track is " + page.getTrack().get(0).getName());
        trackList.addAll(page.getTrack());

        int totalPages = page.getAttr().getTotalPages();
        publishProgress(1/totalPages);

        // first page was already added, so we start at page 2
        for (int i = 2; i < totalPages; i ++) {
            page = lastFmApi.getRecentTracks(mUsername, i);
            trackList.addAll(page.getTrack());
            publishProgress(i/totalPages);
        }
        publishProgress(1.0f);

        return trackList;
    }
}
