package com.osacky.hipsterviz.api.thomasApi;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.ArtistDataResponse;

public class RequestArtistsSpiceRequest extends RetrofitSpiceRequest<ArtistDataResponse.ArtistList, ThomasApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "RequestArtistSpiceRequest";

    private final int mLimit;
    private static final long cacheDuration = DurationInMillis.ALWAYS_EXPIRED;

    public RequestArtistsSpiceRequest(int limit) {
        super(ArtistDataResponse.ArtistList.class, ThomasApi.class);
        mLimit = limit;

    }

    public static CachedSpiceRequest<ArtistDataResponse.ArtistList> getCachedSpiceRequest(int limit) {
        RequestArtistsSpiceRequest historyPageSpiceRequest = new RequestArtistsSpiceRequest(limit);
        return new CachedSpiceRequest<ArtistDataResponse.ArtistList>(historyPageSpiceRequest, limit, cacheDuration);
    }

    @Override
    public ArtistDataResponse.ArtistList loadDataFromNetwork() throws Exception {
        return getService().requestArtists(mLimit);
    }
}
