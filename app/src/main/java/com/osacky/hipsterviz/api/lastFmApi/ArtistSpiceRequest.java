package com.osacky.hipsterviz.api.lastFmApi;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.artist.RealArtist;

public class ArtistSpiceRequest extends RetrofitSpiceRequest<RealArtist, LastFmApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "UserHistorySpiceRequest";
    private static final long cacheDuration = DurationInMillis.ALWAYS_EXPIRED;

    private final String mId;
    private final boolean mIsMbid;

    public ArtistSpiceRequest(String id, boolean isMbid) {
        super(RealArtist.class, LastFmApi.class);
        mId = id;
        mIsMbid = isMbid;
    }

    public static CachedSpiceRequest<RealArtist> getCachedSpiceRequest(String id, boolean isMbid) {
        ArtistSpiceRequest historyPageSpiceRequest = new ArtistSpiceRequest(id, isMbid);
        return new CachedSpiceRequest<RealArtist>(historyPageSpiceRequest, id, cacheDuration);
    }

    @Override
    public RealArtist loadDataFromNetwork() throws Exception {
        if (mIsMbid) {
            return getService().getArtistInfoByMbid(mId);
        } else {
            return getService().getArtistInfoByName(mId);
        }
    }
}
