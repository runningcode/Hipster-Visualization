package com.osacky.hipsterviz.api.lastFmApi;

import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.TrackHistoryPage;

public class HistoryPageSpiceRequest extends RetrofitSpiceRequest<TrackHistoryPage, LastFmApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "HistoryPageSpiceRequest";

    private final String mUsername;
    private final int mPage;

    public HistoryPageSpiceRequest(String username, int page) {
        super(TrackHistoryPage.class, LastFmApi.class);
        mUsername = username;
        mPage = page;
    }

    public static CachedSpiceRequest<TrackHistoryPage> getCachedSpiceRequest(String username, int page, long cacheDuration) {
        HistoryPageSpiceRequest historyPageSpiceRequest = new HistoryPageSpiceRequest(username, page);
        return new CachedSpiceRequest<>(historyPageSpiceRequest, username + page, cacheDuration);
    }

    @Override
    public TrackHistoryPage loadDataFromNetwork() throws Exception {
        return getService().getRecentTracks(mUsername, mPage);
    }
}
