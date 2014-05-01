package com.osacky.hipsterviz;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.osacky.hipsterviz.api.LoadingInterface;
import com.osacky.hipsterviz.api.lastFmApi.ProcessScoreSpiceRequest;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_score)
public class ScoreActivity extends ActionBarActivity implements LoadingInterface {

    @Extra
    ProcessScoreSpiceRequest.ScoreResponse mScoreResponse;

    @ViewById(R.id.pager)
    ViewPager mViewPager;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private SystemBarTintManager tintManager;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.blue);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (this);
        username = sharedPreferences.getString(getString(R.string.PREF_USERNAME), "nosacky");
    }

    @AfterViews
    void setUpGraph() {
        mViewPager.setAdapter(mSectionsPagerAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            mViewPager.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), 0);
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return ScoreFragment_.builder()
                            .mScoreResponse(mScoreResponse)
                            .build();
                case 1:
                    return GraphFragment_.builder()
                            .mScoreResponse(mScoreResponse)
                            .build();
                case 2:
                    return HistoryListFragment_.builder()
                            .username(username)
                            .build();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SCORE";
                case 1:
                    return "CHART";
                case 2:
                    return "HISTORY";
            }
            return null;
        }
    }
}
