package com.osacky.hipsterviz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.osacky.hipsterviz.models.Attr;
import com.osacky.hipsterviz.models.TrackHistoryPage;

public class PlaceholderFragment extends BaseSpiceListFragment implements RequestListener<TrackHistoryPage> {

    private static final String TAG = "PlaceHolderFragment";
    private static boolean mDataChanged = false;

    public PlaceholderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new TrackListAdapter(getActivity()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            new SaveDataTask(getActivity().getApplicationContext()).forceLoad();
        } else {
            loadingInterface.onLoadingProgressUpdate((int) (attr.getProgress() * 10000));
            getListAdapter().addData(page.getTrack());
            mDataChanged = true;
            setListShown(true);
            getSpiceManager().execute(HistoryPageSpiceRequest.getCachedSpiceRequest("nosacky", attr.getPage() + 1, DurationInMillis.ALWAYS_RETURNED), this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDataChanged) {
            new SaveDataTask(getActivity().getApplicationContext()).forceLoad();
        }
    }

    @Override
    public TrackListAdapter getListAdapter() {
        return (TrackListAdapter) super.getListAdapter();
    }

    class SaveDataTask extends AsyncTaskLoader<Void> {

        private final Context mContext;

        public SaveDataTask(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public Void loadInBackground() {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
            editor.putString(mContext.getString(R.string.PREF_SAVED_HISTORY), new Gson().toJson(getListAdapter().getTracks()));
            editor.commit();
            mDataChanged = false;
            return null;
        }
    }
}
