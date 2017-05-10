package com.cake.notificator;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.cake.notificator.MainActivity.NOTIFY_LIMIT;

public class QuickNote extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);
        if (mPrefs.getBoolean("quicknoteprompt", false)) {
            showDialog();
        } else {
            quicknoteAccept();
        }

        super.onCreate(savedInstanceState);
    }

    //======================================

    //show confirmation dialog when set.
    void showDialog() {

        //get text.
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String bigText = clipboard.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(QuickNote.this);

        //set default text if empty. //todo: fix crashing app if no text in clipboard.
        if (bigText.length() == 0) {
            builder.setMessage(getString(R.string.prompt_Quicknote_Text_Empty));
        } else {
            builder.setMessage(bigText);
        }

        builder.setTitle(getString(R.string.prompt_Quicknote_Title))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.dialog_Negative_No),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                .setPositiveButton(getString(R.string.dialog_Positive_Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quicknoteAccept();
                            }
                        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //create notification.
    void quicknoteAccept() {

        Context context = getApplicationContext();
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();

        //get text.
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String bigText = clipboard.getText().toString();

        if (bigText.length() == 0) {
            bigText = getString(R.string.prompt_Quicknote_Text_Empty);
        }

        //abort creating empty notification.
        if (bigText.length() == 0) {
            return;
        }

        //store id locally.
        int NOTIFY_ID = mPrefs.getInt("id", 0);

        //create intent.
        Intent notificationIntent = new Intent(context, QuickNote.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //build notification.
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.statusbaricon)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentText(bigText);

        //check vibration.
        if (mPrefs.getBoolean("vibration", false)) {
            builder.setVibrate(new long[]{0, 50});
        }

        //create default title.
        builder.setContentTitle(getString(R.string.notification_Title_Quicknote));

        //show notification.
        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(bigText).build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);

        //append history.
        if (! mPrefs.getBoolean("isSilentQuick", false)) {
            history_Append(bigText);
        }

        //handle id and write to storage.
        NOTIFY_ID++;
        if (NOTIFY_ID > NOTIFY_LIMIT) {
            NOTIFY_ID = 0;
        }

        mPrefsEditor.putInt("id", NOTIFY_ID).apply();

        //finish activity.
        finish();
    }

    //write history method.
    private void history_Append(String textIn) {
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);

        //get timestamp. //so ugly.
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        String strDate = dateFormat.format(calendar.getTime());

        //append history.
        boolean isSilent = mPrefs.getBoolean("isSilent", false);

        if (! isSilent) {

            SharedPreferences mHistory = getSharedPreferences("history", 0);
            SharedPreferences.Editor mHistoryEditor = getSharedPreferences("history", 0)
                    .edit();

            String allHistory = mHistory.getString("allHistory", "");
            String allHistoryWIP = "* [ " + strDate + " ]\n";

            allHistoryWIP = allHistoryWIP + textIn + "\n\n" + allHistory;

            allHistory = allHistoryWIP;

            mHistoryEditor.putString("allHistory", allHistory).apply();

        } else {

            //utilize strings from prefs.
            SharedPreferences.Editor mPrefsEdit = mPrefs.edit();
            mPrefsEdit.putString("bigText", "").apply();
            mPrefsEdit.putString("titleText", "").apply();
        }
    }
}
