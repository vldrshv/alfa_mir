package ru.alfabank.alfamir.utility.static_utilities;

import android.content.res.Resources;
import android.graphics.*;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;

public class PictureClipper implements ImageDecorator {

    private static Resources resources;

    public PictureClipper(Resources resources) {
        this.resources = resources;
    }

    public static RoundedBitmapDrawable makeItRound(String baseImage) {
        if (baseImage != null && !baseImage.equals("")) {
            Bitmap before = toBitmap(baseImage);
            return makeItRound(before);
        }
        return null;
    }

    public static RoundedBitmapDrawable makeItRound(Bitmap before) {
        Bitmap after;
        if (before.getWidth() >= before.getHeight()) {
            after = Bitmap.createBitmap(
                    before,
                    before.getWidth() / 2 - before.getHeight() / 2,
                    0,
                    before.getHeight(),
                    before.getHeight()
            );
        } else {
            after = Bitmap.createBitmap(
                    before,
                    0,
                    before.getHeight() / 2 - before.getWidth() / 2,
                    before.getWidth(),
                    before.getWidth()
            );
        }
        RoundedBitmapDrawable img = RoundedBitmapDrawableFactory.create(resources, after);
        img.setCircular(true);
        return img;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    @Override
    public void round(String base64) {

    }

    public static Bitmap toBitmap(String baseImage) {
        byte[] imageBytes = Base64.decode(baseImage.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
