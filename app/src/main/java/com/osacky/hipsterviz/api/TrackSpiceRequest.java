package com.osacky.hipsterviz.api;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.osacky.hipsterviz.models.track.RealBaseTrack;

public class TrackSpiceRequest extends RetrofitSpiceRequest<RealBaseTrack, LastFmApi> {

    @SuppressWarnings("unused")
    private static final String TAG = "TrackSpiceRequest";

    private String mMbid;
    private String mTrack;
    private String mArtist;

    public TrackSpiceRequest(String mbid) {
        super(RealBaseTrack.class, LastFmApi.class);
        mMbid = mbid;
    }

    public TrackSpiceRequest(String track, String artist) {
        super(RealBaseTrack.class, LastFmApi.class);
        mTrack = track;
        mArtist = artist;
    }

    public static CachedSpiceRequest<RealBaseTrack> getCachedSpiceRequest(String mbid) {
        TrackSpiceRequest trackSpiceRequest = new TrackSpiceRequest(mbid);
        return new CachedSpiceRequest<RealBaseTrack>(trackSpiceRequest, mbid, DurationInMillis.ALWAYS_RETURNED);
    }

    public static CachedSpiceRequest<RealBaseTrack> getCachedSpiceRequest(String track, String artist) {
        TrackSpiceRequest trackSpiceRequest = new TrackSpiceRequest(track, artist);
        return new CachedSpiceRequest<RealBaseTrack>(trackSpiceRequest, track + artist, DurationInMillis.ALWAYS_RETURNED);
    }

    @Override
    public RealBaseTrack loadDataFromNetwork() throws Exception {
        if (mMbid != null) {
            return getService().getTrackInfo(mMbid);
        } else {
            return getService().getTrackInfo(mTrack, mArtist);
        }
    }
}
