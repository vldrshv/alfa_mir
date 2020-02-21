package ru.alfabank.alfamir.profile.presentation.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.appcompat.widget.PopupMenu;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.feed.presentation.feed.FeedActivity;
import ru.alfabank.alfamir.messenger.presentation.chat_activity.MessengerActivity;
import ru.alfabank.alfamir.people.presentation.PeopleActivity;
import ru.alfabank.alfamir.profile.data.dto.SubProfile;
import ru.alfabank.alfamir.profile.presentation.absence.AbsenceActivity;
import ru.alfabank.alfamir.profile.presentation.address.AddressActivity;
import ru.alfabank.alfamir.profile.presentation.edit_info.EditInfoActivity;
import ru.alfabank.alfamir.profile.presentation.personal_info.PersonalInfoActivity;
import ru.alfabank.alfamir.profile.presentation.skills.PersonSkillsActivity;
import ru.alfabank.alfamir.ui.activities.ActivityTeam;
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract;
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static ru.alfabank.alfamir.Constants.Messenger.USER_ID;
import static ru.alfabank.alfamir.Constants.PROFILE_ID;
import static ru.alfabank.alfamir.Constants.Post.FEED_ID;
import static ru.alfabank.alfamir.Constants.Post.FEED_TITLE;
import static ru.alfabank.alfamir.Constants.Post.FEED_TYPE;
import static ru.alfabank.alfamir.Constants.Post.FEED_URL;
import static ru.alfabank.alfamir.Constants.Post.POST_CREATION_ENABLED;
import static ru.alfabank.alfamir.Constants.Profile.ADD_PHONE_MOBILE;
import static ru.alfabank.alfamir.Constants.Profile.ADMINISTRATION_TEAM;
import static ru.alfabank.alfamir.Constants.Profile.FUNCTIONAL_TEAM;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_0;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_1;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_2;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_3;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_4;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_0;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_1;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_2;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_3;
import static ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_4;
import static ru.alfabank.alfamir.Constants.UI.FRAGMENT_TO_BACKSTACK;

