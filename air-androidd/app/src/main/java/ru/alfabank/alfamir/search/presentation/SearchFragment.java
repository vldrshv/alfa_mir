package ru.alfabank.alfamir.search.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.widget.RxTextView;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.main.main_activity.MainActivity;
import ru.alfabank.alfamir.main.main_activity.contract.MainActivityContract;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.search.presentation.contract.SearchFragmentContract;
import ru.alfabank.alfamir.ui.custom_views.InvisibleToBackPressedEditText;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;
import rx.Observable;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import static ru.alfabank.alfamir.Constants.Log.LOG_TEST;
import static ru.alfabank.alfamir.Constants.PROFILE_ID;

public class SearchFragment extends BaseFragment implements
        SearchFragmentContract.View,
        InvisibleToBackPressedEditText.KeyboardCatcher {

    @Inject
    SearchFragmentContract.Presenter mPresenter;
    @Inject
    SearchAdapter mAdapter;
    @BindView(R.id.search_fragment_toolbar_back)
    FrameLayout flBack;
    @BindView(R.id.search_fragment_toolbar_edittext)
    EditText mEditText;
    @BindView(R.id.recycler_container)
    RecyclerView mRecycler;
    @BindView(R.id.search_fragment_toolbar_close)
    FrameLayout flClose;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swContainer;
    @Inject
    LogWrapper mLogWrapper;
    private LinearLayoutManager mLm;
    private MainActivityContract.View mParentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParentView = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, root);
        setListeners();
        return root;
    }

    private void setListeners() {
        flBack.setOnClickListener(v -> {
            hideKeyboard();

            Fragment parent = getParentFragment();
            if (parent != null) {
                parent.getChildFragmentManager().popBackStack();
                return;
            }
            getActivity().onBackPressed();
        });
        flClose.setOnClickListener(v ->
                mEditText.setText("")
        );

        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mPresenter.onSearchInput(mEditText.getText().toString());
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                return true;
            }
            return false;
        });

        RxTextView.textChanges(mEditText)
                .filter(charSequence -> charSequence.length() > 2)
                .debounce(500, TimeUnit.MILLISECONDS)
                .flatMap(charSequence -> {
                    mLogWrapper.debug(LOG_TEST, charSequence.toString());
                    mPresenter.onSearchInput(charSequence.toString());
                    return Observable.just(charSequence.toString());
                })
                .subscribe();
    }


    @Override
    public void onResume() {
        super.onResume();
        mParentView.hideBottomNavBar();
        if (!checkIfInitialized()) return;
        mPresenter.takeView(this);
    }

    @Override
    public void showKeyboard() {
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mPresenter.dropView();
        mParentView.showBottomNavBar();
        super.onDestroy();
    }

    @Override
    public void catchKeyboard() {
    }

    @Override
    public void showResults() {
        mAdapter.setHasStableIds(true); // TODO must uncomment
        mRecycler.setAdapter(mAdapter);
        mLm = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(mLm);
    }

    @Override
    public String getUserInput() {
        return mEditText.getText().toString();
    }

    @Override
    public void openProfileUi(String id) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(PROFILE_ID, id);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            swContainer.setRefreshing(true);
        });
    }

    @Override
    public void hideLoading() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            swContainer.setRefreshing(false);
        }, 500);
    }
}
