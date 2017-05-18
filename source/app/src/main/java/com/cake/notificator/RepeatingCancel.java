package com.cake.notificator;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by cake on 18.05.17.
 */

public class RepeatingCancel extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {



        finish();
        super.onCreate(savedInstanceState);
    }
}
