package com.cake.notificator;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class QuickNote_FromTextSelection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String quickText = getIntent()
                    .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString();

            Intent intent = new Intent(QuickNote_FromTextSelection.this, QuickNote.class);
            intent.putExtra("from_selection", quickText);
            startActivity(intent);
        }

        finish();

        super.onCreate(savedInstanceState);
    }
}
