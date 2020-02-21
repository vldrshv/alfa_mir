package ru.alfabank.alfamir.utility.callbacks;

/**
 * Created by U_M0WY5 on 19.09.2017.
 */

public interface DownloadingPostPic {
    void onDownloadedPostPic(String base64, String url, InflatingPostPic callbackViewHolder, int position);
}
