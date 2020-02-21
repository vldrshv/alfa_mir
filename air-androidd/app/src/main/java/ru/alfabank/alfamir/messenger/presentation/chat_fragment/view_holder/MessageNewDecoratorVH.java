package ru.alfabank.alfamir.messenger.presentation.chat_fragment.view_holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract;

public class MessageNewDecoratorVH extends RecyclerView.ViewHolder implements ChatAdapterContract.MessageNewDecoratorRowView {

    private ViewHolderClickListener mListener;

    public MessageNewDecoratorVH(@NonNull View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
    }

    public interface ViewHolderClickListener { }

}
