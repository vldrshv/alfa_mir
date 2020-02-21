package ru.alfabank.alfamir.main.home_fragment.presentation.view_holder;//package ru.alfabank.alfamir.main.home_fragment.presentation.view_holder;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.util.Base64;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import ru.alfabank.alfamir.R;
//import ru.alfabank.alfamir.main.home_fragment.presentation.contract.TopNewsAdapterContract;
//
//
//public class TopNewsVH extends RecyclerView.ViewHolder implements TopNewsAdapterContract.TopNewsRowView {
//
//    private VHClickListener mListener;
//    @BindView(R.id.top_news_viewholder_image)
//    ImageView image;
//    @BindView(R.id.top_news_viewholder_tv_title)
//    TextView tvTitle;
//    @BindView(R.id.top_news_viewholder_fl_main)
//    FrameLayout flMain;
//
//    public TopNewsVH(@NonNull View itemView, VHClickListener listener) {
//        super(itemView);
//        ButterKnife.bind(this, itemView);
//        mListener = listener;
//        flMain.setOnClickListener(v -> mListener.onItemClicked(getAdapterPosition()));
//    }
//
//    @Override
//    public void setTitle(String title) {
//        tvTitle.setText(title);
//    }
//
//    @Override
//    public void setPicture(String encodedImage, boolean isAnimated) {
//        Handler handler = new Handler();
//        new Thread(() -> {
//            byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            handler.post(() -> {
//                image.setImageBitmap(bitmap);
//                image.setVisibility(View.VISIBLE);
//                if (isAnimated) {
//                    image.setAlpha(0f);
//                    int mShortAnimationDuration = 200;
//                    image.animate()
//                            .alpha(1f)
//                            .setDuration(mShortAnimationDuration)
//                            .setListener(null);
//                }
//            });
//        }).start();
//    }
//
//    public interface VHClickListener {
//        void onItemClicked(int position);
//    }
//
//}
