package com.eder.padilla.alanproject.ui.main.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.ui.main.Main2Activity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by ederpadilla on 02/05/17.
 */

public class GrapfhFragment extends Fragment {

    //@BindView(R.id.calendarView)
    //MaterialCalendarView mMaterialCalendarView;

    @BindView(R.id.graph)
    GraphView mGraphView;

    @BindView(R.id.next_fragment_graph)
    TextView mTvNextFragment;

    @OnClick(R.id.next_fragment_graph)
    public void changeFragment(){
        ((Main2Activity)getActivity()).viewPager.setCurrentItem(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_graph,container,false);
        ButterKnife.bind(this,view);
        Realm.init(getActivity());
        //CalendarDay today = CalendarDay.today();
        //mMaterialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        //mMaterialCalendarView.setDateSelected(today, true);
        initGraph(mGraphView);
        return view;
    }

    public static GrapfhFragment newInstance() {
        GrapfhFragment grapfhFragment = new GrapfhFragment();
        return grapfhFragment;
    }

    public void initGraph(GraphView graph) {
        // second series
        //StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        //staticLabelsFormatter.setHorizontalLabels(new String[] {"old", "middle", "new"});
        //staticLabelsFormatter.setVerticalLabels(new String[] {"Baja", "Normal", "Alta"});
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(1, 3),
                new DataPoint(2, 6),
                new DataPoint(3, 2),
                new DataPoint(4, 5)
        });
        series2.setTitle(getString(R.string.temperature));
        series2.setDrawBackground(true);
        series2.setColor(Color.argb(255, 114, 153, 251));
        series2.setBackgroundColor(Color.argb(80, 114, 153, 251));
        series2.setAnimated(true);
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(20);
        series2.setThickness(10);
        series2.setDrawDataPoints(true);
        graph.addSeries(series2);

        // legend
        graph.getLegendRenderer().setVisible(true);
        //graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);

        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);

        // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalableY(true);

        // activate vertical scrolling
        graph.getViewport().setScrollableY(true);
        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
    }
}
