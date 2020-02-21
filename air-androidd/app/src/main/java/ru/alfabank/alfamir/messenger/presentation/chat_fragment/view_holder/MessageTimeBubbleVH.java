package ru.alfabank.alfamir.messenger.presentation.chat_fragment.view_holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract;

public class MessageTimeBubbleVH extends RecyclerView.ViewHolder implements ChatAdapterContract.MessageTimeBubbleRowView {

    @BindView(R.id.message_time_bubble_viewholder_tv_date)
    TextView tvDate;

    private ViewHolderClickListener mListener;

    public MessageTimeBubbleVH(@NonNull View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
    }

    @Override
    public void setDate(String date) {
        tvDate.setText(date);
    }

    public interface ViewHolderClickListener { }

}
