package ru.alfabank.alfamir.utility.image_cropper;

import android.graphics.*;

import javax.inject.Inject;

public class ImageCropperImp implements ImageCropper {


    @Inject
    ImageCropperImp() {
    }

    @Override
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        return applyCorners(cropToSquare(bitmap), pixels);
    }

    private Bitmap cropToSquare(Bitmap bitmap) {
        Bitmap croppedBitmap;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            croppedBitmap = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );
        } else {
            croppedBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }
        return croppedBitmap;
    }

    private Bitmap applyCorners(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, (float) pixels, (float) pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
