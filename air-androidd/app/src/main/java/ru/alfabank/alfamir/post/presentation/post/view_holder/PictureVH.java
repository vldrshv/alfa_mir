package ru.alfabank.alfamir.post.presentation.post.view_holder;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.post.presentation.post.FullscreenActivity;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract;

public class PictureVH extends RecyclerView.ViewHolder implements PostAdapterContract.PictureRowView {

    @BindView(R.id.rc_news_item_post_text_image_image)
    ImageView image;
    private ViewHolderClickListener mListener;

    public PictureVH(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
    }

    @Override
    public void setPostImage(Bitmap bitmap, boolean isAnimated) {

        image.setImageBitmap(bitmap);
        image.setVisibility(View.VISIBLE);

        if (!isAnimated) return; // todo have to check

        image.setAlpha(0f);
        int mShortAnimationDuration = 200;
        image.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration);
        mListener.onContentLoaded(getAdapterPosition());
        image.setOnClickListener( v -> openImageFullScreen(bitmap));
    }


    private void openImageFullScreen(Bitmap bitmap) {
        System.out.println("image clicked");
        Intent intent = new Intent(image.getContext(), FullscreenActivity.class);

        intent.putExtra("type", "image");
        intent.putExtra("url", saveToInternalStorage(bitmap));
        intent.putExtra("name", "tmp_air.jpg");

        image.getContext().startActivity(intent);
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(itemView.getContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File myPath=new File(directory,"tmp_air.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    @Override
    public void clearImage() {
        image.setImageBitmap(null);
    }

    @Override
    public void setPostImageSizeParameters(int height) {
        image.requestLayout();
        image.getLayoutParams().height = height;
        image.getLayoutParams().width = Constants.Initialization.getSCREEN_WIDTH_PHYSICAL();
    }

    public interface ViewHolderClickListener {
        void onContentLoaded(int position);
    }
}
