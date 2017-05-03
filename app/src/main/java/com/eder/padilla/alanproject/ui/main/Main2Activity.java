package com.eder.padilla.alanproject.ui.main;

import android.animation.ArgbEvaluator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.adapter.ViewPagerAdapter;
import com.eder.padilla.alanproject.util.ArtikCloudSession;
import com.eder.padilla.alanproject.util.Constants;
import com.eder.padilla.alanproject.util.Util;
import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;

    ViewPagerAdapter viewPagerAdapter;

    int currentPositionViewer = Constants.ZERO;

    @BindColor(R.color.colorone)
    int colorone;

    @BindColor(R.color.colortwo)
    int colortwo;

    @BindColor(R.color.colorthree)
    int colorthree;

    Integer[] colors = null;

    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        initViewPager();
    }

    private void initViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        pageIndicatorView.setViewPager(viewPager);
        pageIndicatorView.setAnimationType(AnimationType.WORM);
        viewPager.addOnPageChangeListener(this);
        viewPager.addOnPageChangeListener(new CustomOnPageChangeListener());
        setUpColors();
        setUpArticCloud();
    }

    private void setUpArticCloud() {
        ArtikCloudSession.getInstance().setContext(this);
        Util.log("Devide id y device name "+ArtikCloudSession.getInstance().getDeviceID()+" Device name "+ArtikCloudSession.getInstance().getDeviceName());
    }

    @Override
    public void onBackPressed() {
        switch (currentPositionViewer){
            case 0:
                finish();
                super.onBackPressed();
                break;
            case 1:
                viewPager.setCurrentItem(0);
                break;
            case 2:
                viewPager.setCurrentItem(1);
                break;
        }
    }
    private void setUpColors(){
        Integer color1 = colorone;
        Integer color2 = colortwo;
        Integer color3 = colorthree;
        Integer[] colors_temp = {color1, color2, color3};
        colors = colors_temp;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPositionViewer = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private class CustomOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if(position < (viewPagerAdapter.getCount() -1) && position < (colors.length - 1)) {

                viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));

            } else {

                // the last page color
                viewPager.setBackgroundColor(colors[colors.length - 1]);

            }

        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mWSUpdateReceiver,
                makeWebsocketUpdateIntentFilter());
        Util.log("Connecting to /live");
        ArtikCloudSession.getInstance().connectFirehoseWS();//non blocking
    }
    private final BroadcastReceiver mWSUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ArtikCloudSession.WEBSOCKET_LIVE_ONOPEN.equals(action)) {
                Util.log("WebSocket /live connected");
            } else if (ArtikCloudSession.WEBSOCKET_LIVE_ONMSG.equals(action)) {
                String status = intent.getStringExtra(ArtikCloudSession.DEVICE_DATA);
                String updateTime = intent.getStringExtra(ArtikCloudSession.TIMESTEP);
                displayDeviceStatus(status, updateTime);
            } else if (ArtikCloudSession.WEBSOCKET_LIVE_ONCLOSE.equals(action) ||
                    ArtikCloudSession.WEBSOCKET_LIVE_ONERROR.equals(action)) {
                displayLiveStatus(intent.getStringExtra("error"));
            }
        }
    };

    private void displayLiveStatus(String status) {
        Util.log("status "+status);
    }

    private void displayDeviceStatus(String status, String updateTimems) {
        Util.log(status);
        long time_ms = Long.parseLong(updateTimems);
        Util.log(DateFormat.getDateTimeInstance().format(new Date(time_ms)));
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mWSUpdateReceiver);
        ArtikCloudSession.getInstance().disconnectFirehoseWS(); //non blocking
    }


    private static IntentFilter makeWebsocketUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONOPEN);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONMSG);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONCLOSE);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONERROR);
        return intentFilter;
    }
}
