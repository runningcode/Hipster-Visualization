package com.osacky.hipsterviz;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.osacky.hipsterviz.api.TrackSpiceRequest;
import com.osacky.hipsterviz.models.track.RealBaseTrack;
import com.osacky.hipsterviz.models.track.RealTrackNoTags;
import com.osacky.hipsterviz.models.track.RealTrackWithTags;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_track_detail)
public class TrackDetailFragment extends BaseSpiceFragment implements RequestListener<RealBaseTrack> {

    private Picasso mPicasso;

    @ViewById(R.id.track_image)
    ImageView trackImage;

    @ViewById(R.id.track_title)
    TextView trackTitle;

    @ViewById(R.id.track_artist)
    TextView trackArtist;

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
        if (realTrack instanceof RealTrackNoTags) {
            Toast.makeText(getActivity(), "NO TAGS", Toast.LENGTH_SHORT).show();
        } else if (realTrack instanceof RealTrackWithTags) {
            Toast.makeText(getActivity(), "WITH TAGS", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), ":(", Toast.LENGTH_SHORT).show();
        }
        trackTitle.setText(realTrack.getName());
        trackArtist.setText(realTrack.getArtist().getName());
        mPicasso.load(realTrack.getImage(getActivity())).into(trackImage);
    }
}
