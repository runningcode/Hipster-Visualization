package com.osacky.hipsterviz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.osacky.hipsterviz.models.Attr;
import com.osacky.hipsterviz.models.TrackHistoryPage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

@EFragment
public class PlaceholderFragment extends BaseSpiceListFragment implements RequestListener<TrackHistoryPage> {

    private static final String TAG = "PlaceHolderFragment";

    @Bean
    TrackListAdapter trackListAdapter;

    public PlaceholderFragment() {
    }

    @AfterViews
    void bindAdapter() {
        setListAdapter(trackListAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListShown(false);
        setEmptyText("YEAH MAN");
    }

    @Override
    public void onStart() {
        super.onStart();
        loadingInterface.onLoadingStarted();
        HistoryPageSpiceRequest spiceRequest = new HistoryPageSpiceRequest("nosacky", 1);
        getSpiceManager().execute(spiceRequest, "nosacky" + 1, DurationInMillis.ALWAYS_RETURNED, this);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Log.i(TAG, spiceException.getMessage());
        loadingInterface.onLoadingFinished();
        setListShown(false);
    }

    @Override
    public void onRequestSuccess(TrackHistoryPage page) {
        final Attr attr = page.getAttr();
        final float progress = attr.getProgress();
        Log.i(TAG, "progress is " + progress);

        if (progress > 1.0f) {
            loadingInterface.onLoadingFinished();
        } else {
            loadingInterface.onLoadingProgressUpdate((int) (attr.getProgress() * 10000));
            trackListAdapter.addData(page.getTrack());
            setListShown(true);
            getSpiceManager().execute(HistoryPageSpiceRequest.getCachedSpiceRequest("nosacky", attr.getPage() + 1, DurationInMillis.ALWAYS_RETURNED), this);
        }
    }
}
