package com.bb.ringtopreddit.top;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bb.ringtopreddit.R;

public class TopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, TopFragment.create())
                    .commit();
        }
    }
}
