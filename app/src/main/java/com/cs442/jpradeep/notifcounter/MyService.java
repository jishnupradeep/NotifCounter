package com.cs442.jpradeep.notifcounter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;
import android.os.Handler;
import android.util.Log;

public class MyService extends Service{

    private static final String BUTTON_CLICK_ACTION = "NotifCounter";
    private static final int NOTIFICATION_REF = 1;
    NotificationManager notificationManager;           //Initializing the notification manager

    //Initializing the variables and threads
    Handler m_handler;
    Runnable m_handlerTask ;
    int count=0;
    int timing=0;
    String string;
    String svcName = Context.NOTIFICATION_SERVICE;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }



    //Starting the Service
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String ed = intent.getExtras().getString("ed");
        count=Integer.parseInt(ed);

        //Setting up the notification
        notificationManager = (NotificationManager)getSystemService(svcName);

        //Toast notification when counter starts
        Toast.makeText(this, "Counter has started", Toast.LENGTH_LONG).show();

        //Handler to perform counting function and trigger notification
        m_handler = new Handler();
        m_handlerTask = new Runnable()
        {//The timer has been created
            @Override
            public void run() {
                count++; //This is the value of counter
                timing++; //This value helps in counting 10 seconds to display notification
                if (timing%10==0)
                {        triggerNotification(customLayoutNotification(),2);
                }
                m_handler.postDelayed(m_handlerTask, 1000);
                string=String.valueOf(count);
                Log.d("Value of Counter ",string);
            }
        };
        m_handlerTask.run(); //Counter runs on a thread
        return START_REDELIVER_INTENT; //So that service intent is redelivered (Similar to START_STICKY)
    }

    //The service is destroyed here
    @Override
    public void onDestroy() {
        super.onDestroy();
        m_handler.removeCallbacks(m_handlerTask);
        cancelNotification(2);
        Toast.makeText(this, "Counter has stopped", Toast.LENGTH_LONG).show();
    }

    //The notification layout is mentioned here
    private Notification.Builder customLayoutNotification() {
        Notification.Builder builder = new Notification.Builder(MyService.this);

        Intent newIntent = new Intent(BUTTON_CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MyService.this, 2, newIntent, 0);
        Bitmap myIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder.setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Notification")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("NotifCounter")
                .setContentText("NotifCounter has counted till Number: "+count)
                .setLights(Color.RED, 3000, 3000)
                .setContentInfo("Info")
                .setLargeIcon(myIconBitmap)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(alarmSound)
                .setContentIntent(pendingIntent);
        return builder;
    }


    //Notification is triggered here
    private void triggerNotification(Notification.Builder builder,int ref) {
        String svc = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager)getSystemService(svc);
        int NOTIFICATION_REF = 1;
        Notification notification = builder.getNotification();
        notificationManager.notify(ref, notification);
    }


    //Notification is cancelled from here
    private void cancelNotification(int ref) {
        notificationManager.cancel(ref);

    }

}