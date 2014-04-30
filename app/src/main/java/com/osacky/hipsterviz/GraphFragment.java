package com.osacky.hipsterviz;

import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.osacky.hipsterviz.api.lastFmApi.ProcessScoreSpiceRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Jonathan on 4/30/14.
 */
@EFragment(R.layout.fragment_graph)
public class GraphFragment extends Fragment {
    @ViewById(R.id.graph_container)
    LinearLayout graphContainer;

    ProcessScoreSpiceRequest.ScoreResponse mScoreResponse;

    @AfterViews
    void initGraph() {
        createGraph();
    }

    public void setmScoreResponse(ProcessScoreSpiceRequest.ScoreResponse scoreResponse){
        mScoreResponse = scoreResponse;
    }

    public void createGraph() {
        int size = mScoreResponse.getScoreArray().size();
        GraphView.GraphViewData[] data = new GraphView.GraphViewData[size];
        for (int i=0; i<size; i++) {
            data[i] = new GraphView.GraphViewData(mScoreResponse.getScoreArray().keyAt(i),
                    ((Number)mScoreResponse.getScoreArray().valueAt(i)).doubleValue());
        }
        GraphView graphView = new LineGraphView(
                getActivity() // context
                , "GraphViewDemo" // heading
        );
        graphView.addSeries(new GraphViewSeries(data));
        graphContainer.addView(graphView);
    }
}
