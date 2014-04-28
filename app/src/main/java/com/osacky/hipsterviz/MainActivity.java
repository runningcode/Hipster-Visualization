package com.osacky.hipsterviz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.WindowFeature;

@EActivity(R.layout.activity_blank)
@WindowFeature({Window.FEATURE_PROGRESS, Window.FEATURE_INDETERMINATE_PROGRESS})
public class MainActivity extends ActionBarActivity implements LoadingInterface {

    @Extra
    String username;

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
    }

    @Override
    public void onLoadingStarted() {
//        setProgressBarVisibility(true);
        setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onLoadingProgressUpdate(int progress) {
//        setProgress(progress);
    }

    @Override
    public void onLoadingFinished() {
//        setProgressBarVisibility(false);
        setProgressBarIndeterminateVisibility(false);
    }
}
