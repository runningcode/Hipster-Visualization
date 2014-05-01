package com.osacky.hipsterviz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.osacky.hipsterviz.api.lastFmApi.ProcessScoreSpiceRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import static com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

@EFragment(R.layout.fragment_graph)
public class GraphFragment extends Fragment {

    @ViewById(R.id.graph_container)
    LinearLayout graphContainer;

    @FragmentArg
    ProcessScoreSpiceRequest.ScoreResponse mScoreResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((ActionBarActivity) getActivity()).getSupportActionBar().show();
    }

    @AfterViews
    void initGraph() {
        createGraph();
    }

    public void createGraph() {
        final SparseArray<Double> scoreArray = mScoreResponse.getScoreArray();
        final int size = scoreArray.size();
        final double width = scoreArray.keyAt(size - 1) - scoreArray.keyAt(0);
        final GraphView graphView = new LineGraphView(getActivity(), getString(R.string.chart_title));

        GraphView.GraphViewData[] data = new GraphView.GraphViewData[size];
        for (int i=0; i<size; i++) {
            data[i] = new GraphView.GraphViewData(scoreArray.keyAt(i), scoreArray.valueAt(i));
        }

        final GraphViewSeriesStyle graphViewSeriesStyle = new GraphViewSeriesStyle(Color.WHITE, 4);
        graphView.addSeries(new GraphViewSeries("Score", graphViewSeriesStyle, data));
        graphView.setViewPort(0, width);
        graphView.setScalable(true);
        graphView.setScrollable(true);
        graphContainer.addView(graphView);
    }
}
