package com.bb.ringtopreddit.picture;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.utils.PermissionsManager;
import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public class PictureFragment extends Fragment implements PictureContract.View {

    private static final String ARG_PICTURE = "arg_picture";

    @BindView(R.id.image_picture)
    ImageView imagePicture;

    @BindView(R.id.container)
    View rootView;

    @Inject
    PictureContract.Presenter presenter;

    @Inject
    PermissionsManager permissionsManager;

    private Unbinder unbinder;
    private Picture picture;

    public static PictureFragment create(Picture picture) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PICTURE, picture);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        picture = getArguments().getParcelable(ARG_PICTURE);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        Glide.with(this).load(picture.getUrl())
                .into(imagePicture);
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.takeView(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        presenter.dropView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.actions_picture, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            savePicture();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePicture() {
        if (imagePicture.getDrawable() != null) {
            presenter.onSavePicture(((BitmapDrawable) imagePicture.getDrawable()).getBitmap(), picture);
        }
    }

    @Override
    public void showMessage(int messageResId) {
        Snackbar.make(rootView, messageResId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void requestWritePermission() {
        permissionsManager.requestWriteExternalStoragePermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionsManager.REQUEST_WRITE_EXTERNAL_STORAGE:
                if (permissionsManager.permissionGranted(grantResults)) {
                    savePicture();
                } else {
                    Snackbar.make(rootView, R.string.error_save_picture_failed, Snackbar.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }
}
