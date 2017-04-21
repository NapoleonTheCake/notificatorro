package com.cake.notificator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.PowerManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

/**
 * Created by cake on 20.04.17.
 */

public class Schedule extends BroadcastReceiver {

    public Schedule() {
        return;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();

        //next is notification code. //

        //get res.
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        String titleText = mPrefs.getString("titleText", "");
        String bigText = mPrefs.getString("bigText", "");
        int NOTIFY_ID = mPrefs.getInt("id", 0);

        //create intent.
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //get res.
        Resources res = context.getResources();

        //build notification.
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.statusbaricon)
                .setAutoCancel(true)
                .setContentTitle(titleText)
                .setContentText(bigText);


        //check vibration.
        if (mPrefs.getBoolean("vibration", true)) {
            builder.setVibrate(new long[] { 0, 50 });
        }

        //create default title if empty.
        if (titleText.length() == 0) {
            builder.setContentTitle(context.getString(R.string.notification_Title_Default));
        }

        //show notification. check for delay.
        builder.setWhen(System.currentTimeMillis());

        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(bigText).build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);

        ////
        wakeLock.release();
    }

    public void setAlarm(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int delay = mPrefs.getInt("delay", 1);
        Toast.makeText(context, Integer.toString(delay), Toast.LENGTH_LONG);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Schedule.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * delay, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 * delay, pendingIntent);
    }
}
