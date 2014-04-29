package com.osacky.hipsterviz;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.osacky.hipsterviz.api.lastFmApi.TrackSpiceRequest;
import com.osacky.hipsterviz.models.track.RealBaseTrack;
import com.osacky.hipsterviz.models.track.RealTrackWithOneTag;
import com.osacky.hipsterviz.models.track.RealTrackWithTags;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_track_detail)
public class TrackDetailFragment extends BaseSpiceFragment
        implements RequestListener<RealBaseTrack> {

    private Picasso mPicasso;

    @ViewById(R.id.track_image)
    ImageView trackImage;

    @ViewById(R.id.track_title)
    TextView trackTitle;

    @ViewById(R.id.track_artist)
    TextView trackArtist;

    @ViewById
    TextView tags;

    @ViewById(R.id.track_wiki)
    TextView wiki;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicasso = Picasso.with(getActivity());
    }

    public void loadData(String mbid) {
        getSpiceManager().execute(TrackSpiceRequest.getCachedSpiceRequest(mbid), this);
    }

    public void loadData(String track, String artist) {
        getSpiceManager().execute(TrackSpiceRequest.getCachedSpiceRequest(track, artist), this);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(RealBaseTrack realTrack) {
        String topTags = "No tags";
        if (realTrack instanceof RealTrackWithTags) {
            topTags = ((RealTrackWithTags) realTrack).getToptags();
        } else if (realTrack instanceof RealTrackWithOneTag) {
            topTags = ((RealTrackWithOneTag) realTrack).getToptags();
        }
        tags.setText(topTags);
        trackTitle.setText(realTrack.getName());
        trackArtist.setText(realTrack.getArtist().getName());
        mPicasso.load(realTrack.getImage(getActivity())).into(trackImage);
        if (realTrack.getWiki() != null) {
            wiki.setText(Html.fromHtml(realTrack.getWiki().content));
        }
    }
}
