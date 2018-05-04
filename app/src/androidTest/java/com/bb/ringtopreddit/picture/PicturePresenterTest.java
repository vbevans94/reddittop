package com.bb.ringtopreddit.picture;

import android.graphics.Bitmap;

import com.bb.ringtopreddit.R;
import com.bb.ringtopreddit.data.model.Picture;
import com.bb.ringtopreddit.utils.ImageSaver;
import com.bb.ringtopreddit.utils.PermissionsManager;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PicturePresenterTest {

    private static final String URL = "url";
    private static final String TITLE = "title";

    private final Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
    private final PermissionsManager mockPermissionsManager = mock(PermissionsManager.class);
    private final PictureContract.View mockView = mock(PictureContract.View.class);
    private final ImageSaver mockImageSaver = mock(ImageSaver.class);

    private PicturePresenter presenter;

    @Before
    public void setUp() {
        presenter = new PicturePresenter(mockImageSaver, mockPermissionsManager);
        presenter.takeView(mockView);
    }

    @Test
    public void onSavePicture_noPermission_shouldRequest() {
        when(mockPermissionsManager.hasWriteExternalStoragePermissions()).thenReturn(false);
        Picture picture = new Picture();

        presenter.onSavePicture(bitmap, picture);

        verify(mockView).requestWritePermission();
        verifyNoMoreInteractions(mockView, mockImageSaver);
    }

    @Test
    public void onSavePicture_hasPermission_shouldSaveAndShowMessage() {
        when(mockPermissionsManager.hasWriteExternalStoragePermissions()).thenReturn(true);
        Picture picture = new Picture(URL, 100, 100);
        picture.setTitle(TITLE);

        presenter.onSavePicture(bitmap, picture);

        verify(mockView).showMessage(eq(R.string.message_saved));
        verify(mockImageSaver).saveImage(eq(bitmap), eq(TITLE), eq(URL));
    }
}