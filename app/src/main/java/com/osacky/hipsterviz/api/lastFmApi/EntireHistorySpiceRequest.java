package com.osacky.hipsterviz.api.lastFmApi;

import android.util.Log;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.Attr;
import com.osacky.hipsterviz.models.TrackHistoryPage;
import com.osacky.hipsterviz.models.track.TrackListTrack;
import com.osacky.hipsterviz.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

public class EntireHistorySpiceRequest extends RetrofitSpiceRequest<EntireHistorySpiceRequest.EntireHistoryResponse,
        LastFmApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "EntireHistorySpiceRequest";
    private static final long cacheDuration = DurationInMillis.ALWAYS_RETURNED;
    private final String mUsername;
    private int totalPages = Integer.MAX_VALUE;
    private int currentPage = 0;

    public EntireHistorySpiceRequest(String username) {
        super(EntireHistoryResponse.class, LastFmApi.class);
        mUsername = username;
    }

    public static CachedSpiceRequest<EntireHistoryResponse> getCachedSpiceRequest(String username) {
        EntireHistorySpiceRequest historyPageSpiceRequest = new EntireHistorySpiceRequest(username);
        return new CachedSpiceRequest<EntireHistoryResponse>(historyPageSpiceRequest, username, cacheDuration);
    }

    @Override
    public EntireHistoryResponse loadDataFromNetwork() throws Exception {
        EntireHistoryResponse response = new EntireHistoryResponse();
        while (currentPage < totalPages) {
            TrackHistoryPage historyPage = getService().getRecentTracks(mUsername, currentPage + 1);
            Log.i(TAG, "entire history totalPages " + totalPages + " currentPage " + currentPage);
            fillMap(response, historyPage);
        }
        return response;
    }

    private void fillMap(EntireHistoryResponse response, TrackHistoryPage historyPage) throws Exception {
        final Attr attr = historyPage.getAttr();
        final float progress = attr.getProgress();
        publishProgress(progress);
        totalPages = attr.getTotalPages();
        currentPage = attr.getPage();

        long key;
        String identifier;
        for (TrackListTrack track : historyPage.getTrack()) {
            key = (Utils.roundWeek(track.getDateTime()).getMillis());
            identifier = track.getArtist().getIdentifier();
            List<String> identifiers = response.historyMap.get(key);
            if (identifiers == null) {
                identifiers = new ArrayList<String>();
                response.historyMap.put(key, identifiers);
            }
            identifiers.add(identifier);
            response.allArtists.add(identifier);
        }
    }

    public static class EntireHistoryResponse {
        HashSet<String> allArtists = new HashSet<String>();

        TreeMap<Long, List<String>> historyMap = new TreeMap<Long, List<String>>();

        public HashSet<String> getAllArtists() {
            return allArtists;
        }

        public TreeMap<Long, List<String>> getHistoryMap() {
            return historyMap;
        }
    }
}
