package com.bb.ringtopreddit.picture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.Picture;

public class PictureActivity extends AppCompatActivity {

    private static final String EXTRA_PICTURE = "extra_picture";

    public static void start(Activity activity, Picture picture) {
        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra(EXTRA_PICTURE, picture);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);

        Picture picture = getIntent().getParcelableExtra(EXTRA_PICTURE);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, PictureFragment.create(picture))
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(picture.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
