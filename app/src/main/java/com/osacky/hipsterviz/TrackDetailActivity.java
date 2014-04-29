package com.osacky.hipsterviz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.WindowFeature;

@EActivity(R.layout.activity_track_details)
@WindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)
public class TrackDetailActivity extends ActionBarActivity implements LoadingInterface {

    @Extra
    String mbid;

    @Extra
    String artist;

    @Extra
    String track;

    @FragmentById(R.id.track_detail_frag)
    TrackDetailFragment trackDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProgressBarIndeterminateVisibility(false);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.trans_white);
    }

    @AfterViews
    void setArguments() {
        if (mbid != null) {
            trackDetailFragment.loadData(mbid);
        } else {
            trackDetailFragment.loadData(track, artist);
        }
    }

    @Override
    public void onLoadingStarted() {
        setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onLoadingProgressUpdate(float progress) {
    }

    @Override
    public void onLoadingFinished() {
        setProgressBarIndeterminateVisibility(false);
    }
}
