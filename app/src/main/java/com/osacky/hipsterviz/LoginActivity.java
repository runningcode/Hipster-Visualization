package com.osacky.hipsterviz;

import android.support.v4.app.FragmentActivity;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity implements LoadingInterface {
    @Override
    public void onLoadingStarted() {
    }

    @Override
    public void onLoadingProgressUpdate(int progress) {
    }

    @Override
    public void onLoadingFinished() {
    }
}
