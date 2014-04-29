package com.osacky.hipsterviz;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import com.octo.android.robospice.SpiceManager;
import com.osacky.hipsterviz.api.LoadingInterface;
import com.osacky.hipsterviz.api.lastFmApi.LastFmSpiceService;

abstract class BaseSpiceListFragment extends ListFragment {

    protected LoadingInterface loadingInterface;

    private SpiceManager spiceManager = new SpiceManager(LastFmSpiceService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setClipToPadding(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getListView().setFitsSystemWindows(true);
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
