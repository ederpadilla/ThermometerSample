package com.eder.padilla.alanproject.ui.main.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.ui.main.Main2Activity;
import com.eder.padilla.alanproject.util.ArtikCloudSession;
import com.eder.padilla.alanproject.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

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
    public TextView mTvTemperature;

    @BindView(R.id.img_alarm)
    ImageView mImageAlarm;

    @BindView(R.id.tv_hour)
    public TextView mTvHour;

    @BindView(R.id.tv_date)
    public TextView mTvdate;

    public String mDate="";

    public String mHour="";

    public Double mTemperature=0.0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_temperature,container,false);
        ButterKnife.bind(this,view);
        Realm.init(getActivity());
        Util.log("el valor de mTemp es "+mTemperature);
        if (mTemperature<1){
        }else{
            mTvTemperature.setText(String.valueOf(mTemperature).substring(0,4));
            if (mTemperature>=15){
                mImageCalm.setVisibility(View.INVISIBLE);
                mImageAlarm.setVisibility(View.VISIBLE);
            }else{
                mImageCalm.setVisibility(View.VISIBLE);
                mImageAlarm.setVisibility(View.INVISIBLE);
            }
        }
        mTvdate.setText(mDate);
        mTvHour.setText(mHour+" hrs");
        setUpArticCloud();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mWSUpdateReceiver,
                makeWebsocketUpdateIntentFilter());
        Util.log("Connecting to /live");
        ArtikCloudSession.getInstance().connectFirehoseWS();//non blocking
        return view;
    }
    private void setUpArticCloud() {
        ArtikCloudSession.getInstance().setContext(getActivity());
        Util.log("Devide id y device name "+ArtikCloudSession.getInstance().getDeviceID()+" Device name "+ArtikCloudSession.getInstance().getDeviceName());
    }
    public static TemperatureFragment newInstance() {
         TemperatureFragment temperatureFragment = new TemperatureFragment();
        return temperatureFragment;
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
        Util.log("Aqui solo imprime el status "+status);
        try {
            JSONObject jsonObj = new JSONObject(status);
            mTemperature = (Double) jsonObj.get("temp");
            mTvTemperature.setText(String.valueOf(mTemperature).substring(0,4)+"ºC");
            if (mTemperature>=15){
                mImageCalm.setVisibility(View.INVISIBLE);
                mImageAlarm.setVisibility(View.VISIBLE);
            }else{
                mImageCalm.setVisibility(View.VISIBLE);
                mImageAlarm.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Date date = new Date(Long.parseLong(updateTimems));
        Calendar calendar = toCalendar(date);
        mDate = calendar.get(Calendar.DAY_OF_MONTH)+" "+getMonthInString(calendar.get(Calendar.MONTH))+" "+calendar.get(Calendar.YEAR);
        mHour = calendar.get(Calendar.HOUR_OF_DAY)+" : "+calendar.get(Calendar.MINUTE);
        mTvdate.setText(mDate);
        mTvHour.setText(mHour+" hrs");
        ((Main2Activity)getActivity()).materialDialog.dismiss();
        long time_ms = Long.parseLong(updateTimems);
        Util.log(DateFormat.getDateTimeInstance().format(new Date(time_ms)));
    }


    private static IntentFilter makeWebsocketUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONOPEN);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONMSG);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONCLOSE);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONERROR);
        return intentFilter;
    }
    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public String getMonthInString(int month){
        switch (month){
            case 0:
                return "Enero";
            case 1:
                return "Febrero";
            case 2:
                return "Marzo";
            case 3:
                return "Abril";
            case 4:
                return "Mayo";
            case 5:
                return "Junio";
            case 6:
                return "Julio";
            case 7:
                return "Agosto";
            case 8:
                return "Septiembre";
            case 9:
                return "Octubre";
            case 10:
                return "Noviembre";
            case 11:
                return "Diciembre";
        }
        return "";
    }

}
