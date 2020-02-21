package ru.alfabank.alfamir.profile.presentation.absence;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;

/**
 * Created by U_M0WY5 on 15.03.2018.
 */

public class AbsenceActivity extends BaseActivity implements AbsenceContract.View {
    @BindView(R.id.activity_absence_ll_back) LinearLayout llBack;
    @BindView(R.id.activity_absence_tv_intro) TextView tvIntro;
    @BindView(R.id.activity_absence_info_tv_delegate) TextView tvHint;
    @BindView(R.id.activity_absence_fl_hint) FrameLayout flHint;
    @BindView(R.id.activity_absence_tv_dates) TextView tvDate;
    @BindView(R.id.activity_absence_tv_delegate) TextView tvDelegate;
    @BindView(R.id.activity_absence_image_decoration) ImageView imageDecoration;


    @Inject
    AbsenceContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.absence_activity);
        ButterKnife.bind(this);

        tvHint.setOnClickListener(view -> presenter.showPrompt());
        flHint.setOnClickListener(view -> presenter.showPrompt());
        llBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void showTitleText(String text) {
        tvIntro.setText(text);
    }

    @Override
    public void showDate(String date, boolean isCurrent) {
        if(isCurrent){
            tvDate.setTextColor(Color.parseColor("#e53935"));
            imageDecoration.setVisibility(View.VISIBLE);
        }
        tvDate.setText(date);
    }

    @Override
    public void showDelegate(String name) {
        tvDelegate.setText(name);
        tvDelegate.setOnClickListener(view -> {
            presenter.openDelegate();
        });
    }

    @Override
    public void showEmptyDelegate() {
        tvDelegate.setText("Делегат отсутствует");
        tvDelegate.setTextColor(Color.parseColor("#999999"));
    }

    @Override
    public void showInfoPrompt() {
        DelegatePromptDialogFragment promptDialogFragment = DelegatePromptDialogFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        promptDialogFragment.show(fragmentManager, "");
    }

    @Override
    public void showActivityProfileUi(String id) {
        Intent intent = new Intent(AbsenceActivity.this, ProfileActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkIfInitialized()) return;
        presenter.takeView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
