package com.cake.notificator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PersistActivity extends AppCompatActivity {

    public static final int NOTIFY_ID_PERSIST = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_persist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);

                //toggle pinned.
                toggle(null);

                //echo done.
                if (mPrefs.getBoolean("alt_notifications", false)) {
                    Toast.makeText(PersistActivity.this, getString(R.string.persist_Toggle_Toggled),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, getString(R.string.persist_Toggle_Toggled),
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set text here.
        SharedPreferences mPrefsText = getSharedPreferences("notifications", 0);
        ((EditText) findViewById(R.id.editText_Persist_Title))
                .setText(mPrefsText.getString("persist_title", ""));
        ((EditText) findViewById(R.id.editText_Persist))
                .setText(mPrefsText.getString("persist_text", ""));

        //handle ignore title.
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);
        if (mPrefs.getBoolean("ignore_title", false)) {
            (findViewById(R.id.editText_Persist)).requestFocus();
        } else {
            (findViewById(R.id.editText_Persist_Title)).requestFocus();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor mPrefsTextEditor = getSharedPreferences("notifications", 0).edit();
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);

        //store texts.
        mPrefsTextEditor.putString("persist_title",
                ((EditText) findViewById(R.id.editText_Persist_Title)).getText().toString()).apply();
        mPrefsTextEditor.putString("persist_text",
                ((EditText) findViewById(R.id.editText_Persist)).getText().toString()).apply();

        //update notification.
        if (mPrefs.getBoolean("persist", false)) {

            //update text.
            update();
        }
    }

    //==============================================

    public void toggle(@Nullable View view) {

        //
        // NOTE! ALSO UPDATE YOUR CODE IN PersistToggle CLASS!
        //

        Context context = getApplicationContext();
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();

        if (mPrefs.getBoolean("persist", false)) {

            //cancel persistent notification.
            ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).cancel(NOTIFY_ID_PERSIST);

            ////

            mPrefsEditor.putBoolean("persist", false).apply();

        } else {

            //update text.
            update();

            ////

            mPrefsEditor.putBoolean("persist", true).apply();
        }
    }

    private void update() {

        //
        // NOTE! ALSO UPDATE YOUR CODE IN PersistToggle CLASS!
        //

        Context context = getApplicationContext();
        SharedPreferences mPrefsText = getSharedPreferences("notifications", 0);
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);

        //get text.
        String bigText = mPrefsText.getString("persist_text", "");
        String titleText = mPrefsText.getString("persist_title", "");

        //set default texts.
        if (bigText.length() == 0) {
            bigText = getString(R.string.persist_Text_Default);
        }
        if (titleText.length() == 0) {
            titleText = getString(R.string.persist_Title_Default);
        }

        //create intent.
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);

        //build notification.
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.statusbaricon)
                .setOngoing(true)
                .setAutoCancel(false)
                .setContentTitle(titleText)
                .setContentText(bigText);

        //check if in priority.
        if (mPrefs.getBoolean("persist_prior", false)) {
            builder.setPriority(Notification.PRIORITY_MAX);
        }

        //colorize notification for 21+ api.
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setColor(getResources().getColor(R.color.color_Persist));
        }

        //show notification.
        builder.setWhen(System.currentTimeMillis());

        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(bigText).build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID_PERSIST, notification);
    }

    public void clearFieldsPersist(@Nullable View view) {

        ((EditText) findViewById(R.id.editText_Persist_Title)).setText("");
        ((EditText) findViewById(R.id.editText_Persist)).setText("");
    }
}