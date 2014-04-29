package com.osacky.hipsterviz.api.thomasApi;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.ArtistDataResponse;

import java.util.HashMap;
import java.util.List;

import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class RankArtistsSpicePost extends RetrofitSpiceRequest<RankArtistsSpicePost.ArtistLookup, ThomasApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "RequestArtistSpiceRequest";
    private static final long cacheDuration = DurationInMillis.ALWAYS_EXPIRED;

    private final String mArtistIds;

    public static class ArtistLookup extends HashMap<String, ArtistDataResponse>{}


    public RankArtistsSpicePost(String artistIds) {
        super(ArtistLookup.class, ThomasApi.class);
        mArtistIds = "{ \"artists\" : " + artistIds + "}";
    }

    public static CachedSpiceRequest<ArtistLookup> getCachedSpiceRequest(String artistIds) {
        RankArtistsSpicePost rankSpicePost = new RankArtistsSpicePost(artistIds);
        return new CachedSpiceRequest<ArtistLookup>(rankSpicePost, artistIds, cacheDuration);
    }

    @Override
    public ArtistLookup loadDataFromNetwork() throws Exception {
        TypedInput typedInput = new TypedByteArray("application/json",
                mArtistIds.getBytes("UTF-8"));
        List<ArtistDataResponse> artistList = getService().rankArtists(typedInput);
        ArtistLookup artistLookup = new ArtistLookup();
        for (ArtistDataResponse artistData : artistList) {
            artistLookup.put(artistData.getArtistId(), artistData);
        }
        return artistLookup;
    }
}
