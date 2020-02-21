package ru.alfabank.alfamir.profile.presentation.skills;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;

public class PersonSkillsActivity extends BaseActivity implements PersonSkillsContract.View {
    @BindView(R.id.activity_skills_ll_back) LinearLayout llBack;
    @BindView(R.id.activity_skills_ll_empty_state) LinearLayout llEmptyState;
    @BindView(R.id.activity_skills_fl_info) FrameLayout flInfo;
    @BindView(R.id.activity_skills_tv_skills) TextView tvSkills;

    @Inject
    PersonSkillsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skills_activity);
        ButterKnife.bind(this);

        llBack.setOnClickListener(view -> onBackPressed());
        flInfo.setOnClickListener(view -> presenter.openInfoPrompt());
    }

    @Override
    public void showSkills(String skills) {
        tvSkills.setVisibility(View.VISIBLE);
        tvSkills.setText(skills);
    }

    @Override
    public void showEmptySkills() {
        tvSkills.setVisibility(View.GONE);
        llEmptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInfoPrompt() {
        SkillsPromptDialogFragment promptDialogFragment = SkillsPromptDialogFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        promptDialogFragment.show(fragmentManager, "");
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

}
