package com.eder.padilla.alanproject.ui.main;

import android.animation.ArgbEvaluator;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.adapter.ViewPagerAdapter;
import com.eder.padilla.alanproject.util.Constants;
import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;

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
}
