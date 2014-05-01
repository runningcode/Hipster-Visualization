package com.osacky.hipsterviz;

import android.support.v4.app.Fragment;

import com.osacky.hipsterviz.api.lastFmApi.ProcessScoreSpiceRequest;
import com.osacky.hipsterviz.views.SpringyFontFitTextView;
import com.osacky.hipsterviz.views.SpringyImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_score)
public class ScoreFragment extends Fragment {

    @ViewById(R.id.score_title)
    SpringyFontFitTextView titleScore;

    @ViewById(R.id.score_badge)
    SpringyImageView imageView;

    @FragmentArg
    ProcessScoreSpiceRequest.ScoreResponse mScoreResponse;

    @AfterViews
    void setupScore() {
        titleScore.setEndValue(1);
        imageView.setEndValue(1);
    }
}