public class ProfileActivity extends BaseActivity implements ProfileContract.View,
        EditMobilePhoneDialogFragment.EditPhoneDialogListener,
        WarningOnExtraChargeDialogFragment.WarningDialogListener {

    @BindView(R.id.activity_profile_image_profile)
    ImageView imageProfile;
    @BindView(R.id.activity_profile_image_favourite_unselected)
    ImageView imageFavouriteUnselected;
    @BindView(R.id.activity_profile_image_favourite_selected)
    ImageView imageFavouriteSelected;
    @BindView(R.id.activity_profile_image_like_unselected)
    ImageView imageLikeUnselected;
    @BindView(R.id.activity_profile_image_like_selected)
    ImageView imageLikeSelected;
    @BindView(R.id.activity_profile_image_add_photo)
    ImageView imageAddPhoto;
    @BindView(R.id.activity_profile_image_chat)
    ImageView imageChat;
    @BindView(R.id.activity_profile_image_mail)
    ImageView imageEmail;
    @BindView(R.id.activity_profile_image_local_time)
    ImageView imageLocalTime;
    @BindView(R.id.activity_profile_image_vacation)
    ImageView imageVacation;
    @BindView(R.id.activity_profile_image_address)
    ImageView imageAddress;

    @BindView(R.id.activity_profile_tv_name)
    TextView tvName;
    @BindView(R.id.activity_profile_tv_position)
    TextView tvTitle;
    @BindView(R.id.activity_profile_department)
    TextView department;
    @BindView(R.id.activity_profile_tv_local_time)
    TextView tvLocalTime;
    @BindView(R.id.activity_profile_tv_current_vacation)
    TextView tvCurrentVacation;
    @BindView(R.id.activity_profile_tv_mail)
    TextView tvEmail;
    @BindView(R.id.activity_profile_tv_call)
    TextView tvCall;
    @BindView(R.id.activity_profile_tv_favourite)
    TextView tvFavorite;
    @BindView(R.id.activity_profile_tv_like)
    TextView tvLike;
    @BindView(R.id.activity_profile_tv_current_vacation_extra)
    TextView tvCurrentExtraVacations;
    @BindView(R.id.activity_profile_tv_vacation)
    TextView tvVacation;
    @BindView(R.id.activity_profile_tv_vacation_extra)
    TextView tvExtraVacation;
    @BindView(R.id.activity_profile_title_tv_current_vacation)
    TextView tvTitleCurrentVacation;
    @BindView(R.id.activity_profile_title_tv_vacation)
    TextView tvTitleVacation;
    @BindView(R.id.activity_profile_tv_address)
    TextView tvAddress;

    @BindView(R.id.activity_profile_ll_current_vacation)
    LinearLayout llCurrentVacation;
    @BindView(R.id.activity_profile_ll_back)
    LinearLayout llBack;
    @BindView(R.id.activity_profile_ll_administration_team)
    LinearLayout llAdministrationTeam;
    @BindView(R.id.activity_profile_ll_function_team)
    LinearLayout llFunctionTeam;
    @BindView(R.id.activity_profile_ll_address)
    LinearLayout llAddress;
    @BindView(R.id.activity_profile_ll_add_mobile)
    LinearLayout llAddMobile;
    @BindView(R.id.activity_profile_ll_more_contacts)
    LinearLayout llMoreContacts;
    @BindView(R.id.activity_profile_tv_more_contacts)
    TextView tvMoreContacts;
    @BindView(R.id.activity_profile_image_more_contacts)
    ImageView imageMoreContacts;
    @BindView(R.id.activity_profile_ll_email)
    LinearLayout llEmail;
    @BindView(R.id.activity_profile_ll_media)
    LinearLayout llMedia;
    @BindView(R.id.activity_profile_ll_personal_info)
    LinearLayout llPersonalInfo;
    @BindView(R.id.activity_profile_ll_vacation)
    LinearLayout llVacation;
    @BindView(R.id.activity_profile_ll_skills)
    LinearLayout llSkills;

    @BindView(R.id.activity_profile_fl_chat)
    FrameLayout flChat;
    @BindView(R.id.activity_profile_fl_favorite)
    FrameLayout flFavourite;
    @BindView(R.id.activity_profile_fl_like)
    FrameLayout flLike;
    @BindView(R.id.activity_profile_fl_more)
    FrameLayout flMore;

    @BindView(R.id.activity_profile_ll_mobile_phone_0)
    LinearLayout llMobilePhone_0;
    @BindView(R.id.activity_profile_tv_mobile_phone_0)
    TextView tvMobilePhone_0;
    @BindView(R.id.activity_profile_fl_edit_mobile_small_0)
    FrameLayout flEditMobileSmall_0;

    @BindView(R.id.activity_profile_ll_mobile_phone_1)
    LinearLayout llMobilePhone_1;
    @BindView(R.id.activity_profile_tv_mobile_phone_1)
    TextView tvMobilePhone_1;
    @BindView(R.id.activity_profile_fl_edit_mobile_small_1)
    FrameLayout flEditMobileSmall_1;

    @BindView(R.id.activity_profile_ll_mobile_phone_2)
    LinearLayout llMobilePhone_2;
    @BindView(R.id.activity_profile_tv_mobile_phone_2)
    TextView tvMobilePhone_2;
    @BindView(R.id.activity_profile_fl_edit_mobile_small_2)
    FrameLayout flEditMobileSmall_2;

    @BindView(R.id.activity_profile_ll_mobile_phone_3)
    LinearLayout llMobilePhone_3;
    @BindView(R.id.activity_profile_tv_mobile_phone_3)
    TextView tvMobilePhone_3;
    @BindView(R.id.activity_profile_fl_edit_mobile_small_3)
    FrameLayout flEditMobileSmall_3;

    @BindView(R.id.activity_profile_ll_mobile_phone_4)
    LinearLayout llMobilePhone_4;
    @BindView(R.id.activity_profile_tv_mobile_phone_4)
    TextView tvMobilePhone_4;
    @BindView(R.id.activity_profile_fl_edit_mobile_small_4)
    FrameLayout flEditMobileSmall_4;

    @BindView(R.id.activity_profile_image_work_phone_0)
    ImageView imageWorkPhone_0;
    @BindView(R.id.activity_profile_tv_work_phone_0)
    TextView tvWorkPhone_0;
    @BindView(R.id.activity_profile_ll_work_phone_0)
    LinearLayout llWorkPhone_0;

    @BindView(R.id.activity_profile_tv_work_phone_1)
    TextView tvWorkPhone_1;
    @BindView(R.id.activity_profile_ll_work_phone_1)
    LinearLayout llWorkPhone_1;

    @BindView(R.id.activity_profile_tv_work_phone_2)
    TextView tvWorkPhone_2;
    @BindView(R.id.activity_profile_ll_work_phone_2)
    LinearLayout llWorkPhone_2;

    @BindView(R.id.activity_profile_tv_work_phone_3)
    TextView tvWorkPhone_3;
    @BindView(R.id.activity_profile_ll_work_phone_3)
    LinearLayout llWorkPhone_3;

    @BindView(R.id.activity_profile_tv_work_phone_4)
    TextView tvWorkPhone_4;
    @BindView(R.id.activity_profile_ll_work_phone_4)
    LinearLayout llWorkPhone_4;

    @BindView(R.id.managers_section)
    LinearLayout managersSection;
    @BindView(R.id.managers_section_title)
    TextView managersSectionTitle;

    @BindView(R.id.assistants_section)
    LinearLayout assistantsSection;
    @BindView(R.id.assistants_section_title)
    TextView assistantsSectionTitle;

    @Inject
    ProfileContract.Presenter presenter;

    private int disabledColor = Color.argb(255, 153, 153, 153);
    private int disabledTextColor = Color.argb(255, 109, 109, 114);
    private int enabledColor = Color.argb(255, 0, 122, 255);
    private DisplayMetrics metrics = new DisplayMetrics();

    @Inject
    LoggerContract.Provider logger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        llAdministrationTeam.setOnClickListener(view -> presenter.onTeamClicked(ADMINISTRATION_TEAM));
        llFunctionTeam.setOnClickListener(view -> presenter.onTeamClicked(FUNCTIONAL_TEAM));
        llAddress.setOnClickListener(view -> presenter.onAddressClicked());
        llMedia.setOnClickListener(view -> presenter.onMediaClicked());
        llPersonalInfo.setOnClickListener(view -> presenter.onPersonalInfoClicked());
        llSkills.setOnClickListener(view -> presenter.onSkillsClicked());
        flMore.setOnClickListener(view -> presenter.onMoreClicked());
        llBack.setOnClickListener(view -> onBackPressed());
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
    public void showName(String name) {
        tvName.setText(name);
    }

    @Override
    public void hideName() {
        tvName.setVisibility(GONE);
    }

    @Override
    public void showTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setDepartment(@NotNull String department) {
        this.department.setText(department);
    }

    @Override
    public void hideTitle() {
        tvTitle.setVisibility(GONE);
    }

    @Override
    public void showImageAnimated(RoundedBitmapDrawable roundedImage) {
        imageProfile.setImageDrawable(roundedImage);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(imageProfile.getContext(), R.anim.fadein);
        imageProfile.startAnimation(myFadeInAnimation);
    }

    @Override
    public void showImage(RoundedBitmapDrawable roundedImage) {
        imageProfile.setImageDrawable(roundedImage);
    }

    @Override
    public void showAddPhoto() {
        imageAddPhoto.setVisibility(VISIBLE);
        imageAddPhoto.setOnClickListener(view -> presenter.onAddPhotoClicked());
    }

    @Override
    public void showMobilePhone_0(String mobilePhone, boolean isEditable) {
        if (isEditable) {
            flEditMobileSmall_0.setVisibility(VISIBLE);
            flEditMobileSmall_0.setOnClickListener(view -> presenter.editMobilePhone(PHONE_MOBILE_0));
        }

        llMobilePhone_0.setVisibility(VISIBLE);
        tvMobilePhone_0.setText(mobilePhone);
        llMobilePhone_0.setOnClickListener(view -> {
            presenter.onCallClicked(PHONE_MOBILE_0);
        });
    }

    @Override
    public void hideMobilePhone_0() {
        llMobilePhone_0.setVisibility(GONE);
    }

    @Override
    public void showMobilePhone_1(String mobilePhone, boolean isEditable) {
        if (isEditable) {
            flEditMobileSmall_1.setVisibility(VISIBLE);
            flEditMobileSmall_1.setOnClickListener(view -> presenter.editMobilePhone(PHONE_MOBILE_1));
        }

        llMobilePhone_1.setVisibility(VISIBLE);
        tvMobilePhone_1.setText(mobilePhone);
        llMobilePhone_1.setOnClickListener(view -> presenter.onCallClicked(PHONE_MOBILE_1));
    }

    @Override
    public void hideMobilePhone_1() {
        llMobilePhone_1.setVisibility(GONE);
    }

    @Override
    public void showMobilePhone_2(String mobilePhone, boolean isEditable) {
        if (isEditable) {
            flEditMobileSmall_2.setVisibility(VISIBLE);
            flEditMobileSmall_2.setOnClickListener(view -> presenter.editMobilePhone(PHONE_MOBILE_2));
        }
        llMobilePhone_2.setVisibility(VISIBLE);
        tvMobilePhone_2.setText(mobilePhone);
        llMobilePhone_2.setOnClickListener(view -> presenter.onCallClicked(PHONE_MOBILE_2));
    }

    @Override
    public void hideMobilePhone_2() {
        llMobilePhone_2.setVisibility(GONE);
    }

    @Override
    public void showMobilePhone_3(String mobilePhone, boolean isEditable) {
        if (isEditable) {
            flEditMobileSmall_3.setVisibility(VISIBLE);
            flEditMobileSmall_3.setOnClickListener(view -> presenter.editMobilePhone(PHONE_MOBILE_3));
        }
        llMobilePhone_3.setVisibility(VISIBLE);
        tvMobilePhone_3.setText(mobilePhone);
        llMobilePhone_3.setOnClickListener(view -> presenter.onCallClicked(PHONE_MOBILE_3));
    }

    @Override
    public void hideMobilePhone_3() {
        llMobilePhone_3.setVisibility(GONE);
    }

    @Override
    public void showMobilePhone_4(String mobilePhone, boolean isEditable) {
        if (isEditable) {
            flEditMobileSmall_4.setVisibility(VISIBLE);
            flEditMobileSmall_4.setOnClickListener(view -> presenter.editMobilePhone(PHONE_MOBILE_4));
        }

        llMobilePhone_4.setVisibility(VISIBLE);
        tvMobilePhone_4.setText(mobilePhone);
        llMobilePhone_4.setOnClickListener(view -> presenter.onCallClicked(PHONE_MOBILE_4));
    }

    @Override
    public void hideMobilePhone_4() {
        llMobilePhone_4.setVisibility(GONE);
    }

    @Override
    public void showAddMobile() {
        llAddMobile.setVisibility(VISIBLE);
        llAddMobile.setOnClickListener(view -> presenter.editMobilePhone(ADD_PHONE_MOBILE));
    }

    @Override
    public void showEditMobileNumberDialog(String phone) {
        EditMobilePhoneDialogFragment dialogFragment = EditMobilePhoneDialogFragment.newInstance(phone);
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "");
    }

    @Override
    public void hideAddMobile() {
        llAddMobile.setVisibility(GONE);
    }

    @Override
    public void showMoreContactsButton() {
        llMoreContacts.setVisibility(VISIBLE);
        tvMoreContacts.setText("Еще");
        imageMoreContacts.setScaleY(1f);
        llMoreContacts.setOnClickListener(view -> presenter.onMoreContactsClicked());
    }

    @Override
    public void showHideExtraContactsButton() {
        tvMoreContacts.setText("Свернуть");
        imageMoreContacts.setScaleY(-1f);
        llMoreContacts.setOnClickListener(view -> presenter.onLessContactsClicked());
    }

    @Override
    public void hideMoreContactsButton() {
        llMoreContacts.setVisibility(GONE);
    }

    @Override
    public void showLocalTime(String localTime) {
        imageLocalTime.setVisibility(VISIBLE);
        tvLocalTime.setVisibility(VISIBLE);
        tvLocalTime.setText(localTime);
    }

    @Override
    public void hideLocalTime() {
        imageLocalTime.setVisibility(GONE);
        tvLocalTime.setVisibility(GONE);
    }

    @Override
    public void showIsFavoured(boolean isFavoured, String text) {
        if (isFavoured) {
            imageFavouriteSelected.setColorFilter(enabledColor);
            imageFavouriteSelected.setVisibility(VISIBLE);
            imageFavouriteUnselected.setVisibility(GONE);
        } else {
            imageFavouriteUnselected.setColorFilter(enabledColor);
            imageFavouriteUnselected.setVisibility(VISIBLE);
            imageFavouriteSelected.setVisibility(GONE);
        }
        tvFavorite.setText(text);
        flFavourite.setOnClickListener(view -> presenter.onFavouriteClicked());
    }

    @Override
    public void showLikes(int isLiked, int likes) {
        if (isLiked == 1) {
            imageLikeSelected.setColorFilter(enabledColor);
            imageLikeSelected.setVisibility(VISIBLE); // TODO test if we really need both
            imageLikeUnselected.setVisibility(GONE);
        } else {
            imageLikeUnselected.setColorFilter(enabledColor);
            imageLikeUnselected.setVisibility(VISIBLE);
            imageLikeSelected.setVisibility(GONE);
        }
        tvLike.setText("" + likes);
        flLike.setOnClickListener(view -> presenter.onLikeClicked());
    }

    @Override
    public void showActivityTeamUi(String profileId, String teamType) {
        Intent intent = new Intent(ProfileActivity.this, ActivityTeam.class);
        intent.putExtra(PROFILE_ID, profileId);
        intent.putExtra("type", teamType);
        startActivity(intent);
    }

    @Override
    public void showActivityAddressUi(String profileId) {
        Intent intent = new Intent(ProfileActivity.this, AddressActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        startActivity(intent);
    }

    @Override
    public void showActivityPersonalInfoUi(String profileId) {
        Intent intent = new Intent(ProfileActivity.this, PersonalInfoActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        startActivity(intent);
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
    public void showActivityNewsUi(String profileId, String name) {
        Intent intent = new Intent(ProfileActivity.this, FeedActivity.class);
        String feedId = profileId;
        String feedUrl = profileId;
        String feedType = "media";
        String feedTitle = "Медиа";
        boolean postCreationEnabled = feedId.equals("http://alfa/Info/Stories");

        intent.putExtra(FEED_ID, feedId);
        intent.putExtra(FEED_URL, feedUrl);
        intent.putExtra(FEED_TYPE, feedType);
        intent.putExtra(FEED_TITLE, feedTitle);
        intent.putExtra(POST_CREATION_ENABLED, postCreationEnabled);
        startActivity(intent);
    }

    @Override
    public void showActivityEditPersonalInfoUi(String profileId) {
        Intent intent = new Intent(ProfileActivity.this, EditInfoActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        startActivity(intent);
    }

    @Override
    public void showActivityAbsenceUi(String profileId) {
        Intent intent = new Intent(ProfileActivity.this, AbsenceActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        startActivity(intent);
    }

    @Override
    public void showActivitySkillsUi(String profileId) {
        Intent intent = new Intent(ProfileActivity.this, PersonSkillsActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        startActivity(intent);
    }

    @Override
    public void showActivityLikesUi(String profileId) {
        Intent intent = new Intent(ProfileActivity.this, PeopleActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        startActivity(intent);
    }

    @Override
    public void showActivityChatUi(String profileId) {
        Intent intent = new Intent(ProfileActivity.this, MessengerActivity.class);
        intent.putExtra(USER_ID, profileId);
        intent.putExtra(FRAGMENT_TO_BACKSTACK, false);
        startActivity(intent);
    }

    @Override
    public void showEmptyVacation(String text) {
        tvTitleVacation.setVisibility(VISIBLE);
        llVacation.setVisibility(VISIBLE);
        imageVacation.setColorFilter(disabledColor);
        tvVacation.setText(text);
        tvVacation.setTextColor(disabledTextColor);
    }

    @Override
    public void showVacation(String vacation) {
        tvTitleVacation.setVisibility(VISIBLE);
        llVacation.setVisibility(VISIBLE);
        tvVacation.setText(vacation);
        llVacation.setOnClickListener(view -> presenter.onAbsenceClicked());
    }

    @Override
    public void showCurrentVacation(String vacation) {
        tvTitleCurrentVacation.setVisibility(VISIBLE);
        llCurrentVacation.setVisibility(VISIBLE);
        tvCurrentVacation.setText(vacation);
        llCurrentVacation.setOnClickListener(view -> presenter.onAbsenceClicked());
    }

    @Override
    public void showEditProfile() {
        imageFavouriteUnselected.setImageResource(R.drawable.ic_edit_profile);
        imageFavouriteUnselected.setColorFilter(enabledColor);
        tvFavorite.setText("Редактировать");
        flFavourite.setOnClickListener(view -> presenter.onEditPersonalInfoClicked());
    }

    @Override
    public void enableEmail(String email) {
        tvEmail.setText(email);
        llEmail.setOnClickListener(view -> presenter.onMailClicked());
    }

    @Override
    public void disableEmail(String text) {
        imageEmail.setColorFilter(disabledColor);
        tvEmail.setText(text);
        tvEmail.setTextColor(disabledTextColor);
    }

    @Override
    public void disableAddress() {
        llAddress.setOnClickListener(null);
        tvAddress.setTextColor(disabledTextColor);
        imageAddress.setColorFilter(disabledColor);
    }

    @Override
    public void enableWorkPhone(String workPhone) {
        tvWorkPhone_0.setText(workPhone);
        llWorkPhone_0.setOnClickListener(view -> presenter.onCallClicked(PHONE_WORK_0));
    }

    @Override
    public void disableWorkPhone(String text) {
        imageWorkPhone_0.setColorFilter(disabledColor);
        tvWorkPhone_0.setText(text);
        tvWorkPhone_0.setTextColor(disabledTextColor);
    }

    @Override
    public void showWorkPhone_1(String workPhone) {
        llWorkPhone_1.setVisibility(VISIBLE);
        tvWorkPhone_1.setText(workPhone);
        llWorkPhone_1.setOnClickListener(view -> presenter.onCallClicked(PHONE_WORK_1));
    }

    @Override
    public void showWorkPhone_2(String workPhone) {
        llWorkPhone_2.setVisibility(VISIBLE);
        tvWorkPhone_2.setText(workPhone);
        llWorkPhone_2.setOnClickListener(view -> presenter.onCallClicked(PHONE_WORK_2));
    }

    @Override
    public void showWorkPhone_3(String workPhone) {
        llWorkPhone_3.setVisibility(VISIBLE);
        tvWorkPhone_3.setText(workPhone);
        llWorkPhone_3.setOnClickListener(view -> presenter.onCallClicked(PHONE_WORK_3));
    }

    @Override
    public void showWorkPhone_4(String workPhone) {
        llWorkPhone_4.setVisibility(VISIBLE);
        tvWorkPhone_4.setText(workPhone);
        llWorkPhone_4.setOnClickListener(view -> presenter.onCallClicked(PHONE_WORK_4));
    }

    @Override
    public void showManagers(List<SubProfile> managers) {

        if (managers.isEmpty()) {
            managersSection.setVisibility(GONE);
            return;
        }

        if (managersSection.getChildCount() == managers.size() + 1) {
            return;
        }

        managersSection.setVisibility(VISIBLE);

        if (managers.size() > 1) {
            managersSectionTitle.setText("руководители");
        } else {
            managersSectionTitle.setText("руководитель");
        }

        for (SubProfile manager : managers) {
            View managerRow = View.inflate(this, R.layout.profile_subprofile_layout, null);
            managerRow.setTag(manager.getLogin());
            managerRow.setOnClickListener(v -> presenter.onManagerClicked(manager.getLogin()));

            ImageView photo = managerRow.findViewById(R.id.photo);
            TextView initials = managerRow.findViewById(R.id.initials);

            if (manager.getPicUrl() == null || manager.getPicUrl().isEmpty()) {
                initials.setText(InitialsMaker.formInitials(manager.getName()));
                photo.setImageDrawable(manager.getBackground());
            }

            ((TextView) managerRow.findViewById(R.id.name)).setText(manager.getName());
            ((TextView) managerRow.findViewById(R.id.post)).setText(manager.getTitle());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            int bottomMargin = 8;

            if (managers.indexOf(manager) == managers.size() - 1) {
                bottomMargin = 1;
            }

            params.setMargins(dpToPx(12), 0, dpToPx(12), dpToPx(bottomMargin));
            managersSection.addView(managerRow, params);
        }
    }

    public void setManagerPhoto(String login, Bitmap image) {
        for (int i = 0; i < managersSection.getChildCount(); i++) {
            View child = managersSection.getChildAt(i);
            if (child.getTag() != null && child.getTag() instanceof String && child.getTag() == login) {
                ImageView photo = child.findViewById(R.id.photo);
                photo.setImageBitmap(image);
            }
        }
    }

    @Override
    public void showAssistants(List<SubProfile> assistants) {

        if (assistants.isEmpty()) {
            assistantsSection.setVisibility(GONE);
            return;
        }

        if (assistantsSection.getChildCount() == assistants.size() + 1) {
            return;
        }

        assistantsSection.setVisibility(VISIBLE);

        if (assistants.size() > 1) {
            assistantsSectionTitle.setText("ассистенты");
        } else {
            assistantsSectionTitle.setText("ассистент");
        }

        for (SubProfile assistant : assistants) {
            View managerRow = View.inflate(this, R.layout.profile_subprofile_layout, null);
            managerRow.setTag(assistant.getLogin());
            managerRow.setOnClickListener(v -> presenter.onManagerClicked(assistant.getLogin()));

            ImageView photo = managerRow.findViewById(R.id.photo);
            TextView initials = managerRow.findViewById(R.id.initials);

            if (assistant.getPicUrl() == null || assistant.getPicUrl().isEmpty()) {
                initials.setText(InitialsMaker.formInitials(assistant.getName()));
                photo.setImageDrawable(assistant.getBackground());
            }

            ((TextView) managerRow.findViewById(R.id.name)).setText(assistant.getName());
            ((TextView) managerRow.findViewById(R.id.post)).setText(assistant.getTitle());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            int bottomMargin = 8;

            if (assistants.indexOf(assistant) == assistants.size() - 1) {
                bottomMargin = 1;
            }

            params.setMargins(dpToPx(12), 0, dpToPx(12), dpToPx(bottomMargin));
            assistantsSection.addView(managerRow, params);
        }
    }

    public void setAssistantPhoto(String login, Bitmap image) {
        for (int i = 0; i < assistantsSection.getChildCount(); i++) {
            View child = assistantsSection.getChildAt(i);
            if (child.getTag() != null && child.getTag() instanceof String && child.getTag() == login) {
                ImageView photo = child.findViewById(R.id.photo);
                photo.setImageBitmap(image);
            }
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * metrics.density);
    }

    @Override
    public void showMore() {
        PopupMenu popup = new PopupMenu(this, flMore);
        popup.inflate(R.menu.popup_menu_one_option);
        MenuItem bedMenuItem0 = popup.getMenu().findItem(R.id.one_option_first);
        bedMenuItem0.setTitle("Кто поставил лайк");

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.one_option_first) {
                presenter.onLikesClicked();
            }
            return false;
        });
        popup.show();
    }

    @Override
    public void enableChatButton() {
        imageChat.setColorFilter(enabledColor);
        imageChat.setOnClickListener(view -> presenter.onChatClicked());
    }

    @Override
    public void disableChatButton() {
        imageChat.setColorFilter(disabledColor);
    }

    /**
     * For some unknown reason the method calls onPause twice,
     * which prevents the normal save of the state in the presenter.
     * Which in turn causes extra phones to be hidden if the users
     * chooses to onCallClicked while they are shown.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void makeCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    @Override
    public void showWarningOnExtraCharge() {
        WarningOnExtraChargeDialogFragment dialogFragment = WarningOnExtraChargeDialogFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "");
    }

    @Override
    public boolean checkCallPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestCallPermission(int phoneType) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, phoneType);
    }

    @Override
    public void sendMail(String email) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Обратная связь по Air");
//        intent.putExtra(Intent.EXTRA_TEXT, "email_body"); // TODO we may want to insert app and phone info automatically
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PHONE_WORK_0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.onCallClicked(PHONE_WORK_0);
                }
            }
        }
    }

    @Override
    public void onNumberEntered(String number) {
        presenter.saveMobilePhone(number);
    }

    @Override
    public void onNumberDeleted() {
        presenter.deleteMobilePhone();
    }


    @Override
    public void onWarningAccepted(boolean isChecked) {
        presenter.processWarning(isChecked);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
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
}
