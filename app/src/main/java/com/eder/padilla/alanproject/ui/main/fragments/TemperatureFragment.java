package com.eder.padilla.alanproject.ui.main.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eder.padilla.alanproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by ederpadilla on 02/05/17.
 */

public class TemperatureFragment extends Fragment {

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
