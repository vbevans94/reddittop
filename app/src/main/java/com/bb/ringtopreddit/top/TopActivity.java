package com.bb.ringtopreddit.top;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.TopApp;

public class TopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TopApp.enterTopScreen(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, TopFragment.create())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isFinishing()) {
            TopApp.exitTopScreen(this);
        }
    }
}
