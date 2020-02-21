package ru.alfabank.alfamir.data.source.repositories.old_trash;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.utility.callbacks.InflatingProfilePic;
import ru.alfabank.alfamir.utility.callbacks.DownloadingProfilePic;
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper;

/**
 * Created by mshvd_000 on 16.09.2017.
 */

public class ProfilePhotoRepository implements DownloadingProfilePic {

    private Map<String, String> base;
    private App app;
    private Resources resources;

    public ProfilePhotoRepository (App app, Resources resources) {
        base = new HashMap<>();
        this.app = app;
        this.resources = resources;
    }

    public void inflateProfileAnimated (String url, InflatingProfilePic viewHolder, int position){
        if (base.containsKey(url)){
            viewHolder.inflateProfilePic(PictureClipper.makeItRound(base.get(url)), position);
        } else {
            app.getDProvider().getProfilePictureAnimated(url, 10, 10, this, viewHolder, position);
        }
    }

    @Override
    public void onDownloadedProfilePic(String base64, String url, InflatingProfilePic callbackViewHolder, int position) {
        if(!base.containsKey(url)){
            base.put(url, base64);
        }
        callbackViewHolder.inflateProfilePic(PictureClipper.makeItRound(base64), position);
    }

    @Override
    public void onDownloadedProfilePicAnimated(String base64, String url, InflatingProfilePic callbackViewHolder, int position) {
        if(!base.containsKey(url)){
            base.put(url, base64);
        }
        callbackViewHolder.inflateProfilePicAnimated(PictureClipper.makeItRound(base64), position);
    }

}