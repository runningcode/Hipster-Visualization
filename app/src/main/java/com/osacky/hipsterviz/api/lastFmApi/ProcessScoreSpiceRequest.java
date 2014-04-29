package com.osacky.hipsterviz.api.lastFmApi;

import android.util.Log;
import android.util.SparseArray;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.api.thomasApi.RankArtistsSpicePost;
import com.osacky.hipsterviz.models.ArtistDataResponse;
import com.osacky.hipsterviz.models.artist.RealBaseArtist;
import com.osacky.hipsterviz.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class ProcessScoreSpiceRequest
        extends RetrofitSpiceRequest<ProcessScoreSpiceRequest.ScoreResponse, LastFmApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "ProcessScoreSpiceRequest";

    private static final int UNKNOWN = -1;
    private static final int INDIE = 0;
    private static final int POP = 1;

    private final EntireHistorySpiceRequest.EntireHistoryResponse mHistory;
    private final RankArtistsSpicePost.ArtistLookup mArtistResponse;

    private static final long cacheDuration = DurationInMillis.ALWAYS_RETURNED;
    public static class ScoreResponse {
        SparseArray<Float> scoreArray = new SparseArray<Float>();
        int totalArtists;
        int totalHiptserArtists;
        int totalPopArtists;
        int totalHipsterArtistListens = 0;
        int totalPopArtistListens = 0;
        int totalListens;
        HashMap<String, Integer> artistScoreLookup = new HashMap<String, Integer>();

    }

    public ProcessScoreSpiceRequest(EntireHistorySpiceRequest.EntireHistoryResponse history,
                                    RankArtistsSpicePost.ArtistLookup artistResponse) {
        super(ScoreResponse.class, LastFmApi.class);
        mHistory = history;
        mArtistResponse = artistResponse;
    }

    public static CachedSpiceRequest<ScoreResponse> getCachedSpiceRequest
            (EntireHistorySpiceRequest.EntireHistoryResponse history,
             RankArtistsSpicePost.ArtistLookup artistList) {
        ProcessScoreSpiceRequest historyPageSpiceRequest = new ProcessScoreSpiceRequest(history, artistList);
        return new CachedSpiceRequest<ScoreResponse>(historyPageSpiceRequest, history, cacheDuration);
    }

    @Override
    public ScoreResponse loadDataFromNetwork() throws Exception {
        ScoreResponse response = new ScoreResponse();
        for (String artist : mHistory.getAllArtists()) {
            RealBaseArtist artistInfoByName;
            if(Utils.isMbid(artist)) {
                artistInfoByName = getService().getArtistInfoByMbid(artist);
            } else {
                artistInfoByName = getService().getArtistInfoByName(artist);
            }
            final int classification = classifyArtist(artistInfoByName);
            switch (classification) {
                case INDIE:
                    response.totalHiptserArtists++;
                    break;
                case POP:
                    response.totalPopArtists++;
                    break;
            }
            response.artistScoreLookup.put(artist, classification);
            response.totalArtists++;
        }
        final SparseArray<List<String>> historyMap = mHistory.getHistoryMap();
        int key;
        for (int i = 0; i < historyMap.size(); i ++) {
            key = historyMap.keyAt(i);
            final List<String> artistIds = historyMap.get(key);
            int songsForToday = artistIds.size();
            response.totalListens += songsForToday;
            int todayIndieListens = 0;
            int todayPopListens = 0;
            for (String string : artistIds) {
                int songClass = response.artistScoreLookup.get(string);
                if (songClass == INDIE) {
                    todayIndieListens++;
                } else if (songClass == POP) {
                    todayPopListens++;
                }
            }
            response.totalHipsterArtistListens += todayIndieListens;
            response.totalPopArtistListens += todayPopListens;
            response.scoreArray.put(key, 1.0f * todayIndieListens/songsForToday);
        }
        return response;
    }

    private int classifyArtist(RealBaseArtist realArtist) throws Exception {
        String identifier = realArtist.getIdentifier();
        if (!mArtistResponse.containsKey(identifier)) {
            Log.i(TAG, "missing key " + identifier);
            // this is a bug
            return UNKNOWN;
        }
        final ArtistDataResponse artistData = mArtistResponse.get(identifier);
        final String tags = realArtist.getTags();
        if (tags == null && artistData.getSum() == 0) {
            // we don't have any data
            return UNKNOWN;
        } else if (artistData.getSum() == 0) {
            // calculate score based on tags because we don't have votes yet
            if (tags.contains("indie")) {
                return INDIE;
            } else if (tags.contains("pop")){
                return POP;
            } else {
                return UNKNOWN;
            }
        } else {
            // calculate score based on votes
            if (artistData.getHipster() > artistData.getNotHipster()) {
                return INDIE;
            } else {
                return UNKNOWN;
            }
        }
    }
}