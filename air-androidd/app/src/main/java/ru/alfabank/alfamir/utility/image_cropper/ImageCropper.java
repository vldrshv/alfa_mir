package ru.alfabank.alfamir.utility.image_cropper;

import android.graphics.Bitmap;

public interface ImageCropper {
    Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels);
}
