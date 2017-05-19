package com.eder.padilla.alanproject.ui.main.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.model.GraphDataPoint;
import com.eder.padilla.alanproject.model.Registro;
import com.eder.padilla.alanproject.ui.main.Main2Activity;
import com.eder.padilla.alanproject.util.ArtikCloudSession;
import com.eder.padilla.alanproject.util.Constants;
import com.eder.padilla.alanproject.util.SendService;
import com.eder.padilla.alanproject.util.Util;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    public static String mDate="12";

    public String mHour="";

    public Double mTemperature=0.0;

    public double counter=0;

    List<Double> cordinates = new ArrayList<>();


    String mTempLevel;

    @OnClick(R.id.next_fragment_temperature)
    public void changeFragment(){
        ((Main2Activity)getActivity()).viewPager.setCurrentItem(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_temperature,container,false);
        ButterKnife.bind(this,view);
        Realm.init(getActivity());
        Realm realm = Realm.getDefaultInstance();
        Registro registro = realm.where(Registro.class).findFirst();
        if (registro==null){
        counter=0;
        if (mTemperature<1){
        }else{
            if (String.valueOf(mTemperature).length()<3){
                mTvTemperature.setText(String.valueOf(mTemperature).substring(0,2)+"ºC");
            }else if (String.valueOf(mTemperature).length()<4){
                mTvTemperature.setText(String.valueOf(mTemperature).substring(0,3)+"ºC");
            }else if (String.valueOf(mTemperature).length()<5){
                mTvTemperature.setText(String.valueOf(mTemperature).substring(0,4)+"ºC");
            }else{
                mTvTemperature.setText(String.valueOf(mTemperature).substring(0,4)+"ºC");
            }
            if (mTemperature >= Constants.MAXIMUM_TEMPERATURE || mTemperature<=Constants.MINIMUM_TEMPERATURE){
                mImageCalm.setVisibility(View.INVISIBLE);
                mImageAlarm.setVisibility(View.VISIBLE);
            }else{
                mImageCalm.setVisibility(View.VISIBLE);
                mImageAlarm.setVisibility(View.INVISIBLE);
            }
        }
        mTvdate.setText(mDate);
        mTvHour.setText(mHour+" hrs");
        }else{
            double tempinrealm;
            Util.log("Se ecnontro "+registro.toString());
            mTvTemperature.setText(registro.getTemperature());
            mTvdate.setText(registro.getDate());
            mTvHour.setText(registro.getHour());
            if (registro.getTemperature().length()<3){
                tempinrealm = Double.parseDouble(registro.getTemperature().substring(0,2));
            }else if(registro.getTemperature().length()<4){
                tempinrealm = Double.parseDouble(registro.getTemperature().substring(0,3));
            }else{
                tempinrealm = Double.parseDouble(registro.getTemperature().substring(0,3));
            }
            if (tempinrealm>=Constants.MAXIMUM_TEMPERATURE || mTemperature<=Constants.MINIMUM_TEMPERATURE){
                mImageCalm.setVisibility(View.INVISIBLE);
                mImageAlarm.setVisibility(View.VISIBLE);
            }else{
                mImageCalm.setVisibility(View.VISIBLE);
                mImageAlarm.setVisibility(View.INVISIBLE);
            }
            ((Main2Activity)getActivity()).materialDialog.dismiss();
        }

        setUpArticCloud();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mWSUpdateReceiver,
                makeWebsocketUpdateIntentFilter());
        Util.log("Connecting to /live");
        ArtikCloudSession.getInstance().connectFirehoseWS();//non blocking
        return view;
    }
    private void setUpArticCloud() {
        ArtikCloudSession.getInstance().setContext(getActivity());
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
                ((Main2Activity)getActivity()).materialDialog.dismiss();
            }
        }
    };

    private void wrongComunication(MaterialDialog dialog) {
        dialog.dismiss();
    }

    private void displayLiveStatus(String status) {
        Util.log("status "+status);
    }

    private void displayDeviceStatus(String status, String updateTimems) {
        Util.log("Aqui solo imprime el status del fragment"+status);
        try {
            JSONObject jsonObj = new JSONObject(status);
            mTemperature = (Double) jsonObj.get("temp");
            cordinates.clear();
                if (String.valueOf(mTemperature).length()<3){
                    mTvTemperature.setText(String.valueOf(mTemperature).substring(0,2)+"ºC");
                    mTempLevel=String.valueOf(mTemperature).substring(0,2)+"ºC";
                }else if (String.valueOf(mTemperature).length()<4){
                    mTvTemperature.setText(String.valueOf(mTemperature).substring(0,3)+"ºC");
                    mTempLevel=String.valueOf(mTemperature).substring(0,3)+"ºC";
                }else if (String.valueOf(mTemperature).length()<5){
                    mTvTemperature.setText(String.valueOf(mTemperature).substring(0,4)+"ºC");
                    mTempLevel=String.valueOf(mTemperature).substring(0,4)+"ºC";
                }else{
                    mTvTemperature.setText(String.valueOf(mTemperature).substring(0,4)+"ºC");
                    mTempLevel=String.valueOf(mTemperature).substring(0,4)+"ºC";
                }
            if (counter!=mTemperature) {
                counter = mTemperature;
                if (mTemperature >= Constants.MAXIMUM_TEMPERATURE || mTemperature<=Constants.MINIMUM_TEMPERATURE) {
                    Util.log("El contador es " + counter);
                    sendNotification();
                    mImageCalm.setVisibility(View.INVISIBLE);
                    mImageAlarm.setVisibility(View.VISIBLE);
                } else {
                    mImageCalm.setVisibility(View.VISIBLE);
                    mImageAlarm.setVisibility(View.INVISIBLE);
                }
                Date date = new Date(Long.parseLong(updateTimems));
                Calendar calendar = toCalendar(date);
                mDate = calendar.get(Calendar.DAY_OF_MONTH)+" "+getMonthInString(calendar.get(Calendar.MONTH))+" "+calendar.get(Calendar.YEAR);
                if (calendar.get(Calendar.MINUTE)>=10){
                    mHour= calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
                }else{
                    mHour= calendar.get(Calendar.HOUR_OF_DAY)+":"+"0"+calendar.get(Calendar.MINUTE);
                }
                mTvdate.setText(mDate);
                Main2Activity.series.setTitle(mDate);
                String hourWithOutSpaces = mHour.trim().replace(" ","");
                String xAxisSeries = hourWithOutSpaces.replace(":",".");
                String dataPointString = xAxisSeries+","+mTemperature;
                Realm realm = Realm.getDefaultInstance();
                GraphDataPoint graphDataPoint = new GraphDataPoint();
                if (realm.where(GraphDataPoint.class).findAll().isEmpty()){
                    graphDataPoint.setId(0);
                }else{
                    graphDataPoint.setId(realm.where(GraphDataPoint.class).findAll().size()+1);
                }
                graphDataPoint.setmDataPoint(dataPointString);
                graphDataPoint.setmDate(mDate);
                graphDataPoint.setmHour(mHour);
                realm.executeTransactionAsync(realm1 -> realm1.copyToRealm(graphDataPoint));
                Util.log("Se manda "+dataPointString);
                for (String number : dataPointString.split(",")){
                    cordinates.add(Double.parseDouble(number));
                }
                DataPoint dataPoint = new DataPoint(cordinates.get(0),cordinates.get(1));
                Main2Activity.series.appendData(dataPoint,true,40);
                Main2Activity.series.setAnimated(true);
                if (Main2Activity.series.getTitle().equals(mDate)){
                }else{
                    Main2Activity.series.resetData(new DataPoint[0]);
                    Main2Activity.series.setTitle(mDate);
                }
                mTvHour.setText(mHour+" hrs");
                Realm realm2 = Realm.getDefaultInstance();
                Registro registro = new Registro("0",mTempLevel,mDate,mHour);
                realm2.beginTransaction();
                realm2.copyToRealmOrUpdate(registro);
                realm2.commitTransaction();
                ((Main2Activity)getActivity()).materialDialog.dismiss();
                long time_ms = Long.parseLong(updateTimems);
                Util.log(DateFormat.getDateTimeInstance().format(new Date(time_ms)));
            }else{

         }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendNotification() {
        NotificationManager nm = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getActivity());
        Intent notificationIntent = new Intent(getActivity(), Main2Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.firewall);
        builder.setContentText(getString(R.string.high_temperature_levels));
        builder.setContentTitle(getString(R.string.atention)+" "+mTemperature+"ºC");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        nm.notify((int)System.currentTimeMillis(),notification);
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

    public static String getMonthInString(int month){
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

    @Override
    public void onDestroyView() {
        Util.log("Entra a on destroyu view");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mWSUpdateReceiver);
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        Util.log("Entra a on pause view");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mWSUpdateReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        Util.log("Entra al onresume");
        Realm realm = Realm.getDefaultInstance();
        Registro registro = realm.where(Registro.class).findFirst();
        if (registro==null){
            if (mTemperature<1){
            }else{
                if (String.valueOf(mTemperature).length()<3){
                    mTvTemperature.setText(String.valueOf(mTemperature).substring(0,2)+"ºC");
                }else if (String.valueOf(mTemperature).length()<4){
                    mTvTemperature.setText(String.valueOf(mTemperature).substring(0,3)+"ºC");
                }else if (String.valueOf(mTemperature).length()<5){
                    mTvTemperature.setText(String.valueOf(mTemperature).substring(0,4)+"ºC");
                }else{
                    mTvTemperature.setText(String.valueOf(mTemperature).substring(0,4)+"ºC");
                }
                if (mTemperature>=Constants.MAXIMUM_TEMPERATURE || mTemperature<=Constants.MINIMUM_TEMPERATURE){
                    mImageCalm.setVisibility(View.INVISIBLE);
                    mImageAlarm.setVisibility(View.VISIBLE);
                }else{
                    mImageCalm.setVisibility(View.VISIBLE);
                    mImageAlarm.setVisibility(View.INVISIBLE);
                }
            }
            mTvdate.setText(mDate);
            mTvHour.setText(mHour+" hrs");
        }else{
            double tempinrealm;
            Util.log("Se ecnontro "+registro.toString());
            mTvTemperature.setText(registro.getTemperature());
            mTvdate.setText(registro.getDate());
            mTvHour.setText(registro.getHour());
            if (registro.getTemperature().length()<3){
                tempinrealm = Double.parseDouble(registro.getTemperature().substring(0,2));
            }else if(registro.getTemperature().length()<4){
                tempinrealm = Double.parseDouble(registro.getTemperature().substring(0,3));
            }else{
                tempinrealm = Double.parseDouble(registro.getTemperature().substring(0,3));
            }
            if (tempinrealm>=Constants.MAXIMUM_TEMPERATURE || mTemperature<=Constants.MINIMUM_TEMPERATURE){
                mImageCalm.setVisibility(View.INVISIBLE);
                mImageAlarm.setVisibility(View.VISIBLE);
            }else{
                mImageCalm.setVisibility(View.VISIBLE);
                mImageAlarm.setVisibility(View.INVISIBLE);
            }
        }
        setUpArticCloud();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mWSUpdateReceiver,
                makeWebsocketUpdateIntentFilter());
        Util.log("Connecting to /live");
        ArtikCloudSession.getInstance().connectFirehoseWS();//non blocking
        super.onResume();
    }
}
