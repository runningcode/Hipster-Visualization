package com.osacky.hipsterviz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.osacky.hipsterviz.api.LoadingInterface;
import com.osacky.hipsterviz.api.lastFmApi.ProcessScoreSpiceRequest;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_blank)
public class ScoreActivity extends ActionBarActivity implements LoadingInterface {

    private static final String GRAPH_FRAG_TAG = "graph_frag_tag";

    @Extra
    ProcessScoreSpiceRequest.ScoreResponse mScoreResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.blue);
    }

    @AfterViews
    void setUpGraph() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(GRAPH_FRAG_TAG);
        if (fragment == null) {
            fragment = GraphFragment_.builder()
                    .mScoreResponse(mScoreResponse)
                    .build();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, GRAPH_FRAG_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLoadingStarted() {
    }

    @Override
    public void onLoadingProgressUpdate(float progress) {
    }

    @Override
    public void onLoadingFinished() {
    }
}
