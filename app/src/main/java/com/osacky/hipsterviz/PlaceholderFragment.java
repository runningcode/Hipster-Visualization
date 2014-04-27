package com.osacky.hipsterviz;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.osacky.hipsterviz.models.Attr;
import com.osacky.hipsterviz.models.TrackHistoryPage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

@EFragment
public class PlaceholderFragment extends BaseSpiceListFragment implements RequestListener<TrackHistoryPage> {

    private static final String TAG = "PlaceHolderFragment";
    private static boolean mDataChanged = false;

    @Bean
    TrackListAdapter trackListAdapter;

    public PlaceholderFragment() {
    }

    @AfterViews
    void bindAdapter() {
        setListAdapter(trackListAdapter);
        setListShown(false);
        setEmptyText("An error occurred while loading the data");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getListAdapter().isEmpty()) {
            loadingInterface.onLoadingStarted();
            HistoryPageSpiceRequest spiceRequest = new HistoryPageSpiceRequest("nosacky", 1);
            getSpiceManager().execute(spiceRequest, "nosacky" + 1, DurationInMillis.ALWAYS_RETURNED, this);
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
        Log.i(TAG, "progress is " + progress);

        if (progress >= 1.0f) {
            loadingInterface.onLoadingFinished();
            loadInBackground();
        } else {
            loadingInterface.onLoadingProgressUpdate((int) (attr.getProgress() * 10000));
            getListAdapter().addData(page.getTrack());
            mDataChanged = true;
            trackListAdapter.addData(page.getTrack());
            setListShown(true);
            getSpiceManager().execute(HistoryPageSpiceRequest.getCachedSpiceRequest("nosacky", attr.getPage() + 1, DurationInMillis.ALWAYS_RETURNED), this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDataChanged) {
            loadInBackground();
        }
    }

    @Override
    public TrackListAdapter getListAdapter() {
        return (TrackListAdapter) super.getListAdapter();
    }

    @Background
    public void loadInBackground() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString(getActivity().getString(R.string.PREF_SAVED_HISTORY), new Gson().toJson(getListAdapter().getTracks()));
        editor.commit();
        mDataChanged = false;
    }
}
