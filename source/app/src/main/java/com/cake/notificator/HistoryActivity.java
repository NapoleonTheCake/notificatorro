package com.cake.notificator;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //load history here.
        SharedPreferences mHistory = getSharedPreferences("history", 0);
        String allHistory = mHistory.getString("allHistory", "");

        //find view and set text.
        TextView textView = (TextView) findViewById(R.id.text_History_Everything);
        textView.setText(allHistory);
    }

    //ask confirmation.
    public void onClick_ClearHistory_Dialog(View view) {
        final View passView = view; //has to be final to pass.

        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
        builder.setMessage(getString(R.string.dialog_History_Clear_Message))
                .setTitle(getString(R.string.dialog_History_Clear_Title))
                .setCancelable(true)
                .setNegativeButton(getString(R.string.dialog_Negative_No),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                .setPositiveButton(getString(R.string.dialog_Positive_Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clear(passView);
                            }
                        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //clear history.
    private void clear(View view) {
        //delete all history.
        SharedPreferences.Editor mHistoryEditor = getSharedPreferences("history", 0).edit();
        mHistoryEditor.putString("allHistory", "").apply();

        //show notification.
        Snackbar.make(view, getString(R.string.text_History_Cleared), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        //empty textview.
        TextView textView = (TextView) findViewById(R.id.text_History_Everything);
        textView.setText("");
    }
}