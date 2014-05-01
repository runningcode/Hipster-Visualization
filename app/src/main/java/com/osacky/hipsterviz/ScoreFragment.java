package com.osacky.hipsterviz;

import android.graphics.Typeface;
import android.os.Handler;
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
        Typeface boldFace = Typeface.createFromAsset(getResources().getAssets(), "fonts/OpenSans-Bold.ttf");
        if (mScoreResponse.getTotalHipsterArtistListens() > mScoreResponse
                .getTotalPopArtistListens()) {
            // HIPSTER
            imageView.setImageResource(R.drawable.ic_hipsterbadge);
            titleScore.setText("You are a hipster");
        } else if (mScoreResponse.getTotalPopArtistListens() > mScoreResponse
                .getTotalHipsterArtistListens()) {
            // POP
            titleScore.setText("You are not a hipster");
            imageView.setImageResource(R.drawable.ic_not_hipster_badge);
        } else {
            titleScore.setText("You are a faux hipster");
            imageView.setImageResource(R.drawable.ic_fauxhipsterbadge);
        }
        titleScore.setTypeface(boldFace);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                titleScore.setEndValue(1);
            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setEndValue(1);
            }
        }, 500);
    }
}
