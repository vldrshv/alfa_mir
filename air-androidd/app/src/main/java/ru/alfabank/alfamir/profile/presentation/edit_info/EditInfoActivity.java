package ru.alfabank.alfamir.profile.presentation.edit_info;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.widget.NestedScrollView;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.profile.presentation.profile.EditMobilePhoneDialogFragment;
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by U_M0WY5 on 19.03.2018.
 */

public class EditInfoActivity extends BaseActivity implements EditInfoContract.View, EditMobilePhoneDialogFragment.EditPhoneDialogListener {
    @BindView(R.id.activity_edit_personal_info_fl_cancel)
    FrameLayout llCancel;
    @BindView(R.id.activity_edit_personal_info_fl_accept)
    FrameLayout llAccept;
    @BindView(R.id.activity_edit_personal_info_image_profile)
    ImageView imageProfile;
    @BindView(R.id.activity_edit_personal_info_image_add_photo)
    ImageView imageAddPhoto;
    @BindView(R.id.activity_edit_personal_info_tv_mobile_0)
    TextView tvMobile0;
    @BindView(R.id.activity_edit_personal_info_tv_mobile_1)
    TextView tvMobile1;
    @BindView(R.id.activity_edit_personal_info_tv_mobile_2)
    TextView tvMobile2;
    @BindView(R.id.activity_edit_personal_info_tv_mobile_3)
    TextView tvMobile3;
    @BindView(R.id.activity_edit_personal_info_tv_mobile_4)
    TextView tvMobile4;
    @BindView(R.id.activity_edit_personal_info_tv_add_mobile)
    TextView tvAddMobile;
    @BindView(R.id.activity_edit_personal_info_nsv_container)
    NestedScrollView nsvContainer;
    @BindView(R.id.activity_edit_personal_info_et_about_me)
    EditText etAboutMe;

    @Inject
    EditInfoContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_personal_info_activity);
        ButterKnife.bind(this);

        setUpEditText();

        tvAddMobile.setOnClickListener(view -> presenter.addMobile());
        imageAddPhoto.setOnClickListener(view -> presenter.addPhoto());

        llAccept.setOnClickListener(view -> {
            presenter.saveChanges();
            onBackPressed();
        });

        llCancel.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void showPhoto(Bitmap encodedImage) {
        RoundedBitmapDrawable roundedImage = PictureClipper.makeItRound(encodedImage);
        imageProfile.setImageDrawable(roundedImage);
    }

    @Override
    public void showMobile(String text) {
        tvMobile0.setVisibility(View.VISIBLE);
        tvMobile0.setText(text);
        tvMobile0.setOnClickListener(view -> presenter.editMobile());
    }

    @Override
    public void hideMobile() {
        tvMobile0.setVisibility(View.GONE);
    }

    @Override
    public void showAddMobile(String text) {
        tvAddMobile.setVisibility(View.VISIBLE);
        tvAddMobile.setText(text);
    }

    @Override
    public void hideAddMobile() {
        tvAddMobile.setVisibility(View.GONE);
    }

    @Override
    public void showAboutMe(String text) {
        etAboutMe.setText(text);
    }

    @Override
    public void showEditMobileNumberDialog(String phone) {
        EditMobilePhoneDialogFragment dialogFragment = EditMobilePhoneDialogFragment.newInstance(phone);
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "");
    }

    @Override
    public void showActivityTakePictureUi() {
        CropImage.activity()
                .setAllowRotation(false)
                .setAllowFlipping(false)
                .setBackgroundColor(R.color.black)
                .setActivityMenuIconColor(R.color.colorAccent)
                .setInitialCropWindowPaddingRatio(0)
                .setAspectRatio(1, 1)
                .setAutoZoomEnabled(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;
        presenter.takeView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
//                profileIcon.setImageURI(resultUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1280, 1280, true);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
                    presenter.updatePhoto(encodedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onNumberEntered(String number) {
        presenter.updateMobilePhone(number);
    }

    @Override
    public void onNumberDeleted() {
        presenter.deleteMobilePhone();
    }

    private void setUpEditText() {
        etAboutMe.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etAboutMe.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etAboutMe.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                etAboutMe.clearFocus();
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

        //        etAboutMe.setOnClickListener(view -> presenter.editAboutMe());
//        etAboutMe.setOnFocusChangeListener((view, b) -> {
//            if(b){
//                presenter.editAboutMe();
//            }
//        });
    }

}
