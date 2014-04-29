package com.osacky.hipsterviz.api.lastFmApi;

import android.util.Log;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.Utils;
import com.osacky.hipsterviz.models.Attr;
import com.osacky.hipsterviz.models.TrackHistoryPage;
import com.osacky.hipsterviz.models.track.TrackListTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntireHistorySpiceRequest extends RetrofitSpiceRequest<EntireHistorySpiceRequest.HistoryMap, LastFmApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "UserHistorySpiceRequest";

    public static class HistoryMap extends HashMap<Long, List<String>>{}

    private final String mUsername;
    private int totalPages = Integer.MAX_VALUE;
    private int currentPage = 0;
    private static final long cacheDuration = DurationInMillis.ALWAYS_RETURNED;

    public EntireHistorySpiceRequest(String username) {
        super(HistoryMap.class, LastFmApi.class);
        mUsername = username;
    }

    public static CachedSpiceRequest<HistoryMap> getCachedSpiceRequest(String username) {
        EntireHistorySpiceRequest historyPageSpiceRequest = new EntireHistorySpiceRequest(username);
        return new CachedSpiceRequest<HistoryMap>(historyPageSpiceRequest, username, cacheDuration);
    }

    @Override
    public HistoryMap loadDataFromNetwork() throws Exception {
        HistoryMap historyMap = new HistoryMap();
        while (currentPage < totalPages) {
            TrackHistoryPage historyPage = getService().getRecentTracks(mUsername, currentPage + 1);
            Log.i(TAG, "entire history totalPages " + totalPages + " currentPage " + currentPage);
            fillMap(historyMap, historyPage);
        }
        return historyMap;
    }

    private void fillMap(HistoryMap historyMap, TrackHistoryPage historyPage) {
        final Attr attr = historyPage.getAttr();
        totalPages = attr.getTotalPages();
        currentPage = attr.getPage();
        final float progress = attr.getProgress();
        publishProgress(progress);

        for (TrackListTrack track : historyPage.getTrack()) {
            long key = Utils.roundDays(track.getDateTime()).getMillis();
            String identifier = track.getArtist().getIdentifier();
            List<String> identifiers = historyMap.get(key);
            if (identifiers == null) {
                identifiers = new ArrayList<String>();
                historyMap.put(key, identifiers);
            }
            identifiers.add(identifier);
        }
    }
}
