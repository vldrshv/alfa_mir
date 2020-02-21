package ru.alfabank.alfamir.search.presentation.view_holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;

public class PageMoreVH extends RecyclerView.ViewHolder {

    private ViewHolderClickListener mListener;
    @BindView(R.id.search_page_more_fl_container)
    FrameLayout flContainer;

    public PageMoreVH(@NonNull View itemView, ViewHolderClickListener listener) {
        super(itemView);
        mListener = listener;
        ButterKnife.bind(this, itemView);
        flContainer.setOnClickListener(v -> mListener.onPageMoreClicked());
    }

    public interface ViewHolderClickListener {
        void onPageMoreClicked();
    }

}
