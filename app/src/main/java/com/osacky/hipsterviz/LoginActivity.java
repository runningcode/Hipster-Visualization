package com.osacky.hipsterviz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.osacky.hipsterviz.api.LoadingInterface;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity implements LoadingInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!(sharedPreferences.getString(getString(R.string.PREF_USERNAME), "").length() == 0)) {
            RateActivity_.intent(this).start();
            finish();
        }
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
