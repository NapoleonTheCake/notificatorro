package com.cake.notificator;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    //maximum amount of notifications at the same time set here.
    private final short NOTIFY_LIMIT = 20;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                @Override
                public void onDrawerOpened(View drawerView) {
                    InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    (findViewById(R.id.editText_Title)).requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //handle buttons colors.
        SharedPreferences mPrefs = getApplicationContext()
                .getSharedPreferences("appsettings", 0);
        if (mPrefs.getBoolean("isDelayed", false)) {
            ((Button) findViewById(R.id.button_SetDelay))
                    .setBackgroundColor(getResources().getColor(R.color.button_Pressed));
        }
        if (mPrefs.getBoolean("isSilent", false)) {
            ((Button) findViewById(R.id.button_SetSilent))
                    .setBackgroundColor(getResources().getColor(R.color.button_Pressed));
        }
    }

    @Override
    protected void onPause() {
        //close keyboard.
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        super.onPause();
    }

    @Override
    protected void onStop() {
        //close keyboard.
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //right hamburger.
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    // Handle navigation view item clicks here.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_Settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_About) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_History) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //==========================================

    //creating notification.
    public void onClick_Notify(View view) {
        Context context = getApplicationContext();
        EditText text = (EditText) findViewById(R.id.editText);
        EditText textTitleEdit = (EditText) findViewById(R.id.editText_Title);
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);
        SharedPreferences.Editor mPrefsTextsEditor = getSharedPreferences("notifications", 0).edit();
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();

        //get text.
        String bigText = text.getText().toString();
        String titleText = textTitleEdit.getText().toString();

        //abort creating empty notification.
        if (bigText.length() == 0 && titleText.length() == 0) {
            if (mPrefs.getBoolean("alt_notifications", false)){
                Toast.makeText(this, getString(R.string.action_New_Note_Empty), Toast.LENGTH_SHORT)
                        .show();
                return;
            } else {
                Snackbar.make(view, getString(R.string.action_New_Note_Empty), Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }
        }

        if (bigText.length() == 0) {
            bigText = getString(R.string.template_Empty_Text);
        }

        //store id locally.
        int NOTIFY_ID = mPrefs.getInt("id", 0);

        //check if delayed.
        if (mPrefs.getBoolean("isDelayed", false)) {

            //store stuff to revoke in Schedule.
            mPrefsTextsEditor.putString("bigText", bigText).apply();
            mPrefsTextsEditor.putString("titleText", titleText).apply();

            Schedule schedule = new Schedule();
            schedule.setAlarm(context);
        } else {

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
                builder.setVibrate(new long[]{0, 50});
            }

            //create default title if empty.
            if (titleText.length() == 0) {
                builder.setContentTitle(getString(R.string.notification_Title_Default));
            }

            //show notification. check for delay.
            builder.setWhen(System.currentTimeMillis());

            Notification notification = new Notification.BigTextStyle(builder)
                    .bigText(bigText).build();

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);

            //reset delay.
            if (mPrefs.getBoolean("isDelayed", false)) {
                onClick_SetDelay(view);
            }
        }

        //append history.
        history_Append(titleText, bigText);

        //handle id and write to storage.
        NOTIFY_ID++;
        if (NOTIFY_ID > NOTIFY_LIMIT) {
            NOTIFY_ID = 0;
        }

        mPrefsEditor.putInt("id", NOTIFY_ID).apply();

        //reset silent if it is checked in settings.
        if (mPrefs.getBoolean("isSilent", false)) {
            if (mPrefs.getBoolean("reset_silent", false)) {
                onClick_Notify_Silent(view);
            }
        }

        //clear text field.
        text.setText("");
        textTitleEdit.setText("");

        //reset silent.
        if (mPrefs.getBoolean("reset_delay", false)) {
            if (mPrefs.getBoolean("isDelayed", false)) {
                onClick_SetDelay(view);
            }
        }

        //echo done.
        if (mPrefs.getBoolean("alt_notifications", false)) {
            Toast.makeText(this, getString(R.string.notification_Created), Toast.LENGTH_SHORT)
                    .show();
        } else {
            Snackbar.make(view, getString(R.string.notification_Created), Snackbar.LENGTH_SHORT).show();
        }

        //back to title.
        textTitleEdit.requestFocus();
    }

    //handling silent notifications here.
    public void onClick_Notify_Silent(View view) {
        Button button = (Button) findViewById(R.id.button_SetSilent);

        Context context = getApplicationContext();

        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();

        boolean isSilent = mPrefs.getBoolean("isSilent", false);

        if (isSilent) {

            mPrefsEditor.putBoolean("isSilent", false).apply();

            if (mPrefs.getBoolean("alt_notifications", false)) {
                Toast.makeText(this, getString(R.string.silent_Disable), Toast.LENGTH_SHORT).show();
            } else {
                Snackbar.make(view, getString(R.string.silent_Disable), Snackbar.LENGTH_SHORT).show();
            }

            //make button great again.
            button.setBackgroundColor(Color.TRANSPARENT);

        } else {

            mPrefsEditor.putBoolean("isSilent", true).apply();

            if (mPrefs.getBoolean("reset_silent", false)) {
                if (mPrefs.getBoolean("alt_notifications", false)) {
                    Toast.makeText(this, getString(R.string.silent_Enable_Once), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Snackbar.make(view, getString(R.string.silent_Enable_Once),
                            Snackbar.LENGTH_SHORT).show();
                }
            } else {
                if (mPrefs.getBoolean("alt_notifications", false)) {
                    Toast.makeText(this, getString(R.string.silent_Enable), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Snackbar.make(view, getString(R.string.silent_Enable), Snackbar.LENGTH_SHORT).show();
                }
            }

            //make button darker.
            button.setBackgroundColor(getResources().getColor(R.color.button_Pressed));
        }
    }

    //handle delayed notifications here.
    public void onClick_SetDelay(final View view) {
        final Button button = (Button) findViewById(R.id.button_SetDelay);

        Context context = getApplicationContext();

        final SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);

        final SharedPreferences.Editor mPrefsEditor = mPrefs.edit();

        boolean isDelayed = mPrefs.getBoolean("isDelayed", false);

        if (isDelayed) {

            mPrefsEditor.putBoolean("isDelayed", false).apply();

            if (mPrefs.getBoolean("alt_notifications", false)) {
                Toast.makeText(this, getString(R.string.dialog_Delay_Disable), Toast.LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(view, getString(R.string.dialog_Delay_Disable), Snackbar.LENGTH_SHORT).show();
            }

            //make button darker.
            button.setBackgroundColor(Color.TRANSPARENT);

        } else {

            //show delay warning.
            if (mPrefs.getBoolean("alt_notifications", false)) {
                Toast.makeText(this, "Delay is still WIP, probably won't work with your device.",
                        Toast.LENGTH_LONG).show();
            } else {
                Snackbar.make(view, "Delay is still WIP, probably won't work with your device.",
                        Snackbar.LENGTH_LONG).show();
            }

            //start action to show dialog.
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptDelay = layoutInflater.inflate(R.layout.prompt_delay, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setView(promptDelay);

            final EditText userInput = (EditText) promptDelay
                    .findViewById(R.id.editText_Delay);

            alertDialogBuilder
                    .setCancelable(true)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //check for empty field.
                            if (userInput.getText().toString().length() == 0) {
                                return;
                            }
                            //put delay.
                            int delay = Integer.parseInt(userInput.getText().toString());
                            mPrefsEditor.putInt("delay", delay);
                            //put delayed.
                            mPrefsEditor.putBoolean("isDelayed", true).apply();
                            //make button darker.
                            button.setBackgroundColor(getResources().getColor(R.color.button_Pressed));
                            //make snack.
                            if (mPrefs.getBoolean("alt_notifications", false)) {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.action_SetDelay_Success) + " " + delay
                                        + " " + getString(R.string.action_SetDelay_Success_Postfix),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(view, getString(R.string.action_SetDelay_Success) + " " + delay
                                        + " " + getString(R.string.action_SetDelay_Success_Postfix), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

//            ((EditText) findViewById(R.id.editText_Delay)).requestFocus();
        }

    }

    //write history method.
    private void history_Append(String titleIn, String textIn) {
        SharedPreferences mPrefs = getSharedPreferences("appsettings", 0);

        //get timestamp. //so ugly.
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        String strDate = dateFormat.format(calendar.getTime());

        //append history.
        boolean isSilent = mPrefs.getBoolean("isSilent", false);

        if (! isSilent) {

            SharedPreferences mHistory = getSharedPreferences("history", 0);
            String allHistory = mHistory.getString("allHistory", "");
            SharedPreferences.Editor mHistoryEditor = getSharedPreferences("history", 0)
                    .edit();
            String allHistoryWIP;
            allHistoryWIP = "* [ " + strDate + " ]\n";
            if (titleIn.length() != 0) {
                allHistoryWIP = allHistoryWIP + titleIn + "\n";
            }
            if (textIn.equals(getString(R.string.template_Empty_Text))) {
                allHistoryWIP = allHistoryWIP + "\n" + allHistory;
            } else {
                allHistoryWIP = allHistoryWIP + textIn + "\n\n" + allHistory;
            }
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

