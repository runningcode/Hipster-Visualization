package com.osacky.hipsterviz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.osacky.hipsterviz.api.lastFmApi.ProcessScoreSpiceRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Map;
import java.util.TreeMap;

import static com.jjoe64.graphview.GraphView.GraphViewData;
import static com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

@EFragment(R.layout.fragment_graph)
public class GraphFragment extends Fragment {

    @ViewById(R.id.graph_container)
    FrameLayout graphContainer;

    @FragmentArg
    ProcessScoreSpiceRequest.ScoreResponse mScoreResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @AfterViews
    void initGraph() {
        final TreeMap<Long, Double> scoreArray = mScoreResponse.getScoreArray();
        final double width = scoreArray.lastKey() - scoreArray.firstKey();
        final GraphView graphView = new LineGraphView(getActivity(), getString(R.string.chart_title));

        GraphViewData[] data = new GraphViewData[scoreArray.size()];
        int i = 0;
        for (Map.Entry<Long, Double> entry : scoreArray.entrySet()) {
            data[i] = new GraphViewData(entry.getKey(), entry.getValue());
            i++;
        }

        final GraphViewSeriesStyle graphViewSeriesStyle = new GraphViewSeriesStyle(Color.WHITE, 4);
        graphView.addSeries(new GraphViewSeries("Score", graphViewSeriesStyle, data));
        graphView.setViewPort(0, width);
        graphView.setScalable(true);
        graphView.setScrollable(true);
        graphContainer.addView(graphView);
    }
}
