package ru.alfabank.alfamir.utility.callbacks;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;

/**
 * Created by U_M0WY5 on 19.09.2017.
 */

public interface InflatingProfilePic {
    void inflateProfilePic (RoundedBitmapDrawable img, int position);
    void inflateProfilePicAnimated (RoundedBitmapDrawable img, int position);
}
