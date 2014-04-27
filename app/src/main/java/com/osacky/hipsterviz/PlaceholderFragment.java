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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

@EFragment
public class PlaceholderFragment extends BaseSpiceListFragment implements RequestListener<TrackHistoryPage>, AbsListView.OnScrollListener {

    @SuppressWarnings("unused")
    private static final String TAG = "PlaceHolderFragment";
    private static boolean mDataChanged = false;
    private static boolean mScrollStateIdle = true;

    @Bean
    TrackListAdapter trackListAdapter;

    public PlaceholderFragment() {
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
            HistoryPageSpiceRequest spiceRequest = new HistoryPageSpiceRequest("nosacky", 1);
            getSpiceManager().execute(spiceRequest, "nosacky" + 1, DurationInMillis.ALWAYS_EXPIRED, this);
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
        if (mDataChanged) {
            saveDataInBackground();
        }
    }

    @Override
    public TrackListAdapter getListAdapter() {
        return (TrackListAdapter) super.getListAdapter();
    }

    @Background
    public void saveDataInBackground() {
        Log.i(TAG, "saving data in background");
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString(getActivity().getString(R.string.PREF_SAVED_HISTORY), new Gson().toJson(getListAdapter().getTracks()));
        editor.commit();
        mDataChanged = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) >= totalItemCount && mScrollStateIdle) {
            mScrollStateIdle = false;
            loadingInterface.onLoadingStarted();
            getSpiceManager().execute(HistoryPageSpiceRequest.getCachedSpiceRequest("nosacky", Math.round(totalItemCount/200f) + 1, DurationInMillis.ALWAYS_RETURNED), this);
        }
    }
}
