package ru.alfabank.alfamir.calendar_event.presentation.view_holder;//package ru.alfabank.alfamir.calendar_event.presentation.view_holder;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.util.Base64;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import ru.alfabank.alfamir.R;
//import ru.alfabank.alfamir.calendar_event.presentation.contract.CalendarEventAdapterContract;
//
//public class CalendarEventCardVH extends RecyclerView.ViewHolder implements CalendarEventAdapterContract.CalendarEventCardRowView {
//
//    @BindView(R.id.calendar_event_card_viewholder_tv_description)
//    TextView tvDescription;
//    @BindView(R.id.calendar_event_card_viewholder_ll_date)
//    LinearLayout llDate;
//    @BindView(R.id.calendar_event_card_viewholder_ll_time)
//    LinearLayout llTime;
//    @BindView(R.id.calendar_event_card_viewholder_tv_date)
//    TextView tvDate;
//    @BindView(R.id.calendar_event_card_viewholder_tv_time)
//    TextView tvTime;
//    @BindView(R.id.calendar_event_card_viewholder_image_background)
//    ImageView imageBackground;
//
//    private VHClickListener mListener;
//
//    public CalendarEventCardVH(@NonNull View itemView, VHClickListener listener) {
//        super(itemView);
//        ButterKnife.bind(this, itemView);
//        mListener = listener;
//        imageBackground.setOnClickListener( view -> mListener.onItemClicked(getAdapterPosition()));
//    }
//
//    @Override
//    public void setTitle(String title) {
//        tvDescription.setText(title);
//    }
//
//    @Override
//    public void setPicture(String encodedImage, boolean isAnimated) {
//        Handler handler = new Handler();
//        new Thread(() -> {
//            byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            handler.post(() -> {
//                imageBackground.setImageBitmap(bitmap);
//                imageBackground.setVisibility(View.VISIBLE);
//                if (isAnimated) {
//                    imageBackground.setAlpha(0f);
//                    int mShortAnimationDuration = 200;
//                    imageBackground.animate()
//                            .alpha(1f)
//                            .setDuration(mShortAnimationDuration)
//                            .setListener(null);
//                }
//            });
//        }).start();
//    }
//
//    @Override
//    public void setDate(String date) {
//        tvDate.setText(date);
//    }
//
//    @Override
//    public void setTime(String time) {
//        tvTime.setText(time);
//    }
//
//    public interface VHClickListener {
//        void onItemClicked(int index);
//    }
//}
