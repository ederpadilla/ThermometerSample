package com.eder.padilla.alanproject.util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.ui.main.Main2Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ederpadilla on 21/03/17.
 */

public class SendService  extends Service {


    public String mDate="";

    public String mHour="";

    public Double mTemperature=0.0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Entra a OnStartComand","hola mundo desde onstart comand");
        LocalBroadcastManager.getInstance(SendService.this).registerReceiver(mWSUpdateReceiver,
                makeWebsocketUpdateIntentFilter());
        Util.log("Connecting to /live");
        ArtikCloudSession.getInstance().connectFirehoseWS();//non blocking
        NotificationManager nm = (NotificationManager)SendService.this.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(SendService.this);
        Intent notificationIntent = new Intent(SendService.this, Main2Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(SendService.this,0,notificationIntent,0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.firewall);
        builder.setContentText("Se registraron altos niveles de temperatura");
        builder.setContentTitle("¡PRECAUCION!");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        nm.notify((int)System.currentTimeMillis(),notification);
        return super.onStartCommand(intent, flags, startId);
    }

    private void setUp(){

    }
    private void setUpArticCloud() {
        ArtikCloudSession.getInstance().setContext(SendService.this);
        Util.log("Devide id y device name "+ArtikCloudSession.getInstance().getDeviceID()+" Device name "+ArtikCloudSession.getInstance().getDeviceName());
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
            if (mTemperature>=15){
                //mImageCalm.setVisibility(View.INVISIBLE);
                //mImageAlarm.setVisibility(View.VISIBLE);
            }else{
                //mImageCalm.setVisibility(View.VISIBLE);
                //mImageAlarm.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Date date = new Date(Long.parseLong(updateTimems));
        Calendar calendar = toCalendar(date);
        mDate = calendar.get(Calendar.DAY_OF_MONTH)+" "+getMonthInString(calendar.get(Calendar.MONTH))+" "+calendar.get(Calendar.YEAR);
        mHour = calendar.get(Calendar.HOUR_OF_DAY)+" : "+calendar.get(Calendar.MINUTE);
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