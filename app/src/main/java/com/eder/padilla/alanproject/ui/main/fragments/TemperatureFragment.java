package com.eder.padilla.alanproject.ui.main.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eder.padilla.alanproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by ederpadilla on 02/05/17.
 */

public class TemperatureFragment extends Fragment {

    @BindView(R.id.img_calm)
    ImageView mImageCalm;

    @BindView(R.id.tv_temperature)
    TextView mTvTemperature;

    @BindView(R.id.img_alarm)
    ImageView mImageAlarm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_temperature,container,false);
        ButterKnife.bind(this,view);
        Realm.init(getActivity());
        return view;
    }

    public static TemperatureFragment newInstance() {
         TemperatureFragment temperatureFragment = new TemperatureFragment();
        return temperatureFragment;
    }


}
