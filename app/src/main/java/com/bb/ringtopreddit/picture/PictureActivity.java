package com.bb.ringtopreddit.picture;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.top.TopFragment;

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

        getSupportActionBar().setTitle(picture.getTitle());
    }
}
