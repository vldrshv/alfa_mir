package ru.alfabank.alfamir.utility.callbacks;

/**
 * Created by U_M0WY5 on 19.09.2017.
 */

public interface DownloadingProfilePic {
    void onDownloadedProfilePic(String base64, String url, InflatingProfilePic callbackViewHolder, int position);
    void onDownloadedProfilePicAnimated(String base64, String url, InflatingProfilePic callbackViewHolder, int position);

}
