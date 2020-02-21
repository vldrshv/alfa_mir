package ru.alfabank.alfamir.feed.presentation.feed_with_header;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.feed.presentation.feed.FeedFragment;

public class FeedWithHeaderFragment extends FeedFragment implements FeedWithHeaderContract.View{

    @BindView(R.id.feed_with_header_toolbar_fl_back)
    FrameLayout flBack;
    @BindView(R.id.feed_with_header_toolbar_fl_options)
    FrameLayout flOptions;
    @BindView(R.id.feed_with_header_toolbar_tv_title)
    TextView tvTitle;

    @Inject
    FeedWithHeaderContract.Presenter mPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.feed_with_header_fragment, container, false);
        ButterKnife.bind(this, root);
        flBack.setOnClickListener(v -> getActivity().onBackPressed());
        flOptions.setOnClickListener(v -> mPresenter.onOptionsClicked());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void setToolbarTitle(String title) {
        tvTitle.setText(title);
    }
}



