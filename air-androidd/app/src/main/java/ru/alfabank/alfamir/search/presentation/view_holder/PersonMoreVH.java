package ru.alfabank.alfamir.search.presentation.view_holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;

public class PersonMoreVH extends RecyclerView.ViewHolder {

    private ViewHolderClickListener mListener;
    @BindView(R.id.search_person_more_fl_container)
    FrameLayout flContainer;

    public PersonMoreVH(@NonNull View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        flContainer.setOnClickListener(v -> mListener.onPersonMoreClicked());
    }

    public interface ViewHolderClickListener {
        void onPersonMoreClicked();
    }

}
