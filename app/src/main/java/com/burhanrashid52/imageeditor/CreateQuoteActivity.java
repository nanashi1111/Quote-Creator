package com.burhanrashid52.imageeditor;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.burhanrashid52.imageeditor.base.BaseActivity;

/**
 * Created by admin on 8/15/18.
 */

public class CreateQuoteActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_create_quote);
    }
}
