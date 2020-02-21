package ru.alfabank.alfamir.post.presentation.post;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Strings;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.feed.presentation.feed.FeedActivity;
import ru.alfabank.alfamir.post.data.dto.PostRaw;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostContract;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.ui.custom_views.InvisibleToBackPressedEditText;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;
import static ru.alfabank.alfamir.Constants.Post.FEED_ID;
import static ru.alfabank.alfamir.Constants.Post.FEED_TITLE;
import static ru.alfabank.alfamir.Constants.Post.FEED_TYPE;
import static ru.alfabank.alfamir.Constants.Post.FEED_URL;
import static ru.alfabank.alfamir.Constants.Post.POST_CREATION_ENABLED;
import static ru.alfabank.alfamir.Constants.Post.POST_URL;

public class PostFragment extends BaseFragment implements PostContract.View, InvisibleToBackPressedEditText.KeyboardCatcher  {

    @BindView(R.id.activity_post_container) RecyclerView recycler;
    @BindView(R.id.activity_post_ll_curtain) LinearLayout llCurtain;
    @BindView(R.id.activity_post_editText) InvisibleToBackPressedEditText editText;
    @BindView(R.id.activity_post_image_send_comment) ImageView buttonSend;
    @BindView(R.id.activity_post_floating_button) FloatingActionButton flButton;
    @BindView(R.id.activity_post_swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_fragment_toolbar_back) FrameLayout flBack;
    @BindView(R.id.activity_post_fl_keyboard) FrameLayout flKeyboard;

    @Inject
    PostContract.Presenter mPresenter;
    @Inject
    PostAdapter adapter;

    private LinearLayoutManager lm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.post_fragment, container, false);
        ButterKnife.bind(this, root);

        initListeners();
        return root;
    }
    private void initListeners() {
        flBack.setOnClickListener(v -> getActivity().onBackPressed());
        flButton.setOnClickListener(v -> mPresenter.onFBClicked());
        buttonSend.setOnClickListener(v -> mPresenter.onSendComment(editText.getText().toString()));
        swipeRefreshLayout.setOnRefreshListener(() -> mPresenter.onPostRefresh());
        editText.setListener(this);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && flButton.getVisibility() == View.VISIBLE) {
                    flButton.hide();
                } else if (dy < 0 && flButton.getVisibility() != View.VISIBLE) {
                    flButton.show();
                }
            }});
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!checkIfInitialized()) return;
        mPresenter.takeView(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onViewPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
        App.Companion.getAppInstance().clearAppMap();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (int i = 0; i < adapter.getItemCount(); i ++) {
            RecyclerView.ViewHolder vh = recycler.findViewHolderForAdapterPosition(i);
            if (vh != null)
                adapter.onViewDetachedFromWindow(vh);
        }
    }

    @Override
    public void showPost() {
        recycler.setAdapter(adapter);
        recycler.setNestedScrollingEnabled(false);
        lm = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lm);
    }

    @Override
    public void showKeyboard(String autoInput) {
        Handler mHandler = new Handler();
        mHandler.post(() -> {
            if(!editText.hasFocus()){
                flKeyboard.setVisibility(View.VISIBLE);
                editText.requestFocus();
                editText.setCursorVisible(true);
                if(!Strings.isNullOrEmpty(autoInput)){
                    editText.setText(autoInput);
                    editText.setSelection(autoInput.length());
                }
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    @Override
    public void setTextEditInput(String input) {
        editText.setText(input);
    }

    @Override
    public void hideKeyboard() {
        Handler mHandler = new Handler();
        mHandler.post(() -> {
            flKeyboard.setVisibility(View.INVISIBLE);
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    buttonSend.getWindowToken(), 0);
        });
    }

    @Override
    public void disableTextInput() {
        buttonSend.setOnClickListener(null);
    }

    @Override
    public void enableTextInput() {
        buttonSend.setOnClickListener(v -> mPresenter.onSendComment(editText.getText().toString()));
    }

    @Override
    public void showCurtain() {
        llCurtain.setAlpha(1f);
        llCurtain.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCurtain() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            int mShortAnimationDuration = 400;
            llCurtain.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            llCurtain.setVisibility(View.GONE);
                        }
                    });
        }, 600);
    }

    @Override
    public void scrollToPosition(int position) {
        recycler.scrollToPosition(position);
    }

    @Override
    public void showFloatingButton(int delay) {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(() -> flButton.show(), delay);
    }

    @Override
    public void hideFloatingButton() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(() -> flButton.hide());
    }

    @Override
    public void showLoadingProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void openNewsActivityUi(String title, String type, String postUrl) {
        Intent intent = new Intent(getContext(), FeedActivity.class);
        boolean postCreationEnabled = postUrl.equals("http://alfa/Info/Stories");

        intent.putExtra(FEED_ID, postUrl);
        intent.putExtra(FEED_URL, postUrl);
        intent.putExtra(FEED_TYPE, type);
        intent.putExtra(FEED_TITLE, title);
        intent.putExtra(POST_CREATION_ENABLED, postCreationEnabled);
        startActivity(intent);
    }

    @Override
    public void onEmailClicked(String uri) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void openProfileActivityUi(String id) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(PROFILE_ID, id);
        startActivity(intent);
    }


    @Override
    public void openPostOptions(int position, int isDeletable, String favoriteStatus, String subscribeStatus) {

    }

    @Override
    public void onItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void showPostHybrid(PostRaw postRaw) {
        recycler.setAdapter(adapter);
        recycler.setNestedScrollingEnabled(false);
        lm = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lm);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(recycler, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onExternalLinkClicked(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onInternalLinkClicked(String url) {
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra(POST_URL, url);
        startActivity(intent);
    }

    @Override
    public void catchKeyboard() {
        Handler handler = new Handler();
        handler.postDelayed(() -> flButton.show(), 400);
        flKeyboard.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(buttonSend.getWindowToken(), 0);
    }
}
