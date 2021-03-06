package com.cake.notificator;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set statusbar color.
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.color_About_StatusBar));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //show dialog.
    public void onClick_CoffeeCup(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptDelay = layoutInflater.inflate(R.layout.prompt_about_coffee, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptDelay);

        alertDialogBuilder
                .setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //open project page.
    public void onClick_CoffeeCup_Page(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/NapoleonTheCake/notificatorro"));
        startActivity(Intent.createChooser(browserIntent, getString(R.string.ui_OpenPage)));
    }

    //send me an email.
    public void onClick_CoffeeCup_Mailme(View view) {
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setType("message/rfc822")
                .putExtra(Intent.EXTRA_EMAIL, new String[] { "contactwithdmitry@gmail.com"} )
                .putExtra(Intent.EXTRA_SUBJECT, "Notificatorro: ");
        startActivity(Intent.createChooser(mailIntent, getString(R.string.ui_SendEmail)));
    }
}
