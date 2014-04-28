package com.osacky.hipsterviz;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.osacky.hipsterviz.api.lastFmApi.ArtistSpiceRequest;
import com.osacky.hipsterviz.api.lastFmApi.LastFmSpiceService;
import com.osacky.hipsterviz.api.thomasApi.RankSpicePost;
import com.osacky.hipsterviz.api.thomasApi.RequestArtistsSpiceRequest;
import com.osacky.hipsterviz.api.thomasApi.ThomasApiService;
import com.osacky.hipsterviz.models.ArtistDataResponse;
import com.osacky.hipsterviz.models.artist.RealArtist;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_hip_or_not)
public class HipSpiceFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = "HipSpiceFragment";

    @ViewById(R.id.hipster_artist_name)
    TextView artistName;

    @ViewById(R.id.hipster_artist_image)
    ImageView imageView;

    @ViewById(R.id.hipster_button_yes)
    View yes;

    @ViewById(R.id.hipster_button_no)
    View no;

    @ViewById(R.id.hipster_button_dont_know)
    View dunno;

    private Picasso mPicasso;
    private RoundedTransformation roundedTransformation;

    private List<String> mArtistIdList = new ArrayList<String>();

    protected LoadingInterface loadingInterface;

    private SpiceManager thomasSpiceManager = new SpiceManager(ThomasApiService.class);
    private SpiceManager lastFmSpiceManager = new SpiceManager(LastFmSpiceService.class);

    private int imageSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingInterface.onLoadingStarted();
        mPicasso = Picasso.with(getActivity().getApplicationContext());
    }

    @AfterViews
    void setViewText() {
        TextView yesText = (TextView) yes.findViewById(R.id.circle_button_text);
        TextView noText = (TextView) no.findViewById(R.id.circle_button_text);
        TextView dunnoText = (TextView) dunno.findViewById(R.id.circle_button_text);

        yesText.setText(getString(R.string.yes_text));
        noText.setText(getString(R.string.no_text));
        dunnoText.setText(getString(R.string.dunno_text));
        final ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        assert viewTreeObserver != null;
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageSize = imageView.getMeasuredHeight();
                try {
                    viewTreeObserver.removeOnPreDrawListener(this);
                } catch (IllegalStateException ignored) {}
                roundedTransformation = new RoundedTransformation(
                    imageSize /2,
                    5,
                    getResources().getColor(android.R.color.white),
                    (int) getResources().getDimension(R.dimen.artist_border_radius)
                );
                return true;
            }
        });

    }

    @Click(R.id.hipster_button_yes)
    void yesClicked() {
        rankArtist("hipster");
    }

    @Click(R.id.hipster_button_no)
    void noClicked() {
        rankArtist("nothipster");
    }

    @Click(R.id.hipster_button_dont_know)
    void dunnoClicked() {
        rankArtist("unknown");
    }

    private void rankArtist(String classification) {
        if (!mArtistIdList.isEmpty()) {
            loadingInterface.onLoadingStarted();
            getThomasSpiceManager().execute(RankSpicePost.getCachedSpiceRequest(mArtistIdList.get(0), classification), new RankArtistRequestListener());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            loadingInterface = (LoadingInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement LoadingInterface.");
        }
    }

    @Override
    public void onStart() {
        thomasSpiceManager.start(getActivity());
        lastFmSpiceManager.start(getActivity());
        getThomasSpiceManager().execute(RequestArtistsSpiceRequest.getCachedSpiceRequest(100), new ArtistListRequestListener());
        super.onStart();
    }

    @Override
    public void onStop() {
        if (thomasSpiceManager.isStarted()) {
            thomasSpiceManager.shouldStop();
        }
        if (lastFmSpiceManager.isStarted()) {
            lastFmSpiceManager.shouldStop();
        }
        loadingInterface.onLoadingFinished();
        super.onStop();
    }

    protected SpiceManager getThomasSpiceManager() {
        return thomasSpiceManager;
    }
    protected SpiceManager getLastFmSpiceManager() {
        return lastFmSpiceManager;
    }

    private class ArtistListRequestListener implements RequestListener<ArtistDataResponse.ArtistList> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            loadingInterface.onLoadingFinished();
            spiceException.printStackTrace();
            Toast.makeText(getActivity(), "An error occured", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(ArtistDataResponse.ArtistList artistDataResponses) {
            loadingInterface.onLoadingFinished();
            for (ArtistDataResponse artistDataResponse : artistDataResponses) {
                mArtistIdList.add(artistDataResponse.getArtistId());
            }
            if (!mArtistIdList.isEmpty()) {
                getLastFmSpiceManager().execute(ArtistSpiceRequest.getCachedSpiceRequest(mArtistIdList.get(0), true), new ArtistDetailRequestListener());
            }
        }
    }

    private class RankArtistRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            loadingInterface.onLoadingFinished();
            spiceException.printStackTrace();
        }

        @Override
        public void onRequestSuccess(String s) {
            loadingInterface.onLoadingFinished();
            mArtistIdList.remove(0);
        }
    }

    private class ArtistDetailRequestListener implements RequestListener<RealArtist> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            loadingInterface.onLoadingFinished();
            spiceException.printStackTrace();
        }

        @Override
        public void onRequestSuccess(RealArtist realArtist) {
            loadingInterface.onLoadingFinished();
            artistName.setText(realArtist.getName());
            mPicasso.load(realArtist.getImage().get(3).getUrl()).centerCrop().resize(imageSize, imageSize).transform(roundedTransformation).into(imageView);
        }
    }
}
