package com.osacky.hipsterviz;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.facebook.rebound.ui.Util;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * this is a god class and should be refactored!
 */
@EFragment(R.layout.fragment_hip_or_not)
public class HipSpiceFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = "HipSpiceFragment";
    protected LoadingInterface loadingInterface;

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
    private SpiceManager thomasSpiceManager = new SpiceManager(ThomasApiService.class);
    private SpiceManager lastFmSpiceManager = new SpiceManager(LastFmSpiceService.class);

    private int imageSize;
    private boolean loadingDone = false;
    private int totalRated = 0;

    private EntireHistorySpiceRequest.EntireHistoryResponse mHistory;
    private CachedSpiceRequest<EntireHistorySpiceRequest.EntireHistoryResponse>
            entireHistoryRequest;
    private CachedSpiceRequest<ArtistDataResponse.ArtistList> artistListRequest =
            RequestArtistsSpiceRequest.getCachedSpiceRequest(100);
    private CachedSpiceRequest<ProcessScoreSpiceRequest.ScoreResponse> scoreResponseRequest;

    private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig
            .fromOrigamiTensionAndFriction(40, 7);
    private SpringSystem springSystem = SpringSystem.create();
    private Spring titleSpring = springSystem.createSpring()
            .setSpringConfig(ORIGAMI_SPRING_CONFIG)
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    renderTitle();
                }
            });
    private Spring noButtonSpring = springSystem.createSpring()
            .setSpringConfig(ORIGAMI_SPRING_CONFIG)
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    renderButton(no, spring, -100f, 0);
                }
            });
    private Spring noTouchSpring = springSystem.createSpring()
            .setSpringConfig(ORIGAMI_SPRING_CONFIG)
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    shrinkView(no, spring);
                }
            });
    private Spring yesButtonSpring = springSystem.createSpring()
            .setSpringConfig(ORIGAMI_SPRING_CONFIG)
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    renderButton(yes, spring, 100f, 0);
                }
            });
    private Spring yesTouchSpring = springSystem.createSpring()
            .setSpringConfig(ORIGAMI_SPRING_CONFIG)
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    shrinkView(yes, spring);
                }
            });
    private Spring dunnoButtonSpring = springSystem.createSpring()
            .setSpringConfig(ORIGAMI_SPRING_CONFIG)
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    renderButton(dunno, spring, 0, 200f);
                }
            });
    private Spring dunnoTouchSpring = springSystem.createSpring()
            .setSpringConfig(ORIGAMI_SPRING_CONFIG)
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    shrinkView(dunno, spring);
                }
            });
    private Spring imageSpring = springSystem.createSpring()
            .setSpringConfig(ORIGAMI_SPRING_CONFIG)
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    renderImage();
                }
            });

    private void shrinkView(View view, Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.2f);
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

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

    @Touch(R.id.hipster_button_yes)
    void onYesTouched(MotionEvent event) {
        handleTouch(event, yesTouchSpring);
    }

    @Touch(R.id.hipster_button_no)
    void onNoTouched(MotionEvent event) {
        handleTouch(event, noTouchSpring);
    }

    @Touch(R.id.hipster_button_dont_know)
    void onDunnoTouched(MotionEvent event) {
        handleTouch(event, dunnoTouchSpring);
    }

    private void handleTouch(MotionEvent event, Spring s) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                s.setEndValue(1);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                s.setEndValue(0);
                rankArtist("hipster");
                break;
        }
    }

    private void rankArtist(String classification) {
        titleSpring.setEndValue(0);
        yesButtonSpring.setEndValue(0);
        noButtonSpring.setEndValue(0);
        dunnoButtonSpring.setEndValue(0);
        imageSpring.setEndValue(0);
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
            titleSpring.setEndValue(0);
        }
    }

    private void showToast(SpiceException spiceException) {
        loadingInterface.onLoadingFinished();
        spiceException.printStackTrace();
        Toast.makeText(getActivity(), spiceException.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Score is really finally done loaded",
                            Toast.LENGTH_LONG).show();

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
            no.setVisibility(View.VISIBLE);
            yes.setVisibility(View.VISIBLE);
            dunno.setVisibility(View.VISIBLE);
            titleSpring.setEndValue(1);
            noButtonSpring.setEndValue(1);
            yesButtonSpring.setEndValue(1);
            dunnoButtonSpring.setEndValue(1);
            mPicasso.load(realArtist.getImage(getActivity()))
                    .centerCrop()
                    .resize(imageSize, imageSize)
                    .transform(roundedTransformation)
                    .noFade()
                    .error(R.drawable.ic_questionmark)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            imageSpring.setEndValue(1);
                        }

                        @Override
                        public void onError() {
                            imageSpring.setEndValue(1);
                        }
                    });
        }
    };

    void renderTitle() {
        Resources resources = getResources();
        double value = titleSpring.getCurrentValue();

        float selectedTitleScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0,
                1);
        artistName.setScaleX(selectedTitleScale);
        artistName.setScaleY(selectedTitleScale);

        float titleTranslateY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1,
                Util.dpToPx(-150f, resources), 0);
        artistName.setTranslationY(titleTranslateY);
    }

    void renderButton(View v, Spring s, float xInitial, float yInitial) {
        Resources resources = getResources();
        double value = s.getCurrentValue();

        float selectedTitleScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0,
                1);
        v.setScaleX(selectedTitleScale);
        v.setScaleY(selectedTitleScale);

        float titleTranslateX = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1,
                Util.dpToPx(xInitial, resources), 0);
        float titleTranslateY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1,
                Util.dpToPx(yInitial, resources), 0);
        v.setTranslationX(titleTranslateX);
        v.setTranslationY(titleTranslateY);
    }

    private void renderImage() {
        Resources resources = getResources();
        double value = imageSpring.getCurrentValue();

        float selectedTitleScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, .33,
                1);
        imageView.setScaleX(selectedTitleScale);
        imageView.setScaleY(selectedTitleScale);

        float titleTranslateY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1,
                Util.dpToPx(-350f, resources), 0);
        imageView.setTranslationY(titleTranslateY);
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
