package com.eder.padilla.alanproject.util;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


/**
 * Created by ederpadilla on 21/03/17.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (networkInfo != null) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            Log.i("MyLog","ssid " + ssid);
            Log.i("MyLog",wifiInfo.getIpAddress() + "");
//            Log.i("myLog", wifiInfo.);
        }
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.i("MyLog","Have Wifi Connection");
            if (!isMyServiceRunning(context, SendService.class)) {
                Intent serviceIntent = new Intent(context, SendService.class);
                context.startService(serviceIntent);
            } else {
                Log.i("MyLog","Service is alredy Running. ");
            }
        } else  if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            Log.i("MyLog","Have Wifi Connection");
            if (!isMyServiceRunning(context, SendService.class)) {
                Intent serviceIntent = new Intent(context, SendService.class);
                context.startService(serviceIntent);
            } else {
                Log.i("MyLog","Service is alredy Running. ");
            }
        }else
            Log.i("MyLog","Don't have Wifi Connection");

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