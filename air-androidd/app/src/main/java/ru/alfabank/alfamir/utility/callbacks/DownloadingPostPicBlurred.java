package ru.alfabank.alfamir.utility.callbacks;

import ru.alfabank.alfamir.data.source.repositories.old_trash.PostPhotoRepository;

/**
 * Created by U_M0WY5 on 18.10.2017.
 */

public interface DownloadingPostPicBlurred {
    void onDownloadedPostPicBlurred(String base64, String url, PostPhotoRepository.InflatingPostPicBlurred callbackViewHolder, int position, int blurType);
}
