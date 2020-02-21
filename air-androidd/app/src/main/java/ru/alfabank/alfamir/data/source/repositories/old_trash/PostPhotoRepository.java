package ru.alfabank.alfamir.data.source.repositories.old_trash;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.utility.callbacks.DownloadingPostPic;
import ru.alfabank.alfamir.utility.callbacks.DownloadingPostPicAnimated;
import ru.alfabank.alfamir.utility.callbacks.DownloadingPostPicBlurred;
import ru.alfabank.alfamir.utility.callbacks.InflatingPostPic;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static ru.alfabank.alfamir.Constants.Blur.LIGHT;
import static ru.alfabank.alfamir.Constants.Blur.STRONG;

/**
 * Created by mshvd_000 on 16.09.2017.
 */

public class PostPhotoRepository implements DownloadingPostPic, DownloadingPostPicBlurred, DownloadingPostPicAnimated, WebService.WebRequestListener {

    private Map<String, String> base;
    private Map<String, Bitmap> baseBlurred;
    private App app;
    private WebService webService;
    private byte[] imageBytes;
    private Bitmap bitMapPhoto;
    private PictureDownloader viewHolder;
    private static final float BITMAP_SCALE_STRONG = 0.2f;
    private static final float BLUR_RADIUS_STRONG = 25f;
    private static final float BITMAP_SCALE_LIGHT = 2f;
    private static final float BLUR_RADIUS_LIGHT = 1f;


    public PostPhotoRepository(App app) {
        base = new HashMap<>();
        baseBlurred = new HashMap<>();
        this.app = app;
        webService = app.getDProvider();
    }

    public void inflateImageAnimated(String url, int width, int height, InflatingPostPic viewHolder, int position) {
        if (base.containsKey(url)) {
            Bitmap pic = getPhotoFromBase64(base.get(url));
            viewHolder.inflatePostPicture(pic, position);
        } else {
            base.put(url, "");
            app.getDProvider().getPostPictureAnimated(url, width, height, this, viewHolder, position);
        }
    }

    public void inflateImageBlurred(String url, int width, int height, InflatingPostPicBlurred viewHolder, int position, int blurType) {
        if (base.containsKey(url) && baseBlurred.containsKey(url)) {
            viewHolder.inflatePostPicBlurred(getPhotoFromBase64(base.get(url)), baseBlurred.get(url), position);
        } else if (base.containsKey(url) && !baseBlurred.containsKey(url)) {
            Bitmap blurred = blur(getPhotoFromBase64(base.get(url)), blurType);
            baseBlurred.put(url, blurred);
            viewHolder.inflatePostPicBlurred(getPhotoFromBase64(base.get(url)), baseBlurred.get(url), position);
        } else {
            app.getDProvider().getPostPictureBlurred(url, width, height, this, viewHolder, position, blurType);
        }
    }

    private Bitmap getPhotoFromBase64(String base64) {
        if (base64 != null) {
            imageBytes = Base64.decode(base64.getBytes(), Base64.DEFAULT);
            try {
                bitMapPhoto = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                return bitMapPhoto;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private Bitmap blur(final Bitmap img, int blurType) {
        if (img != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            int width = 0;
            int height = 0;
            switch (blurType) {
                case STRONG: {
                    width = Math.round(img.getWidth() * BITMAP_SCALE_STRONG);
                    height = Math.round(img.getHeight() * BITMAP_SCALE_STRONG);
                    break;
                }
                case LIGHT: {
                    width = Math.round(img.getWidth() * BITMAP_SCALE_LIGHT);
                    height = Math.round(img.getHeight() * BITMAP_SCALE_LIGHT);
                    break;
                }
            }

            Bitmap inputBitmap = Bitmap.createScaledBitmap(img, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(app);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS_STRONG);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
            return outputBitmap;
        } else {
            return null;
        }
    }

    @Override
    public void onDownloadedPostPic(String base64, String url, InflatingPostPic callbackViewHolder, int position) {
        Date d = new Date();
        if (!base.containsKey(url)) {
            base.put(url, base64);
        }
        callbackViewHolder.inflatePostPicture(getPhotoFromBase64(base64), position);
    }

    @Override
    public void onDownloadedPostPicBlurred(String base64, String url, InflatingPostPicBlurred callbackViewHolder, int position, int blurType) {
        Bitmap blurred = blur(getPhotoFromBase64(base64), blurType);
        if (!base.containsKey(url)) {
            base.put(url, base64);
            baseBlurred.put(url, blurred);
        }
        callbackViewHolder.inflatePostPicBlurredAnimated(getPhotoFromBase64(base64), blurred, position); // TODO
    }

    @Override
    public void onDownloadedPostPicAnimated(String base64, String url, InflatingPostPic callbackViewHolder, int position) {

        if (base.containsKey(url) && base.get(url) == "") {
            base.put(url, base64);
        }

        callbackViewHolder.inflatePostPictureAnimated(getPhotoFromBase64(base64), position, true);
    }

    public interface InflatingPostPicBlurred {
        void inflatePostPicBlurred(Bitmap img, Bitmap blurredImg, int position);
        void inflatePostPicBlurredAnimated(Bitmap img, Bitmap blurredImg, int position);
    }

    public interface PictureInflater {
        void onPictureReceived(Bitmap img, int position);
    }

    @Override
    public void onResponse(JsonWrapper jsonWrapper, int responseType) {
        viewHolder.onPictureReceived(jsonWrapper.getPhoto().getImgbasecode(), responseType);
    }

    @Override
    public void onFailure(JsonWrapper jsonWrapper, int responseType) {

    }

    public interface PictureDownloader {
        void onPictureReceived(String base64, int position);
    }

}
