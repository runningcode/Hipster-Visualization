package com.osacky.hipsterviz;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.osacky.hipsterviz.views.SpringyButton;
import com.osacky.hipsterviz.views.SpringyFontFitTextView;
import com.osacky.hipsterviz.views.SpringyImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_hip_or_not)
@OptionsMenu(R.menu.hip_menu)
public class HipSpiceFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = "HipSpiceFragment";
    private static final int ARTISTS_LOAD_LIMIT = 20;
    protected LoadingInterface loadingInterface;
    private ProcessScoreSpiceRequest.ScoreResponse mScoreResponse;

    @ViewById(R.id.hipster_artist_name)
    SpringyFontFitTextView artistName;

    @ViewById(R.id.hipster_artist_image)
    SpringyImageView imageView;

    @ViewById(R.id.hipster_button_yes)
    SpringyButton yes;

    @ViewById(R.id.hipster_button_no)
    SpringyButton no;

    @ViewById(R.id.hipster_button_dont_know)
    SpringyButton dunno;

    private Picasso mPicasso;
    private RoundedTransformation roundedTransformation;
    private List<String> mArtistIdList = new ArrayList<String>();
    private SpiceManager thomasSpiceManager = new SpiceManager(ThomasApiService.class);
    private SpiceManager lastFmSpiceManager = new SpiceManager(LastFmSpiceService.class);

    private int imageSize;
    private boolean loadingDone = false;
    private int totalRated = 0;
    private String mUsername;

    private EntireHistorySpiceRequest.EntireHistoryResponse mHistory;
    private CachedSpiceRequest<EntireHistorySpiceRequest.EntireHistoryResponse>
            entireHistoryRequest;
    private CachedSpiceRequest<ArtistDataResponse.ArtistList> artistListRequest =
            RequestArtistsSpiceRequest.getCachedSpiceRequest(ARTISTS_LOAD_LIMIT);
    private CachedSpiceRequest<ProcessScoreSpiceRequest.ScoreResponse> scoreResponseRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingInterface.onLoadingStarted();
        mPicasso = Picasso.with(getActivity().getApplicationContext());
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUsername = sharedPreferences.getString(getString(R.string.PREF_USERNAME), "");
        assert ((mUsername != null) && (mUsername.length() <= 0));
        entireHistoryRequest = EntireHistorySpiceRequest.getCachedSpiceRequest(mUsername);
    }

    @AfterViews
    void initViews() {
        final TextView yesText = (TextView) yes.findViewById(R.id.circle_button_text);
        final TextView noText = (TextView) no.findViewById(R.id.circle_button_text);
        final TextView dunnoText = (TextView) dunno.findViewById(R.id.circle_button_text);

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
        if (!loadingDone) {
            setHasOptionsMenu(false);
        }

    }

    @Touch(R.id.hipster_button_yes)
    void onYesTouched(MotionEvent event) {
        handleTouch(event, yes, "hipster");
    }

    @Touch(R.id.hipster_button_no)
    void onNoTouched(MotionEvent event) {
        handleTouch(event, no, "nothipster");
    }

    @Touch(R.id.hipster_button_dont_know)
    void onDunnoTouched(MotionEvent event) {
        handleTouch(event, dunno, "unknown");
    }

    @OptionsItem(R.id.action_show_score)
    void handleShowScore() {
        GraphFragment newFragment = GraphFragment_.builder()
                .mScoreResponse(mScoreResponse)
                .build();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void handleTouch(MotionEvent event, SpringyButton button, String classification) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                button.setTouchValue(1);
                break;
            // this doesn't seem to work
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_HOVER_EXIT:
                button.setTouchValue(0);
                break;
            case MotionEvent.ACTION_UP:
                button.setTouchValue(0);
                rankArtist(classification);
                break;
        }
    }

    private void rankArtist(String classification) {
        setSpringValues(0);
        imageView.setEndValue(0);
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
        getThomasSpiceManager().addListenerIfPending(ArtistDataResponse.ArtistList.class, 100,
                artistListListener);
        getLastFmSpiceManager().addListenerIfPending(EntireHistorySpiceRequest
                .EntireHistoryResponse.class, mUsername, new EntireHistoryRequestListener());
        getLastFmSpiceManager().addListenerIfPending(ProcessScoreSpiceRequest.ScoreResponse.class,
                "score", scoreResponseListener);
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


    private void showToast(SpiceException spiceException) {
        loadingInterface.onLoadingFinished();
        spiceException.printStackTrace();
        Toast.makeText(getActivity(), spiceException.getCause().getMessage(), Toast.LENGTH_SHORT).show();
    }

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
                    if (totalRated > 10) {
                        setHasOptionsMenu(true);
                    }
                    mScoreResponse = scoreResponse;
                }

                @Override
                public void onRequestNotFound() {

                }
            };

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

    RequestListener<RealBaseArtist> artistRequestListener = new RequestListener<RealBaseArtist>() {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showToast(spiceException);
        }

        @Override
        public void onRequestSuccess(RealBaseArtist realArtist) {
            loadingInterface.onLoadingFinished();
            artistName.setText(realArtist.getName());
            artistName.setVisibility(View.VISIBLE);
            setSpringValues(1);
            mPicasso.load(realArtist.getImage(getActivity()))
                    .centerCrop()
                    .resize(imageSize, imageSize)
                    .transform(roundedTransformation)
                    .noFade()
                    .skipMemoryCache()
                    .error(R.drawable.ic_questionmark)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            imageView.setEndValue(1);
                        }

                        @Override
                        public void onError() {
                            imageView.setEndValue(1);
                        }
                    });
        }
    };

    private void setSpringValues(double value) {
        artistName.setEndValue(value);
        no.setAnimValue(value);
        yes.setAnimValue(value);
        dunno.setAnimValue(value);
    }

    private class RankArtistRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showToast(spiceException);
        }

        @Override
        public void onRequestSuccess(String s) {
            loadingInterface.onLoadingFinished();
            if (s.equals("ok")) {
                totalRated++;
                // successfully rated artist so remove it from the list.
                mArtistIdList.remove(0);
                if (totalRated > 10 && loadingDone) {
                    setHasOptionsMenu(true);
                }
            }
            loadFirstArtist();
        }
    }

    private void loadFirstArtist() {
        if (!mArtistIdList.isEmpty()) {
            CachedSpiceRequest<RealBaseArtist> cachedSpiceRequest;
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
            artistName.setEndValue(0);
        } else {
            getThomasSpiceManager().execute(artistListRequest, artistListListener);
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
