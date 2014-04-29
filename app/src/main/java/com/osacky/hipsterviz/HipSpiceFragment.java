package com.osacky.hipsterviz;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.listener.PendingRequestListener;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.osacky.hipsterviz.api.LoadingInterface;
import com.osacky.hipsterviz.api.lastFmApi.ArtistSpiceRequest;
import com.osacky.hipsterviz.api.lastFmApi.EntireHistorySpiceRequest;
import com.osacky.hipsterviz.api.lastFmApi.LastFmSpiceService;
import com.osacky.hipsterviz.api.lastFmApi.ProcessScoreSpiceRequest;
import com.osacky.hipsterviz.api.thomasApi.RankArtistsSpicePost;
import com.osacky.hipsterviz.api.thomasApi.RankSpicePost;
import com.osacky.hipsterviz.api.thomasApi.RequestArtistsSpiceRequest;
import com.osacky.hipsterviz.api.thomasApi.ThomasApiService;
import com.osacky.hipsterviz.models.ArtistDataResponse;
import com.osacky.hipsterviz.models.artist.RealBaseArtist;
import com.osacky.hipsterviz.utils.Utils;
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
    protected LoadingInterface loadingInterface;
    @ViewById(R.id.hipster_artist_name)
    TextView artistName;
    @ViewById(R.id.hipster_artist_image)
    ImageView imageView;
    RequestListener<RealBaseArtist> artistRequestListener = new RequestListener<RealBaseArtist>() {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showToast(spiceException);
        }

        @Override
        public void onRequestSuccess(RealBaseArtist realArtist) {
            loadingInterface.onLoadingFinished();
            artistName.setText(realArtist.getName());
            mPicasso.load(realArtist.getImage(getActivity()))
                    .centerCrop()
                    .resize(imageSize, imageSize)
                    .transform(roundedTransformation)
                    .into(imageView);
        }
    };
    @ViewById(R.id.hipster_button_yes)
    View yes;
    @ViewById(R.id.hipster_button_no)
    View no;
    @ViewById(R.id.hipster_button_dont_know)
    View dunno;
    private Picasso mPicasso;
    private RoundedTransformation roundedTransformation;
    private List<String> mArtistIdList = new ArrayList<String>();
    private SpiceManager thomasSpiceManager = new SpiceManager(ThomasApiService.class);
    private SpiceManager lastFmSpiceManager = new SpiceManager(LastFmSpiceService.class);
    private int imageSize;
    private boolean loadingDone = false;
    PendingRequestListener<ProcessScoreSpiceRequest.ScoreResponse> scoreResponseListener =
            new PendingRequestListener<ProcessScoreSpiceRequest.ScoreResponse>() {

                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    showToast(spiceException);
                }

                @Override
                public void onRequestSuccess(ProcessScoreSpiceRequest.ScoreResponse scoreResponse) {
                    loadingInterface.onLoadingFinished();
                    loadingDone = true;
                    Toast.makeText(getActivity(), "Score is really finally done loaded",
                            Toast.LENGTH_LONG).show();

                }

                @Override
                public void onRequestNotFound() {

                }
            };
    private int totalRated = 0;
    private EntireHistorySpiceRequest.EntireHistoryResponse mHistory;
    private CachedSpiceRequest<EntireHistorySpiceRequest.EntireHistoryResponse>
            entireHistoryRequest;
    private CachedSpiceRequest<ArtistDataResponse.ArtistList> artistListRequest =
            RequestArtistsSpiceRequest.getCachedSpiceRequest(100);
    PendingRequestListener<ArtistDataResponse.ArtistList> artistListListener =
            new PendingRequestListener<ArtistDataResponse.ArtistList>() {

                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    showToast(spiceException);
                }

                @Override
                public void onRequestSuccess(ArtistDataResponse.ArtistList artistDataResponses) {
                    loadingInterface.onLoadingFinished();
                    for (ArtistDataResponse data : artistDataResponses) {
                        mArtistIdList.add(data.getArtistId());
                    }
                    loadFirstArtist();
                }

                @Override
                public void onRequestNotFound() {
                    getThomasSpiceManager().execute(artistListRequest, this);
                }
            };
    private CachedSpiceRequest<ProcessScoreSpiceRequest.ScoreResponse> scoreResponseRequest;
    RequestListener<RankArtistsSpicePost.ArtistLookup> artistLookupListener =
            new RequestListener<RankArtistsSpicePost.ArtistLookup>() {

                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    showToast(spiceException);
                }

                @Override
                public void onRequestSuccess(RankArtistsSpicePost.ArtistLookup artistDataResponses) {
                    if (scoreResponseRequest == null) {
                        scoreResponseRequest = ProcessScoreSpiceRequest.getCachedSpiceRequest
                                (mHistory, artistDataResponses);
                    }
                    getLastFmSpiceManager().execute(scoreResponseRequest, scoreResponseListener);
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingInterface.onLoadingStarted();
        mPicasso = Picasso.with(getActivity().getApplicationContext());
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String username = sharedPreferences.getString(getString(R.string.PREF_USERNAME), "");
        assert ((username != null) && (username.length() <= 0));
        entireHistoryRequest = EntireHistorySpiceRequest.getCachedSpiceRequest(username);
        getThomasSpiceManager().addListenerIfPending(ArtistDataResponse.ArtistList.class, 100,
                artistListListener);
        getLastFmSpiceManager().addListenerIfPending(EntireHistorySpiceRequest
                .EntireHistoryResponse.class, username, new EntireHistoryRequestListener());
        getLastFmSpiceManager().addListenerIfPending(ProcessScoreSpiceRequest.ScoreResponse.class,
                "score", scoreResponseListener);
    }

    @AfterViews
    void initViews() {
        TextView yesText = (TextView) yes.findViewById(R.id.circle_button_text);
        TextView noText = (TextView) no.findViewById(R.id.circle_button_text);
        TextView dunnoText = (TextView) dunno.findViewById(R.id.circle_button_text);

        yesText.setText(getString(R.string.yes_text));
        noText.setText(getString(R.string.no_text));
        dunnoText.setText(getString(R.string.dunno_text));
        final ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        assert viewTreeObserver != null;
        // fill up image based on available space
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageSize = imageView.getMeasuredHeight();
                try {
                    viewTreeObserver.removeOnPreDrawListener(this);
                } catch (IllegalStateException ignored) {
                }
                roundedTransformation = new RoundedTransformation(
                        imageSize / 2,
                        5,
                        getResources().getColor(android.R.color.white),
                        (int) getResources().getDimension(R.dimen.artist_border_radius)
                );
                return true;
            }
        });
        Typeface boldFace = Typeface.createFromAsset(getResources().getAssets(), "fonts/OpenSans-Bold.ttf");
        Typeface normalFace = Typeface.createFromAsset(getResources().getAssets(), "fonts/OpenSans-Regular.ttf");

        artistName.setTypeface(boldFace);
        yesText.setTypeface(normalFace);
        noText.setTypeface(normalFace);
        dunnoText.setTypeface(normalFace);

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
            getThomasSpiceManager().execute(
                    RankSpicePost.getCachedSpiceRequest(mArtistIdList.get(0), classification),
                    new RankArtistRequestListener()
            );
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

    private void loadFirstArtist() {
        if (!mArtistIdList.isEmpty()) {
            CachedSpiceRequest<RealBaseArtist> cachedSpiceRequest;
            /*  what a bad-ass motherfucking regex
             *  this checks if the id is an mbid or an actual artist name
             */
            String firstArtist = mArtistIdList.get(0);
            if (Utils.isMbid(firstArtist)) {
                cachedSpiceRequest = ArtistSpiceRequest.getCachedSpiceRequest(
                        firstArtist, true);
            } else {
                cachedSpiceRequest = ArtistSpiceRequest.getCachedSpiceRequest(
                        firstArtist, false);
            }
            getLastFmSpiceManager().execute(
                    cachedSpiceRequest,
                    artistRequestListener
            );
        }
    }

    private void showToast(SpiceException spiceException) {
        loadingInterface.onLoadingFinished();
        spiceException.printStackTrace();
        Toast.makeText(getActivity(), spiceException.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private class RankArtistRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showToast(spiceException);
        }

        @Override
        public void onRequestSuccess(String s) {
            if (s.equals("ok")) {
                totalRated++;
                if (totalRated > 10 && loadingDone) {
                    if (!mArtistIdList.isEmpty()) {
                        mArtistIdList.remove(0);
                    }
                    Toast.makeText(getActivity(), "Done rating!", Toast.LENGTH_SHORT).show();
                    loadFirstArtist();
                    // TODO we're done!
                } else if (!mArtistIdList.isEmpty()) {
                    mArtistIdList.remove(0);
                    loadFirstArtist();
                } else {
                    Toast.makeText(getActivity(), "Need more people to rate!", Toast.LENGTH_SHORT).show();
                    //TODO load more people to rate
                }
            } else {
                Toast.makeText(getActivity(), "That was a weird error", Toast.LENGTH_SHORT).show();
                //TODO retry request?
            }
            loadingInterface.onLoadingFinished();
        }
    }

    private class EntireHistoryRequestListener
            implements PendingRequestListener<EntireHistorySpiceRequest.EntireHistoryResponse>,
            RequestProgressListener {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showToast(spiceException);
        }

        @Override
        public void onRequestSuccess(EntireHistorySpiceRequest.EntireHistoryResponse historyMap) {
            loadingInterface.onLoadingFinished();
            mHistory = historyMap;
            // HOLY SHIT THIS IS AMAZING
            String artistIds = new Gson().toJson(historyMap.getAllArtists());

            getThomasSpiceManager().execute(
                    RankArtistsSpicePost.getCachedSpiceRequest(artistIds),
                    artistLookupListener
            );
        }

        @Override
        public void onRequestProgressUpdate(RequestProgress progress) {
            loadingInterface.onLoadingProgressUpdate(progress.getProgress());
        }

        @Override
        public void onRequestNotFound() {
            getLastFmSpiceManager().execute(entireHistoryRequest, this);
        }
    }
}
