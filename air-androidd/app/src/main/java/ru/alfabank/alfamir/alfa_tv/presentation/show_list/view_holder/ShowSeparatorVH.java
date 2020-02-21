package ru.alfabank.alfamir.alfa_tv.presentation.show_list.view_holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract.ShowListAdapterContract;

public class ShowSeparatorVH extends RecyclerView.ViewHolder implements ShowListAdapterContract.ShowSeparatorView {

    @BindView(R.id.show_separator_viewholder_tv_date)
    TextView tvTitle;

    public ShowSeparatorVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}

