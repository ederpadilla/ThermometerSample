package com.eder.padilla.alanproject.ui.main.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eder.padilla.alanproject.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by ederpadilla on 02/05/17.
 */

public class GrapfhFragment extends Fragment {

    @BindView(R.id.calendarView)
    MaterialCalendarView mMaterialCalendarView;

    @BindView(R.id.graph)
    GraphView mGraphView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_graph,container,false);
        ButterKnife.bind(this,view);
        Realm.init(getActivity());
        CalendarDay today = CalendarDay.today();
        mMaterialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        mMaterialCalendarView.setDateSelected(today, true);
        initGraph(mGraphView);
        return view;
    }

    public static GrapfhFragment newInstance() {
        GrapfhFragment grapfhFragment = new GrapfhFragment();
        return grapfhFragment;
    }

    public void initGraph(GraphView graph) {
        graph.setScaleX(10f);
        // first series
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series.setColor(R.color.colorPrimaryDark);
        series.setTitle("first line");
        graph.addSeries(series);

        // second series
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(1, 3),
                new DataPoint(2, 6),
                new DataPoint(3, 2),
                new DataPoint(4, 5)
        });
        series2.setTitle("speed");
        series2.setDrawBackground(true);
        series2.setColor(Color.argb(255, 255, 60, 60));
        series2.setBackgroundColor(Color.argb(100, 204, 119, 119));
        series2.setDrawDataPoints(true);
        graph.addSeries(series2);

        // legend
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }
}
