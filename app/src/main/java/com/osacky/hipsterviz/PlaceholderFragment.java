package com.osacky.hipsterviz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.osacky.hipsterviz.models.Track;

public class PlaceholderFragment extends BaseSpiceFragment {

    private static final String TAG = "PlaceHolderFragment";

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadingInterface.onLoadingStarted();
        UserHistorySpiceRequest request = new UserHistorySpiceRequest("nosacky");
        getSpiceManager().execute(request, "nosacky", DurationInMillis.ALWAYS_RETURNED, new UserHistoryRequestListener());
    }

    private final class UserHistoryRequestListener implements RequestListener<Track.List>, RequestProgressListener {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.i(TAG, spiceException.getMessage());
            loadingInterface.onLoadingFinished();
        }

        @Override
        public void onRequestSuccess(Track.List tracks) {
            Log.i(TAG, tracks.get(0).getName());
            loadingInterface.onLoadingFinished();
        }

        @Override
        public void onRequestProgressUpdate(RequestProgress progress) {
            Log.i(TAG, "progress is " + progress.getProgress());
            loadingInterface.onLoadingProgressUpdate((int) (progress.getProgress() * 10000));
        }
    }
}
