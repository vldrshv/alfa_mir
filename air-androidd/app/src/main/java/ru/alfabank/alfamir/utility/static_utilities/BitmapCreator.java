package ru.alfabank.alfamir.utility.static_utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
/**
 * Created by U_M0WY5 on 13.02.2018.
 */

public class BitmapCreator {
    public static Bitmap getPhotoFromBase64(String base64){
        if(base64!=null){
            byte[] imageBytes = Base64.decode(base64.getBytes(), Base64.DEFAULT);
            try{
                Bitmap bitMapPhoto = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                return bitMapPhoto;
            } catch (Exception e){
                return null;
            }
        } else {
            return null;
        }
    }
}
