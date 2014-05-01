package com.osacky.hipsterviz.api.thomasApi;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class RankSpicePost extends RetrofitSpiceRequest<String, ThomasApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "RequestArtistSpiceRequest";
    // hack so we never send the same request twice (as long as the cache isn't cleared)
    private static final long cacheDuration = DurationInMillis.ALWAYS_RETURNED;
    private final String mArtistId;
    private final String mClassification;


    public RankSpicePost(String artistId, String classification) {
        super(String.class, ThomasApi.class);
        mArtistId = artistId;
        mClassification = classification;
    }

    public static CachedSpiceRequest<String> getCachedSpiceRequest(String artistId, String classification) {
        RankSpicePost rankSpicePost = new RankSpicePost(artistId, classification);
        return new CachedSpiceRequest<>(rankSpicePost, artistId, cacheDuration);
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        return getService().rank(mArtistId, mClassification);
    }
}
