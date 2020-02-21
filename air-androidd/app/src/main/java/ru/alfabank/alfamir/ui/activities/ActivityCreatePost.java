package ru.alfabank.alfamir.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.data.dto.old_trash.models.ModelCommunityType;
import ru.alfabank.alfamir.ui.adapters.AdapterSuggestions;
import ru.alfabank.alfamir.ui.adapters.AdapterUploadPicture;
import ru.alfabank.alfamir.utility.callbacks.JsonGetter;
import ru.alfabank.alfamir.utility.decorators.ListSpacingDecoration;
import ru.alfabank.alfamir.utility.logging.firebase.FirebaseWrapper;
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract;
import ru.alfabank.alfamir.utility.static_utilities.DensityHelper;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;


/**
 * Created by U_M0WY5 on 25.08.2017.
 * with no-Resume-friendly
 */

public class ActivityCreatePost extends BaseActivity implements JsonGetter,
        AdapterUploadPicture.ViewHolder.UploadListener, AdapterSuggestions.SuggestionsListener,
        LoggerContract.Client.NewPost {

    private EditText edTitle;
    private EditText edBody;
    private FrameLayout frameSendPost;
    private LinearLayout llBack;
    private RecyclerView recyclerView;
    private RecyclerView recyclerSugesstions;
    private FrameLayout flDevider;
    private FrameLayout flSugestions;
    private EditText edDistanation;
    private ImageView suggestions;
    private TextView textView;
    private ImageView imageCancel;
    private boolean isSent;
    private boolean isSending;
    private App app;
    private String destination;
    private String url;
    private int positionOfItemToDelete;
    private AdapterUploadPicture adapter;
    private Uri pictureUri;
    private List<String> links;
    private List<ModelCommunityType> communityTypes;
    private Bundle bundle;
    private int newsType;
    private int paddingBottom;
    private int paddingRight;
    private int paddingLeft;
    private int paddingTop;
    private int chosenPosition = -1;

    private static final int UPLOADED_PICTURE_RESPONSE = 1;
    private static final int DELETED_PICTURE_RESPONSE = 2;
    private static final int POST_PICTURE_RESPONSE = 3;
    private static final int GET_COMMUNITIES_TYPES = 4;

    private boolean takingPictures;
    private boolean uploadingPhoto;

    @Inject
    LoggerContract.Provider logger;
    @Inject
    FirebaseWrapper mFirebaseWrapper;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        bundle = getIntent().getExtras();
        newsType = bundle.getInt("feedType");

        if (newsType == Constants.Feed.NEWS_TYPE_SPORT || newsType == Constants.Feed.NEWS_TYPE_FORUM || newsType == Constants.Feed.NEWS_TYPE_NOTICE_BOARD) {
            setContentView(R.layout.create_post_community_activity);
            textView = findViewById(R.id.activity_create_post_tv_back);
            switch (newsType) {
                case Constants.Feed.NEWS_TYPE_SPORT: {
                    url = "http://alfa/Community/sport";
                    destination = url;
                    textView.setText("Спортивные сообщества");
                    break;
                }
                case Constants.Feed.NEWS_TYPE_FORUM: {
                    url = "http://alfa/Community/forum";
                    destination = url;
                    textView.setText("Форум");
                    break;
                }
                case Constants.Feed.NEWS_TYPE_NOTICE_BOARD: {
                    url = "http://alfa/Community/ads";
                    destination = url;
                    textView.setText("Доска объявлений");
                    break;
                }
            }
        } else {
            setContentView(R.layout.create_post_activity);
            url = "http://alfa/ya/alfaforyou";
            textView = findViewById(R.id.activity_create_post_tv_back);
//            textView.setText("Моя Альфа-Лайф");
            destination = "alfalife";
        }

//        logger = Logger.getInstance();
        logOpen(url);

        if (newsType == Constants.Feed.NEWS_TYPE_SPORT || newsType == Constants.Feed.NEWS_TYPE_FORUM || newsType == Constants.Feed.NEWS_TYPE_NOTICE_BOARD) {
            recyclerSugesstions = findViewById(R.id.activity_create_post_recycler_suggestion);
            recyclerSugesstions.setNestedScrollingEnabled(false);
            edDistanation = findViewById(R.id.activity_create_post_et_where);
        }

        recyclerView = findViewById(R.id.activity_create_post_recycler);
        edTitle = findViewById(R.id.activity_create_post_et_title);
        edBody = findViewById(R.id.activity_create_post_et_body);
        frameSendPost = findViewById(R.id.activity_create_post_fl_post);
        flDevider = findViewById(R.id.activity_create_post_fl_suggestions_devider);
        imageCancel = findViewById(R.id.activity_create_post_image_suggestions_cancel);
        suggestions = findViewById(R.id.activity_create_post_image_suggestions_show);
        llBack = findViewById(R.id.back);
        links = new LinkedList<>();
        links.add(0, "fake");
        setClickListeners();
    }

    private void setClickListeners() {
        frameSendPost.setOnClickListener(v -> {
            // TODO should take care of cases when not all fields are entered
            if (newsType == Constants.Feed.NEWS_TYPE_SPORT || newsType == Constants.Feed.NEWS_TYPE_FORUM || newsType == Constants.Feed.NEWS_TYPE_NOTICE_BOARD) {
                if (chosenPosition != -1 && edTitle.getText().toString().length() != 0 && edBody.getText().toString().length() != 0 && !uploadingPhoto && !isSending) {
                    isSending = true;
                    Bundle bundle = new Bundle();
                    mFirebaseWrapper.logEvent("send_post_activity_create_post", bundle);
                    logCreate(url, "feedpost", edTitle.getText().toString());
                    app.getDProvider().publishPost(ActivityCreatePost.this,
                            RequestFactory.INSTANCE.formCreatePostInCommunityRequest(url, communityTypes.get(chosenPosition).getId(), edTitle.getText().toString(), edBody.getText().toString(), links));
                }
            } else {
                if (edTitle.getText().toString().length() != 0 && edBody.getText().toString().length() != 0 && !uploadingPhoto && !isSending) {
                    isSending = true;
                    Bundle bundle = new Bundle();
                    mFirebaseWrapper.logEvent("send_post_activity_create_post", bundle);
                    logCreate(url, "feedpost", edTitle.getText().toString());
                    app.getDProvider().publishPost(ActivityCreatePost.this,
                            RequestFactory.INSTANCE.formCreatePostRequest(destination, edTitle.getText().toString(), edBody.getText().toString(), links));
                }
            }
        });

        llBack.setOnClickListener(v -> onBackPressed());
        adapter = new AdapterUploadPicture(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ListSpacingDecoration(this, R.dimen.spacing));
        int numberOfColumns = DensityHelper.calculateNoOfColumns(this);
        GridLayoutManager glm = new GridLayoutManager(this, numberOfColumns, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(glm);
        flSugestions = findViewById(R.id.activity_create_post_suggestions);
        if (newsType == Constants.Feed.NEWS_TYPE_SPORT || newsType == Constants.Feed.NEWS_TYPE_FORUM || newsType == Constants.Feed.NEWS_TYPE_NOTICE_BOARD) {
            paddingBottom = edDistanation.getPaddingBottom();
            paddingLeft = edDistanation.getPaddingLeft();
            paddingRight = edDistanation.getPaddingRight();
            paddingTop = edDistanation.getPaddingTop();
            edDistanation.setOnClickListener((View v) -> {
                if (recyclerSugesstions.getVisibility() == View.VISIBLE) {
                    hideSuggestions();
                } else {
                    showSuggestions();
                }
            });
        }
    }

    private void hideSuggestions() {
        recyclerSugesstions.setVisibility(View.GONE);
        edDistanation.setBackground(getResources().getDrawable(R.drawable.frame_enter_text_active));
        edDistanation.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        flDevider.setVisibility(View.VISIBLE);
        imageCancel.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            edDistanation.setElevation(0);
            flSugestions.setElevation(0);
        }
        if (TextUtils.isEmpty(edDistanation.getText().toString())) {
            edDistanation.setHintTextColor(Color.parseColor("#999999"));
        } else {
            edDistanation.setTextColor(Color.parseColor("#000000"));
        }
        suggestions.setVisibility(View.VISIBLE);
    }

    private void showSuggestions() {
        if (communityTypes != null) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ActivityCreatePost.this.getCurrentFocus().getWindowToken(), 0);

            AdapterSuggestions adapterSuggestions = new AdapterSuggestions(communityTypes, this);
            recyclerSugesstions.setAdapter(adapterSuggestions);
            LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerSugesstions.setLayoutManager(lm);

            recyclerSugesstions.setVisibility(View.VISIBLE);
            imageCancel.setVisibility(View.VISIBLE);

            edDistanation.setBackground(getResources().getDrawable(R.drawable.frame_ednter_text_shar_bottom_part_colored));
            flDevider.setVisibility(View.INVISIBLE);
            edDistanation.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                edDistanation.setElevation(12);
                flSugestions.setElevation(12);
            }
            edDistanation.setHintTextColor(Color.parseColor("#FFFFFFFF"));
            if (TextUtils.isEmpty(edDistanation.getText().toString())) {
                edDistanation.setHintTextColor(Color.parseColor("#FFFFFFFF"));
            } else {
                edDistanation.setTextColor(Color.parseColor("#FFFFFFFF"));
            }
            edDistanation.setCursorVisible(false);
            edDistanation.setClickable(false);
            edDistanation.setFocusable(false);

            suggestions.setVisibility(View.GONE);

        }
    }

    private void dispatchTakePictureIntent() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        takingPictures = false;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                pictureUri = result.getUri();

                Handler handler = new Handler();
                new Thread(() -> {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pictureUri);
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1280, 1280, true);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
                        handler.post(() -> {
                            app.getDProvider().uploadPhoto(this, destination, encodedImage);
                        });
                        uploadingPhoto = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            }
        }
    }

    @Override
    public void onResponse(JsonWrapper jsonWrapper, int responseType) {
        if (responseType == UPLOADED_PICTURE_RESPONSE) {
            links.add(1, jsonWrapper.getFileUrl()); // TODO should process the failed upload case
            adapter.addPicture(pictureUri);
            uploadingPhoto = false;

        } else if (responseType == DELETED_PICTURE_RESPONSE) {
            if (positionOfItemToDelete < links.size()) { // TODO не валится, но не красиво. хотя?..
                links.remove(positionOfItemToDelete);
                adapter.deletePicture(positionOfItemToDelete);
            }
        } else if (responseType == POST_PICTURE_RESPONSE) {
            isSent = true;
            onBackPressed();
        } else if (responseType == GET_COMMUNITIES_TYPES) {
            communityTypes = jsonWrapper.getCommunityTypes(); // TODO
        }
    }

    @Override
    public void onFailure(JsonWrapper jsonWrapper, int responseType) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!takingPictures && !isSent) {
            for (int i = 1; i < links.size(); i++) {
                app.getDProvider().deletePhoto(ActivityCreatePost.this, destination, links.get(i));
            }
        }
    }

    @Override
    public void uploadPictureItemClicked() {
        Bundle bundle = new Bundle();
        mFirebaseWrapper.logEvent("add_picture_activity_create_post", bundle);
        takingPictures = true;
        dispatchTakePictureIntent();
    }

    @Override
    public void deletePictureItemClicked(int position) {
        positionOfItemToDelete = position;
        app.getDProvider().deletePhoto(ActivityCreatePost.this, destination, links.get(position));
    }

    @Override
    public void onSuggestionsClicked(int position) {
        chosenPosition = position;
        edDistanation.setText(communityTypes.get(position).getTitle());
        edDistanation.setTextColor(Color.parseColor("#000000"));
        hideSuggestions();
    }

    @Override
    public void onBackPressed() {
        if (edTitle.getText().toString().isEmpty() && edBody.getText().toString().isEmpty()) {
            super.onBackPressed();
        } else if (!isSent) {
            new AlertDialog.Builder(this)
                    .setTitle("Вы уверены, что хотите выйти?")
                    .setMessage("Введеный Вами текст будет удален.")
                    .setPositiveButton("Выйти", (dialog, which) -> finish())
                    .setNegativeButton("Остаться", null)
                    .show();
        } else if (isSent) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;
        if (newsType == Constants.Feed.NEWS_TYPE_SPORT || newsType == Constants.Feed.NEWS_TYPE_FORUM || newsType == Constants.Feed.NEWS_TYPE_NOTICE_BOARD) {
            if (communityTypes == null) {
                app.getDProvider().getCommunitiesTypes(this,"{type:'community'," + "parameters:{obj: 'getcategory',url:'" + url + "'}}");
            }
        }
    }

    @Override
    public void logOpen(String feedId) {
        logger.openCreatePost(url);
    }

    @Override
    public void logCreate(String feedId, String name, String title) {
        logger.createPost(feedId, name, title);
    }
}
