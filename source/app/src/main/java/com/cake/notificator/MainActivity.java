package com.cake.notificator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //==========================================

    public void onClick_Notify(View view) {
        Context context = getApplicationContext(); //get app context.
        EditText text = (EditText) findViewById(R.id.editText);
        EditText textTitleEdit = (EditText) findViewById(R.id.editText_Title);

        //get big text.
        String bigText = text.getText().toString();
        String titleText = textTitleEdit.getText().toString();
        if (titleText.length() == 0) {
            titleText = getString(R.string.notification_Title);
        }

        //store id locally.
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();
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
//                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.yourtask))
                .setTicker(getString(R.string.notification_Ticker))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(titleText)
                .setContentText(bigText);

        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(bigText).build();

        //show notification.
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);

        //set counter text.
        TextView textView_Count = (TextView) findViewById(R.id.textView_Counter);
        String text_Count = getString(R.string.text_Count) + " " + NOTIFY_ID;
        textView_Count.setText(text_Count);

        //handle id and write to storage.
        NOTIFY_ID++;

        if (NOTIFY_ID > 100) {
            NOTIFY_ID = 0;
        }

        mPrefsEditor.putInt("id", NOTIFY_ID).apply();

        //append history.
        SharedPreferences mHistory = getSharedPreferences("history", MODE_PRIVATE);
        String allHistory = mHistory.getString("allHistory", "");
        SharedPreferences.Editor mHistoryEditor = getSharedPreferences("history", MODE_PRIVATE).edit();
        allHistory = allHistory + titleText + "\n" + bigText + "\n\n";
        mHistoryEditor.putString("allHistory", allHistory).apply();

        //clear text field.
        text.setText("");
        textTitleEdit.setText("");

        //echo done.
        Toast toast = Toast.makeText(context, getString(R.string.notification_Created), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, -100);
        toast.show();

        //back to title.
        textTitleEdit.requestFocus();
    }

    public void onClick_Crab(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.text_Crab_Toast),
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -100);
        toast.show();

        TextView textView = (TextView) findViewById(R.id.textView_Counter);
        textView.setVisibility(View.VISIBLE);
    }

    public void onClick_History(View view) {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }
}

