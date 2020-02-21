package ru.alfabank.alfamir.alfa_tv.presentation.show_list.view_holder;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract.ShowListAdapterContract;

public class ShowVH extends RecyclerView.ViewHolder implements ShowListAdapterContract.ShowView {
    @BindView(R.id.show_viewholder_ll_body) LinearLayout llBody;
    @BindView(R.id.show_viewholder_tv_title) TextView tvTitle;
    @BindView(R.id.show_viewholder_tv_date) TextView tvDate;
    @BindView(R.id.show_viewholder_tv_password) TextView tvPassword;
    @BindView(R.id.show_viewholder_ll_password) LinearLayout llPassword;
    @BindView(R.id.show_viewholder_image_password) ImageView imagePassword;

    private ViewHolderClickListener mListener;

    private int enabledColor = Color.argb(255, 84, 179, 52);
    private int disabledColor = Color.argb(255, 142, 142, 147);

    public ShowVH(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        llBody.setOnClickListener(view -> mListener.onShowClicked(getAdapterPosition()));
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setTime(String date) {
        tvDate.setText(date);
    }

    @Override
    public void showPasswordRequired(boolean isSaved) {
        llPassword.setVisibility(View.VISIBLE);
        if(isSaved){
            tvPassword.setText("Пароль введён");
            tvPassword.setTextColor(enabledColor);
            imagePassword.setImageResource(R.drawable.ic_pass_unlocked);
        } else {
            tvPassword.setText("Нужен пароль");
            tvPassword.setTextColor(disabledColor);
            imagePassword.setImageResource(R.drawable.ic_password_needed);
        }
    }

    @Override
    public void hidePasswordRequired() {
        llPassword.setVisibility(View.GONE);
    }

    public interface ViewHolderClickListener {
        void onShowClicked(int position);
    }

}
