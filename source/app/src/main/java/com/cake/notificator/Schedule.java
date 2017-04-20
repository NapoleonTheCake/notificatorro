package com.cake.notificator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

/**
 * Created by cake on 20.04.17.
 */

public class Schedule extends BroadcastReceiver {

    Notification.Builder notificationBuilder;
    int delayMinutes;
    String bigText;
    int NOTIFY_ID;

    public Schedule(Notification.Builder notificationBuilderIn, int delayMinutesIn, String bigTextIn, int NOTIFY_ID_IN) {
        notificationBuilder = notificationBuilderIn;
        delayMinutes = delayMinutesIn;
        bigText = bigTextIn;
    }

    public Schedule() {
        return;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "WOW", Toast.LENGTH_LONG).show();


        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();

        //next is notification code.
//        notificationBuilder.setWhen(System.currentTimeMillis() * 1000 * 60 * delayMinutes - 60000);

        notificationBuilder.setWhen(System.currentTimeMillis());

        Notification notification = new Notification.BigTextStyle(notificationBuilder).bigText(bigText)
                .build();
        
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);

        wakeLock.release();
    }

    public void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Schedule.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);
    }
}
