package ru.alfabank.alfamir.main.main_feed_fragment.view_holder;

import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedListContract;

/**
 * Created by U_M0WY5 on 10.05.2018.
 */

public class SurveyViewHolder extends RecyclerView.ViewHolder implements MainFeedListContract.SurveyRowView {
    @BindView(R.id.view_holder_quiz_tv_name)
    TextView tvName;
    @BindView(R.id.view_holder_quiz_fl_close)
    FrameLayout flClose;
    @BindView(R.id.view_holder_quiz_tv_description)
    TextView tvDescription;
    @BindView(R.id.view_holder_quiz_tv_date)
    TextView tvDate;
    @BindView(R.id.view_holder_quiz_tv_progress)
    TextView tvProgress;
    @BindView(R.id.view_holder_quiz_image_cover)
    ImageView imageCover;
    @BindView(R.id.view_holder_quiz_image_date)
    ImageView imageDate;
    @BindView(R.id.view_holder_quiz_fl_quiz_body)
    FrameLayout flBody;
    @BindView(R.id.view_holder_quiz_ll_date)
    LinearLayout llDate;
    @BindView(R.id.view_holder_quiz_ll_progress)
    LinearLayout llProgress;
    @BindView(R.id.view_holder_quiz_progress_bar)
    ProgressBar progressBar;


    private ViewHolderClickListener mListener;

    public SurveyViewHolder(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;

        flBody.setOnClickListener(view -> mListener.onQuizClicked());
        flClose.setOnClickListener(view -> mListener.onQuizHideClicked());
    }

    @Override
    public void setSurveyTypeLocalizedName(String name) {
        tvName.setText(name);
    }

    @Override
    public void setTitle(String surveyTitle) {
        tvDescription.setText(surveyTitle);
    }

    @Override
    public void showEndDate(String endDate) {
        llDate.setVisibility(View.VISIBLE);
        tvDate.setText(endDate);
    }

    @Override
    public void setCompleted(String text) {
        llDate.setVisibility(View.VISIBLE);
        imageDate.setImageResource(R.drawable.ic_clock);
        tvDate.setText(text);
    }

    @Override
    public void hideEndDate() {
        llDate.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar(String text, int percentage) {
        llProgress.setVisibility(View.VISIBLE);
        progressBar.setProgress(percentage);
        tvProgress.setText(text);
        tvName.setMaxLines(1);
    }

    @Override
    public void hideProgressBar() {
        llProgress.setVisibility(View.GONE);
    }

    @Override
    public void setImage(Bitmap bitmap, boolean isAnimated) {
        imageCover.setImageBitmap(bitmap);
        imageCover.setVisibility(View.VISIBLE);

        if (isAnimated) {
            imageCover.setAlpha(0f);
            int mShortAnimationDuration = 200;
            imageCover.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }

    public interface ViewHolderClickListener {
        void onQuizClicked();

        void onQuizHideClicked();
    }

}
