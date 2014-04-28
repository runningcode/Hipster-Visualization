package com.osacky.hipsterviz;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.AbsListView;

import com.google.gson.Gson;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.osacky.hipsterviz.api.HistoryPageSpiceRequest;
import com.osacky.hipsterviz.models.Attr;
import com.osacky.hipsterviz.models.TrackHistoryPage;
import com.osacky.hipsterviz.models.track.TrackListTrack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;

@EFragment
public class HistoryListFragment extends BaseSpiceListFragment implements RequestListener<TrackHistoryPage>, AbsListView.OnScrollListener {

    @SuppressWarnings("unused")
    private static final String TAG = "PlaceHolderFragment";
    private boolean mDataChanged = false;
    private boolean mScrollStateIdle = true;

    @Bean
    TrackListAdapter trackListAdapter;

    public HistoryListFragment() {
    }

    @AfterViews
    void bindAdapter() {
        setListAdapter(trackListAdapter);
        setListShown(false);
        getListView().setFastScrollEnabled(true);
        getListView().setOnScrollListener(this);
        setEmptyText("An error occurred while loading the data");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getListAdapter().isEmpty()) {
            loadingInterface.onLoadingStarted();
            getSpiceManager().execute(HistoryPageSpiceRequest.getCachedSpiceRequest("nosacky", 1, DurationInMillis.ALWAYS_RETURNED) , this);
        } else {
            setListShown(true);
        }
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        spiceException.printStackTrace();
        loadingInterface.onLoadingFinished();
        setEmptyText(spiceException.getMessage());
    }

    @Override
    public void onRequestSuccess(TrackHistoryPage page) {
        final Attr attr = page.getAttr();
        final float progress = attr.getProgress();

        if (progress >= 1.0f) {
            loadingInterface.onLoadingFinished();
            saveDataInBackground();
        } else {
            loadingInterface.onLoadingFinished();
            getListAdapter().addData(page.getTrack());
            setListShown(true);
            mDataChanged = true;
            mScrollStateIdle = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveDataInBackground();
    }

    @Override
    public TrackListAdapter getListAdapter() {
        return (TrackListAdapter) super.getListAdapter();
    }

    @Background
    public void saveDataInBackground() {
        if (mDataChanged) {
            Log.i(TAG, "saving data in background");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
            editor.putString(getActivity().getString(R.string.PREF_SAVED_HISTORY), new Gson().toJson(getListAdapter().getTracks()));
            editor.commit();
            mDataChanged = false;
        }
    }

    @ItemClick
    void listItemClicked(TrackListTrack trackListTrack) {
        if (trackListTrack.getMbid() == null) {
            TrackDetailActivity_.intent(getActivity())
                    .mbid(trackListTrack.getMbid())
                    .start();
        } else {
            TrackDetailActivity_.intent(getActivity())
                    .track(trackListTrack.getName())
                    .artist(trackListTrack.getArtist().getName())
                    .start();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) >= (totalItemCount - 50) && mScrollStateIdle) {
            mScrollStateIdle = false;
            loadingInterface.onLoadingStarted();
            getSpiceManager().execute(HistoryPageSpiceRequest.getCachedSpiceRequest("nosacky", Math.round(totalItemCount/200f) + 1, DurationInMillis.ALWAYS_RETURNED), this);
        }
    }
}
