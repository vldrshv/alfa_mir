package ru.alfabank.alfamir.profile.presentation.personal_info;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;

/**
 * Created by mshvdvsk on 25/02/2018.
 */

public class PersonalInfoActivity extends BaseActivity implements PersonalInfoContract.View {
    @BindView(R.id.activity_personal_info_tv_birthday) TextView tvBirthday;
    @BindView(R.id.activity_personal_info_et_about) EditText etAboutMe;
    @BindView(R.id.activity_personal_info_tv_add_about_me) TextView tvAddAboutMe;
    @BindView(R.id.activity_personal_info_ll_container_birthday) LinearLayout llBirthday;
    @BindView(R.id.activity_personal_info_ll_container_about) LinearLayout llAbout;
    @BindView(R.id.activity_personal_info_ll_back) LinearLayout llBack;
    @BindView(R.id.activity_personal_info_fl_edit_info) FrameLayout flEditInfo;

    @Inject
    PersonalInfoContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_activity);
        ButterKnife.bind(this);

        setUpEditText();

        llBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void showBirthday(String date) {
        llBirthday.setVisibility(View.VISIBLE);
        tvBirthday.setText(date);
    }

    @Override
    public void hideBirthday() {
        llBirthday.setVisibility(View.GONE);
    }

    @Override
    public void showAboutMe(String text) {
        etAboutMe.setVisibility(View.VISIBLE);
        etAboutMe.setText(text);
    }

    @Override
    public void showEmptyAboutMe(String text) {
        etAboutMe.setVisibility(View.VISIBLE);
        etAboutMe.setTextColor(Color.parseColor("#999999"));
        etAboutMe.setText(text);
    }

    @Override
    public void hideAboutMe() {
        etAboutMe.setVisibility(View.GONE);
    }

    @Override
    public void showEditInfoIcon() {
        flEditInfo.setVisibility(View.VISIBLE);
        flEditInfo.setOnClickListener(view -> {
            focusOnEditText();
        });
    }

    @Override
    public void hideEditInfoIcon() {
        flEditInfo.setVisibility(View.GONE);
    }


    @Override
    public void showAddInfoButton(String text) {
        tvAddAboutMe.setVisibility(View.VISIBLE);
        tvAddAboutMe.setText(text);
        tvAddAboutMe.setOnClickListener(view -> presenter.addAboutMe());
    }

    @Override
    public void hideAddInfoButton() {
        tvAddAboutMe.setVisibility(View.GONE);
    }

    @Override
    public void focusOnEditText() {
        etAboutMe.setFocusableInTouchMode(true);
        etAboutMe.setFocusable(true);
        etAboutMe.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etAboutMe, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkIfInitialized()) return;
        presenter.takeView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    private void setUpEditText(){
        etAboutMe.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etAboutMe.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etAboutMe.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId==EditorInfo.IME_ACTION_DONE){
                etAboutMe.clearFocus();
                etAboutMe.setFocusable(false);
                presenter.uploadAboutMe(etAboutMe.getText().toString());
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }
            return false;
        });

        etAboutMe.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                presenter.updateAboutMe(etAboutMe.getText().toString());
            }
        });

    }

}
