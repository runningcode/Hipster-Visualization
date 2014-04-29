package com.osacky.hipsterviz.api.thomasApi;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.ArtistDataResponse;

import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class RankArtistsSpicePost extends RetrofitSpiceRequest<ArtistDataResponse.ArtistList, ThomasApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "RequestArtistSpiceRequest";

    private final String mArtistIds;

    private static final long cacheDuration = DurationInMillis.ALWAYS_EXPIRED;

    public RankArtistsSpicePost(String artistIds) {
        super(ArtistDataResponse.ArtistList.class, ThomasApi.class);
        mArtistIds = "{ \"artists\" : " + artistIds + "}";
    }

    public static CachedSpiceRequest<ArtistDataResponse.ArtistList> getCachedSpiceRequest(String artistIds) {
        RankArtistsSpicePost rankSpicePost = new RankArtistsSpicePost(artistIds);
        return new CachedSpiceRequest<ArtistDataResponse.ArtistList>(rankSpicePost, artistIds, cacheDuration);
    }

    @Override
    public ArtistDataResponse.ArtistList loadDataFromNetwork() throws Exception {
        TypedInput typedInput = new TypedByteArray("application/json",
                mArtistIds.getBytes("UTF-8"));
        return getService().rankArtists(typedInput);
    }
}
