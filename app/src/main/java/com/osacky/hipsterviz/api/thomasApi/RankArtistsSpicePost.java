package com.osacky.hipsterviz.api.thomasApi;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.ArtistDataResponse;

import java.util.List;

public class RankArtistsSpicePost extends RetrofitSpiceRequest<ArtistDataResponse.ArtistList, ThomasApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "RequestArtistSpiceRequest";

    private final List<String> mArtistIds;

    private static final long cacheDuration = DurationInMillis.ALWAYS_EXPIRED;

    public RankArtistsSpicePost(List<String> artistIds) {
        super(ArtistDataResponse.ArtistList.class, ThomasApi.class);
        mArtistIds = artistIds;
    }

    public static CachedSpiceRequest<ArtistDataResponse.ArtistList> getCachedSpiceRequest(List<String> artistIds) {
        RankArtistsSpicePost rankSpicePost = new RankArtistsSpicePost(artistIds);
        return new CachedSpiceRequest<ArtistDataResponse.ArtistList>(rankSpicePost, artistIds, cacheDuration);
    }

    @Override
    public ArtistDataResponse.ArtistList loadDataFromNetwork() throws Exception {
        return getService().rankArtists(mArtistIds);
    }
}
