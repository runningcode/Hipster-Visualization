package com.osacky.hipsterviz;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;
import com.osacky.hipsterviz.api.LastFmSpiceService;

public abstract class BaseSpiceFragment extends Fragment {

    protected LoadingInterface loadingInterface;

    private SpiceManager spiceManager = new SpiceManager(LastFmSpiceService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        spiceManager.start(getActivity());
        super.onStart();
    }

    @Override
    public void onStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
