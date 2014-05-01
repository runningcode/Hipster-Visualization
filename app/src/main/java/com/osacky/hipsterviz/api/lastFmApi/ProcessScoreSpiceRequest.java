package com.osacky.hipsterviz.api.lastFmApi;

import android.util.Log;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.api.thomasApi.RankArtistsSpicePost;
import com.osacky.hipsterviz.models.ArtistDataResponse;
import com.osacky.hipsterviz.models.artist.RealBaseArtist;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.osacky.hipsterviz.models.ArtistDataResponse.HIPSTER;
import static com.osacky.hipsterviz.models.ArtistDataResponse.MAINSTREAM;

public class ProcessScoreSpiceRequest
        extends RetrofitSpiceRequest<ProcessScoreSpiceRequest.ScoreResponse, LastFmApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "ProcessScoreSpiceRequest";

    private static final int UNKNOWN = -1;
    private static final int INDIE = 0;
    private static final int POP = 1;
    private static final long cacheDuration = DurationInMillis.ALWAYS_RETURNED;
    private final EntireHistorySpiceRequest.EntireHistoryResponse mHistory;
    private final RankArtistsSpicePost.ArtistLookup mArtistResponse;

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
        return new CachedSpiceRequest<>(historyPageSpiceRequest, "score",
                cacheDuration);
    }

    @Override
    public ScoreResponse loadDataFromNetwork() throws Exception {
        ScoreResponse response = new ScoreResponse();
        for (String artist : mHistory.getAllArtists()) {
            Log.i(TAG, "current progress " + response.totalArtists + "/" + mHistory.getAllArtists()
                    .size());
            publishProgress(response.totalArtists * 1.0f / mHistory.getAllArtists().size());

            final String classificationString = mArtistResponse.get(artist).getClassification();
            int classification = UNKNOWN;
            if (classificationString.equals(HIPSTER)) {
                response.totalHipsterArtists++;
                classification = INDIE;
            } else if (classificationString.equals(MAINSTREAM)) {
                response.totalPopArtists++;
                classification = POP;
            }
            response.artistScoreLookup.put(artist, classification);
            response.totalArtists++;
        }
        for (Map.Entry<Long, List<String>> entry : mHistory.getHistoryMap().entrySet()) {
            int songsForToday = entry.getValue().size();
            // don't add a data point if we didn't listen to songs this day
            if (songsForToday == 0) {
                continue;
            }
            response.totalListens += songsForToday;
            int todayIndieListens = 0;
            int todayPopListens = 0;
            for (String string : entry.getValue()) {
                int songClass = response.artistScoreLookup.get(string);
                if (songClass == INDIE) {
                    todayIndieListens++;
                } else if (songClass == POP) {
                    todayPopListens++;
                }
            }
            response.totalHipsterArtistListens += todayIndieListens;
            response.totalPopArtistListens += todayPopListens;
            response.scoreArray.put(entry.getKey(), 1.0 * todayIndieListens / songsForToday);
        }
        return response;
    }

    // this is now being done server side
    @SuppressWarnings("unused")
    private int classifyArtist(RealBaseArtist realArtist) throws Exception {
        String identifier = realArtist.getIdentifier();
        if (!mArtistResponse.containsKey(identifier)) {
            // this should never happen
            throw new Exception("Missing key");
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
            } else if (tags.contains("pop")) {
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

    public static class ScoreResponse implements Serializable {
        TreeMap<Long, Double> scoreArray = new TreeMap<>();
        int totalArtists;
        int totalHipsterArtists;
        int totalPopArtists;
        int totalHipsterArtistListens;
        int totalPopArtistListens;
        int totalListens;
        HashMap<String, Integer> artistScoreLookup = new HashMap<>();

        public TreeMap<Long, Double> getScoreArray() {
            return scoreArray;
        }

        public int getTotalArtists() {
            return totalArtists;
        }

        public int getTotalHipsterArtists() {
            return totalHipsterArtists;
        }

        public int getTotalPopArtists() {
            return totalPopArtists;
        }

        public int getTotalHipsterArtistListens() {
            return totalHipsterArtistListens;
        }

        public int getTotalPopArtistListens() {
            return totalPopArtistListens;
        }

        public int getTotalListens() {
            return totalListens;
        }

        public HashMap<String, Integer> getArtistScoreLookup() {
            return artistScoreLookup;
        }
    }
}
