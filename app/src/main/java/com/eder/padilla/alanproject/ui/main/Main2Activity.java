package com.eder.padilla.alanproject.ui.main;

import android.animation.ArgbEvaluator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.adapter.ViewPagerAdapter;
import com.eder.padilla.alanproject.util.Constants;
import com.eder.padilla.alanproject.util.DialogManager;
import com.eder.padilla.alanproject.util.NonSwipeableViewPager;
import com.eder.padilla.alanproject.util.SendService;
import com.eder.padilla.alanproject.util.Util;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.viewpager)
    public NonSwipeableViewPager viewPager;

    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;

    ViewPagerAdapter viewPagerAdapter;

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

    public static LineGraphSeries<DataPoint> series;

    public static String mDateToday;

    int currentPositionViewer = Constants.ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        Intent intent = new Intent();
        intent.setClass(Main2Activity.this, SendService.class);
        stopService(intent);
        initSeries();
            materialDialog = DialogManager.showProgressDialog(Main2Activity.this);
            initViewPager();
        if (Util.isOnline(getApplicationContext())){}else{
            new MaterialDialog.Builder(Main2Activity.this)
                    .title("Upps..")
                    .content("No hay respuesta del servidor, intente mas tarde.")
                    .positiveText("Aceptar")
                    .cancelable(false)
                    .onPositive((dialog, which) -> dialog.dismiss())
                    .positiveColorRes(R.color.colorPrimaryDark)
                    .titleColorRes(R.color.colorPrimary)
                    .show();
        }
    }

    private void initSeries() {
        series = new LineGraphSeries<>();
        series.setTitle(getString(R.string.temperature));
        series.setDrawBackground(true);
        series.setColor(Color.argb(255, 114, 153, 251));
        series.setBackgroundColor(Color.argb(80, 114, 153, 251));
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(20);
        series.setThickness(10);
        series.setDrawDataPoints(true);
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

    @Override
    protected void onDestroy() {
        Util.log("Entra a ondestry!!!");
        if (!isMyServiceRunning(getApplicationContext(), SendService.class)) {
            Intent serviceIntent = new Intent(getApplicationContext(), SendService.class);
            getApplicationContext().startService(serviceIntent);
        } else {
            Log.i("MyLog","Service is alredy Running. ");
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (!isMyServiceRunning(getApplicationContext(), SendService.class)) {
            Intent serviceIntent = new Intent(getApplicationContext(), SendService.class);
            getApplicationContext().startService(serviceIntent);
        } else {
            Log.i("MyLog","Service is alredy Running. ");
        }
        Util.log("Entra a pause!!!");
        super.onPause();
    }

    private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
