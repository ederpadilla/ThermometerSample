package com.eder.padilla.alanproject.util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.model.Registro;
import com.eder.padilla.alanproject.ui.main.Main2Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by ederpadilla on 21/03/17.
 */

public class SendService  extends Service {

    public String mDate="";

    public String mHour="";

    public Double mTemperature=0.0;

    public double counter=0;

    String mTempLevel;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setUpArticCloud();
        LocalBroadcastManager.getInstance(this).registerReceiver(mWSUpdateReceiver,
                makeWebsocketUpdateIntentFilter());
        Util.log("Connecting to /live");
        ArtikCloudSession.getInstance().connectFirehoseWS();//non blocking
        return super.onStartCommand(intent, flags, startId);
    }
    private static IntentFilter makeWebsocketUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONOPEN);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONMSG);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONCLOSE);
        intentFilter.addAction(ArtikCloudSession.WEBSOCKET_LIVE_ONERROR);
        return intentFilter;
    }
    private void setUpArticCloud() {
        ArtikCloudSession.getInstance().setContext(this);
    }
    private final BroadcastReceiver mWSUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ArtikCloudSession.WEBSOCKET_LIVE_ONOPEN.equals(action)) {
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
    private void displayDeviceStatus(String status, String updateTimems) {
        Util.log("Aqui solo imprime el status del seervicio "+status);
        Date date = new Date(Long.parseLong(updateTimems));
        Calendar calendar = toCalendar(date);
        mDate = calendar.get(Calendar.DAY_OF_MONTH)+" "+getMonthInString(calendar.get(Calendar.MONTH))+" "+calendar.get(Calendar.YEAR);
        mHour = calendar.get(Calendar.HOUR_OF_DAY)+" : "+calendar.get(Calendar.MINUTE)+" hrs";
        try {
            JSONObject jsonObj = new JSONObject(status);
            mTemperature = (Double) jsonObj.get("temp");
            if (String.valueOf(mTemperature).length()<3){
                mTempLevel=String.valueOf(mTemperature).substring(0,2)+"ºC";
            }else if (String.valueOf(mTemperature).length()<4){
                mTempLevel=String.valueOf(mTemperature).substring(0,3)+"ºC";
            }else if (String.valueOf(mTemperature).length()<5){
                mTempLevel=String.valueOf(mTemperature).substring(0,4)+"ºC";
            }else{
                mTempLevel=String.valueOf(mTemperature).substring(0,4)+"ºC";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Util.log("el contador es "+counter+" el valor de temperature " +mTemperature);
        if (counter!=mTemperature){
            counter=mTemperature;
            if (counter>=Constants.MAXIMUM_TEMPERATURE){
                if (String.valueOf(mTemperature).length()<3){
                    mTempLevel=String.valueOf(mTemperature).substring(0,2)+"ºC";
                }else if (String.valueOf(mTemperature).length()<4){
                    mTempLevel=String.valueOf(mTemperature).substring(0,3)+"ºC";
                }else if (String.valueOf(mTemperature).length()<5){
                    mTempLevel=String.valueOf(mTemperature).substring(0,4)+"ºC";
                }else{
                    mTempLevel=String.valueOf(mTemperature).substring(0,4)+"ºC";
                }
                sendNotification();

            }
        }else{

        }

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        Registro registro = new Registro("0",mTempLevel,mDate,mHour);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(registro);
        realm.commitTransaction();
        Util.log("Se supone se crea "+registro.toString());
        long time_ms = Long.parseLong(updateTimems);
        Util.log(DateFormat.getDateTimeInstance().format(new Date(time_ms)));
    }
    private void sendNotification(){
        Util.log("Entra a la notificacion y el contador es "+counter);
        //NotificationManager nm = (NotificationManager)SendService.this.getSystemService(NOTIFICATION_SERVICE);
        //Notification.Builder builder = new Notification.Builder(SendService.this);
        //Intent notificationIntent = new Intent(getApplicationContext(), Main2Activity.class);
        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.setContentIntent(contentIntent);
        //builder.setSmallIcon(R.mipmap.firewall);
        //builder.setContentText(getString(R.string.high_temperature_levels));
        //builder.setContentTitle(getString(R.string.atention)+" "+mTempLevel);
        //builder.setAutoCancel(true);
        //builder.setDefaults(Notification.DEFAULT_ALL);
        //Notification notification = builder.build();
        //nm.notify((int)System.currentTimeMillis(),notification);
        Intent intent = new Intent(this, Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.firewall)
                .setContentTitle(getString(R.string.atention)+" "+mTempLevel)
                .setContentText(getString(R.string.high_temperature_levels))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mWSUpdateReceiver);
        Util.log("Entra al destroy del servicio");
        super.onDestroy();
    }


}