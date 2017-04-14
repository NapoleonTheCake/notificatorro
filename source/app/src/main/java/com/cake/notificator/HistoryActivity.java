package com.cake.notificator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Add clear history button.", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //load history here.
        SharedPreferences mHistory = getSharedPreferences("history", MODE_PRIVATE);
        String allHistory = mHistory.getString("allHistory", "");

        //find view and set text.
        TextView textView = (TextView) findViewById(R.id.text_History_Everything);
        textView.setText(allHistory);
    }

    public void onClick_ClearHistory(View view) {
        //delete all history.
        SharedPreferences.Editor mHistoryEditor = getSharedPreferences("history", MODE_PRIVATE).edit();
        mHistoryEditor.putString("allHistory", "").apply();

        //show notification.
        Snackbar.make(view, getString(R.string.text_History_Cleared), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        //empty textview.
        TextView textView = (TextView) findViewById(R.id.text_History_Everything);
        textView.setText("");
    }
}
