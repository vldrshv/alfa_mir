package ru.alfabank.alfamir.notification.presentation;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.notification.presentation.contract.NotificationAdapterContract;
import ru.alfabank.alfamir.notification.presentation.contract.NotificationContract;
import ru.alfabank.alfamir.notification.presentation.view_holder.NotificationVH;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

import javax.inject.Inject;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements NotificationAdapterContract.Adapter {

    private RecyclerView mRecyclerView;
    private NotificationContract.Presenter mPresenter;
    private ImageCropper mImageCropper;
    private LogWrapper mLog;

    @Inject
    NotificationAdapter(NotificationContract.Presenter presenter, LogWrapper log, ImageCropper imageCropper){
        mPresenter = presenter;
        mLog = log;
        mImageCropper = imageCropper;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mPresenter.takeListAdapter(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NotificationVH(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.notification_viewholder_new, viewGroup, false), mPresenter, mImageCropper);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        NotificationVH notificationVH = (NotificationVH) viewHolder;
        mPresenter.bindListRowNotification(i, notificationVH);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getListSize();
    }

    @Override
    public long getItemId(int position) {
        return mPresenter.getItemId(position);
    }

    @Override
    public void onDataSetChanged() {
        mRecyclerView.getRecycledViewPool().clear();
        notifyDataSetChanged();
    }

    @Override
    public void onImageLoaded(int position, long viewId, Bitmap encodedImage, boolean isAnimated) {
        NotificationVH viewHolder = (NotificationVH) mRecyclerView.findViewHolderForItemId(viewId);
        if (viewHolder == null) {
            notifyItemChanged(position);
            return;
        }
        viewHolder.setImage(encodedImage, isAnimated);
    }

    @Override
    public void openPostUi(String feedId, String postId, String feedUrl, String feedType) {

    }
}
