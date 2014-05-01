package com.osacky.hipsterviz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Window;

import com.osacky.hipsterviz.api.LoadingInterface;
import com.osacky.hipsterviz.utils.TypefaceSpan;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

@EActivity(R.layout.activity_blank)
@WindowFeature({Window.FEATURE_PROGRESS, Window.FEATURE_INDETERMINATE_PROGRESS})
public class RateActivity extends ActionBarActivity implements LoadingInterface {

    public static final int MAX_PROGRESS = 10000;
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HipSpiceFragment_())
                    .commit();
        }
        setProgressBarVisibility(false);
        setProgressBarIndeterminateVisibility(false);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.blue);
        SpannableString spannableString = new SpannableString(getString(R.string.app_name));
        spannableString.setSpan(
                new TypefaceSpan(this, "OpenSans-Regular.ttf"),
                0,
                spannableString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        getSupportActionBar().setTitle(spannableString);
    }

    @Override
    public void onLoadingStarted() {
        setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onLoadingProgressUpdate(float progress) {
        if (progress >= 1.0f) {
            setProgressBarVisibility(false);
        } else {
            setProgressBarVisibility(true);
            setProgress((int) (progress * MAX_PROGRESS));
        }
    }

    @Override
    public void onLoadingFinished() {
        setProgressBarIndeterminateVisibility(false);
    }
}
