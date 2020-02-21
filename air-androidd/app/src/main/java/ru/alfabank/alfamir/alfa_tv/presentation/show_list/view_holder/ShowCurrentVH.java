package ru.alfabank.alfamir.alfa_tv.presentation.show_list.view_holder;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract.ShowListAdapterContract;

import static ru.alfabank.alfamir.Constants.Show.PROGRESS_STATUS_CURRENT;
import static ru.alfabank.alfamir.Constants.Show.PROGRESS_STATUS_ENDED;
import static ru.alfabank.alfamir.Constants.Show.PROGRESS_STATUS_PROLONGED;

public class ShowCurrentVH extends RecyclerView.ViewHolder implements ShowListAdapterContract.ShowCurrentView {
    @BindView(R.id.show_current_viewholder_ll_body) LinearLayout llBody;
    @BindView(R.id.show_current_viewholder_tv_title) TextView tvTitle;
    @BindView(R.id.show_current_viewholder_tv_start) TextView tvStart;
    @BindView(R.id.show_current_viewholder_tv_end) TextView tvEnd;
    @BindView(R.id.show_current_viewholder_ll_password) LinearLayout llPassword;
    @BindView(R.id.show_current_viewholder_tv_password) TextView tvPassword;
    @BindView(R.id.show_current_viewholder_image_password) ImageView imagePassword;
    @BindView(R.id.show_current_viewholder_progress_bar) ProgressBar progressBar;

    private int mColorGreen = R.drawable.alfa_tv_show_progress_bar;
    private int mColorYellow = R.drawable.alfa_tv_show_progress_bar_prolonged;
    private int mColorRed = R.drawable.alfa_tv_show_progress_bar_ended;

    private int enabledColor = Color.argb(255, 84, 179, 52);
    private int disabledColor = Color.argb(255, 142, 142, 147);

    private ViewHolderClickListener mListener;
    private CountDownTimer mTimer;

    public ShowCurrentVH(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        progressBar.setMax(10000);
        llBody.setOnClickListener(view -> mListener.onShowClicked(getAdapterPosition()));
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setStartTime(String date) {
        tvStart.setText(date);
    }

    @Override
    public void setEndTime(String date) {
        tvEnd.setText(date);
    }

    @Override
    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void setProgressBarColor(int showStatus) {
        int color = 0;
        switch (showStatus){
            case PROGRESS_STATUS_CURRENT:{
                color = mColorGreen;
                break;
            }
            case PROGRESS_STATUS_PROLONGED:{
                color = mColorGreen;
                break;
            }
            case PROGRESS_STATUS_ENDED:{
                color = mColorRed;
                break;
            }
        }
        Drawable drawable = llBody.getResources().getDrawable(color);
        progressBar.setProgressDrawable(drawable);
    }

    @Override
    public void setProgressTimer(long millisUntilFinished, long oneTick) {
        mTimer = new CountDownTimer(millisUntilFinished, oneTick) {
            public void onTick(long millisUntilFinished) {
                mListener.onTimeTicked(getAdapterPosition());
            }

            public void onFinish() {
                mListener.onTimerEnded(getAdapterPosition());
            }
        }.start();
    }

    @Override
    public void clearProgressTimer() {
        if(mTimer!=null) mTimer.cancel();
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
        llPassword.setVisibility(View.INVISIBLE);
    }

    public interface ViewHolderClickListener {
        void onShowClicked(int position);
        void onTimeTicked(int position);
        void onTimerEnded(int position);
    }

}
