package ru.alfabank.alfamir.utility.callbacks;

import android.graphics.Bitmap;

/**
 * Created by U_M0WY5 on 19.09.2017.
 */

public interface InflatingPostPic {
    void inflatePostPicture(Bitmap img, int position);
    void inflatePostPictureAnimated(Bitmap img, int position, boolean firstTime);
}
