package com.eder.padilla.alanproject.util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * Created by ederpadilla on 21/03/17.
 */

public class SendService  extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Entra a OnStartComand","hola mundo desde onstart comand");
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendAnswers(){
        /**
         gNotificationManager nm = (NotificationManager)SendService.this.getSystemService(NOTIFICATION_SERVICE);
         Notification.Builder builder = new Notification.Builder(SendService.this);
         Intent notificationIntent = new Intent(SendService.this, MainQuizActivity.class);
         PendingIntent contentIntent = PendingIntent.getActivity(SendService.this,0,notificationIntent,0);
         builder.setContentIntent(contentIntent);
         builder.setSmallIcon(R.mipmap.ic_launcher);
         builder.setContentText("Se enviaron correctamente");
         builder.setContentTitle("Las encuestas pendientes");
         builder.setAutoCancel(true);
         builder.setDefaults(Notification.DEFAULT_ALL);
         Notification notification = builder.build();
         nm.notify((int)System.currentTimeMillis(),notification);*/
    }

}