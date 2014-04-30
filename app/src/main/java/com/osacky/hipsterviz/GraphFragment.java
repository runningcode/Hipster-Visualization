package com.osacky.hipsterviz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.osacky.hipsterviz.api.lastFmApi.ProcessScoreSpiceRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

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

    @AfterViews
    void initGraph() {
        createGraph();
    }

    public void createGraph() {
        int size = mScoreResponse.getScoreArray().size();
        GraphView.GraphViewData[] data = new GraphView.GraphViewData[size];
        for (int i=0; i<size; i++) {
            data[i] = new GraphView.GraphViewData(mScoreResponse.getScoreArray().keyAt(i),
                    ((Number)mScoreResponse.getScoreArray().valueAt(i)).doubleValue());
        }
        GraphView graphView = new LineGraphView(getActivity(), getString(R.string.chart_title));
        graphView.addSeries(new GraphViewSeries(data));
        graphContainer.addView(graphView);
    }
}
