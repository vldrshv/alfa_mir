package ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.view_holder;

import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListAdapterContract;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;

public class ChatVH extends RecyclerView.ViewHolder implements ChatListAdapterContract.ChatRowView {
    @BindView(R.id.chat_viewholder_fl_main_body)
    FrameLayout flMainBody;
    @BindView(R.id.chat_viewholder_tv_title)
    TextView tvTitle;
    @BindView(R.id.chat_viewholder_tv_last_message_time)
    TextView tvTime;
    @BindView(R.id.chat_viewholder_tv_last_message)
    TextView tvMessage;
    @BindView(R.id.chat_viewholder_tv_new_messages)
    TextView tvNewMessageCount;
    @BindView(R.id.chat_viewholder_fl_new_messages)
    FrameLayout flNewMessages;
    @BindView(R.id.chat_viewholder_image_chat_pic)
    ImageView imageChatPic;
    @BindView(R.id.chat_viewholder_image_online)
    ImageView imageOnline;

    private ViewHolderClickListener mListener;
    private ImageCropper mImageCropper;

    public ChatVH(View itemView, ViewHolderClickListener listener, ImageCropper imageCropper) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        mImageCropper = imageCropper;
        flMainBody.setOnClickListener(view -> mListener.onChatClicked(getAdapterPosition()));
    }

    @Override
    public void setChatPic(Bitmap bitmap, boolean isCached) {

        Bitmap curvedBitmap = mImageCropper.getRoundedCornerBitmap(bitmap, (int) (4 * Constants.Initialization.getDENSITY()));
        imageChatPic.setImageBitmap(curvedBitmap);
        imageChatPic.setVisibility(View.VISIBLE);
        if (!isCached) {
            imageChatPic.setAlpha(0f);
            int mShortAnimationDuration = 200;
            imageChatPic.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }

    @Override
    public void setChatTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setOnlineStatus(boolean isOnline) {
        if (isOnline) imageOnline.setVisibility(View.VISIBLE);
        else imageOnline.setVisibility(View.GONE);
    }

    @Override
    public void setLastMessageTime(String time) {
        tvTime.setText(time);
    }

    @Override
    public void setLastMessage(String message) {
        tvMessage.setText(message);
    }

    @Override
    public void setNewMessageCount(String count) {
        tvNewMessageCount.setText(count);
    }

    @Override
    public void showNewMessageBadge() {
        flNewMessages.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNewMessageBadge() {
        flNewMessages.setVisibility(View.GONE);
    }

    public interface ViewHolderClickListener {
        void onChatClicked(int position);
    }
}
