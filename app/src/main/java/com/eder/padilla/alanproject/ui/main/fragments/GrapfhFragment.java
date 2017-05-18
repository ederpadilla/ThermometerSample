package com.eder.padilla.alanproject.ui.main.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.model.GraphDataPoint;
import com.eder.padilla.alanproject.ui.main.Main2Activity;
import com.eder.padilla.alanproject.util.Util;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ederpadilla on 02/05/17.
 */

public class GrapfhFragment extends Fragment {

    //@BindView(R.id.calendarView)
    //MaterialCalendarView mMaterialCalendarView;

    @BindView(R.id.graph)
    public GraphView mGraphView;

    @BindView(R.id.next_fragment_graph)
    TextView mTvNextFragment;

    List<Double> doubleList = new ArrayList<>();

    @OnClick(R.id.next_fragment_graph)
    public void changeFragment(){
        ((Main2Activity)getActivity()).viewPager.setCurrentItem(2);
    }

    public String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_graph,container,false);
        ButterKnife.bind(this,view);
        Realm.init(getActivity());
        mGraphView.addSeries(Main2Activity.series);
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DAY_OF_MONTH)+" "+TemperatureFragment.getMonthInString(calendar.get(Calendar.MONTH))+" "+calendar.get(Calendar.YEAR);
        Main2Activity.series.setOnDataPointTapListener((series, dataPoint) -> showDialogPoint(series,dataPoint));
        initGraph(mGraphView);
        getDataPointsInRealm();
        return view;
    }

    private void getDataPointsInRealm(){
            Realm realm = Realm.getDefaultInstance();
            RealmResults<GraphDataPoint> graphDataPoints = realm.where(GraphDataPoint.class).findAll();
            Util.log("Se tiene en realm "+graphDataPoints.toString());
            if (notEmpty(graphDataPoints)){
                Util.log("entra a 1");
                for (GraphDataPoint graphDataPoint : graphDataPoints){
                    Util.log("entra a 2");
                    if (graphDataPoint.getmDate().equals(date)){
                        Util.log("entra a 3");
                        doubleList.clear();
                        for (String number : graphDataPoint.getmDataPoint().split(",")){
                            doubleList.add(Double.parseDouble(number));
                            Util.log("entra a 4");
                        }
                        Util.log("entra a 5");
                        DataPoint dataPoint = new DataPoint(doubleList.get(0),doubleList.get(1));
                        Main2Activity.series.appendData(dataPoint,true,40);
                        Main2Activity.series.setAnimated(true);
                    }
                }
            }

    }

    @Override
    public void onResume() {
        //getDataPointsInRealm();
        super.onResume();
    }



    private  void showDialogPoint(Series series, DataPointInterface dataPoint) {
        new MaterialDialog.Builder(getActivity())
                .title("Temperatura medida a las "+String.valueOf(dataPoint.getX()).replace(".",":")+" hrs")
                .content("Fue de "+String.valueOf(dataPoint.getY())+" grados celcius.")
                .positiveText("Aceptar")
                .cancelable(false)
                .onPositive((dialog, which) -> dialog.dismiss())
                .positiveColorRes(R.color.colorPrimaryDark)
                .titleColorRes(R.color.colorPrimary)
                .show();
    }

    public static GrapfhFragment newInstance() {
        GrapfhFragment grapfhFragment = new GrapfhFragment();
        return grapfhFragment;
    }

    public void initGraph(GraphView graph) {


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
    public boolean notEmpty(RealmResults a) {
        return !a.isEmpty();
    }
}
