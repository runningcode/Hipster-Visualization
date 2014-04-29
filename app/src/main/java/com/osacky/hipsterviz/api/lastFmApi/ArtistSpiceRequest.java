package com.osacky.hipsterviz.api.lastFmApi;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.artist.RealBaseArtist;

public class ArtistSpiceRequest extends RetrofitSpiceRequest<RealBaseArtist, LastFmApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "UserHistorySpiceRequest";
    private static final long cacheDuration = DurationInMillis.ALWAYS_EXPIRED;

    private final String mId;
    private final boolean mIsMbid;

    public ArtistSpiceRequest(String id, boolean isMbid) {
        super(RealBaseArtist.class, LastFmApi.class);
        mId = id;
        mIsMbid = isMbid;
    }

    public static CachedSpiceRequest<RealBaseArtist> getCachedSpiceRequest(String id, boolean isMbid) {
        ArtistSpiceRequest historyPageSpiceRequest = new ArtistSpiceRequest(id, isMbid);
        return new CachedSpiceRequest<RealBaseArtist>(historyPageSpiceRequest, id, cacheDuration);
    }

    @Override
    public RealBaseArtist loadDataFromNetwork() throws Exception {
        if (mIsMbid) {
            return getService().getArtistInfoByMbid(mId);
        } else {
            return getService().getArtistInfoByName(mId);
        }
    }
}
