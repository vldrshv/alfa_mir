package ru.alfabank.alfamir.messenger.presentation.chat_list_fragment;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListAdapterContract;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListContract;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.view_holder.ChatVH;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

import javax.inject.Inject;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ChatListAdapterContract.Adapter {

    private ChatListContract.Presenter mPresenter;
    private ImageCropper mImageCropper;


    @Inject
    ChatListAdapter(
            ChatListContract.Presenter presenter,
            ImageCropper imageCropper,
            LogWrapper log) {
        mPresenter = presenter;
        mImageCropper = imageCropper;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mPresenter.takeListAdapter(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatVH(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.chat_viewholder, parent, false), mPresenter, mImageCropper);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatVH viewHolder = (ChatVH) holder;
        mPresenter.bindListRowChat(position, viewHolder);
    }

    @Override
    public long getItemId(int position) {
        return mPresenter.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getListSize();
    }

    @Override
    public void onDataSetChanged() {
        notifyDataSetChanged();
    }
}
