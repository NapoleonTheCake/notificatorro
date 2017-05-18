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
import android.media.RingtoneManager;
import android.os.Build;
import android.os.PowerManager;
import android.preference.PreferenceManager;

/**
 * Created by cake on 20.04.17.
 */

public class Schedule extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();

        //next is notification code. //

        //get res.
        String titleText = intent.getStringExtra("TITLE_TEXT");
        String bigText = intent.getStringExtra("BIG_TEXT");
        int NOTIFY_ID = intent.getIntExtra("NOTIFICATION_ID", 0);
        int DELAY = intent.getIntExtra("DELAY", 0);
        boolean isRepeating = intent.getBooleanExtra("REPEATING", false);

        //create intent.
        Intent notificationIntent;

        if (isRepeating) {
            notificationIntent = new Intent(context, RepeatingCancel.class);
            notificationIntent.putExtra("NOTIFY_ID", NOTIFY_ID);
        } else notificationIntent = new Intent(context, MainActivity.class);

        //use NOTIFY_ID as requestCode.
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                NOTIFY_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //build notification.
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.statusbaricon)
                .setAutoCancel(true)
                .setContentTitle(titleText)
                .setVibrate(new long[] { 0, 50 })
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(bigText);

        //for api 21+
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setColor(context.getResources().getColor(R.color.color_Delayed));
        }

        //create default title if empty.
        if (titleText.length() == 0) {
            if (isRepeating) builder.setContentTitle(DELAY + " " + context.getString(R.string.notification_Title_Repeating));
            else builder.setContentTitle(DELAY + " " + context
                    .getString(R.string.notification_Delayed_Title_Default));
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

        SharedPreferences mPrefs = context.getSharedPreferences("appsettings", 0);
        SharedPreferences mPrefsText = context.getSharedPreferences("notifications", 0);
        int delay = mPrefs.getInt("delay", 0);
        int id = mPrefs.getInt("id", 0);
        String titleText = mPrefsText.getString("titleText", "");
        String bigText = mPrefsText.getString("bigText", "");
        boolean isRepeating = mPrefs.getBoolean("repeating", false);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, Schedule.class);
        intent.putExtra("DELAY", delay);
        intent.putExtra("NOTIFICATION_ID", id);
        intent.putExtra("TITLE_TEXT", titleText);
        intent.putExtra("BIG_TEXT", bigText);
        intent.putExtra("REPEATING", isRepeating);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (isRepeating) alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 * delay,
                1000 * 60 * delay, pendingIntent);
        else alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 * delay, pendingIntent);
    }
}
