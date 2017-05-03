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

import com.afollestad.materialdialogs.MaterialDialog;
import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.adapter.ViewPagerAdapter;
import com.eder.padilla.alanproject.ui.main.fragments.TemperatureFragment;
import com.eder.padilla.alanproject.util.ArtikCloudSession;
import com.eder.padilla.alanproject.util.Constants;
import com.eder.padilla.alanproject.util.DialogManager;
import com.eder.padilla.alanproject.util.Util;
import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
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

    public String mDate="";

    public String mHour="";

    public String mTemperature="";

    Integer[] colors = null;

    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    public MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        materialDialog = DialogManager.showProgressDialog(Main2Activity.this);
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
      // LocalBroadcastManager.getInstance(this).registerReceiver(mWSUpdateReceiver,
      //         makeWebsocketUpdateIntentFilter());
      // Util.log("Connecting to /live");
      // ArtikCloudSession.getInstance().connectFirehoseWS();//non blocking
    }

}
