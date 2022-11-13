package com.burhanrashid52.imageeditor.test;

import android.os.Bundle;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.burhanrashid52.imageeditor.R;
import com.burhanrashid52.imageeditor.base.BaseActivity;

/**
 * Created by admin on 9/11/18.
 */

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ImageView iv = findViewById(R.id.iv);

    }


    private void displayWelcomeMessage() {
        // [START get_config_values]
    }
}
