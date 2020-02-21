package ru.alfabank.alfamir.main.media_fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.feed.presentation.feed.FeedFragment;
import ru.alfabank.alfamir.search.presentation.SearchFragment;
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils;

import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_ADD;

public class MediaFragment extends FeedFragment {

    @BindView(R.id.recycler_container)
    RecyclerView rvContainer;
    @BindView(R.id.media_fragment_floating_button)
    FloatingActionButton flAddNew;
    @BindView(R.id.media_fragment_toolbar_fl_search)
    FrameLayout flSearch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.media_fragment, container, false);
        ButterKnife.bind(this, root);
        flSearch.setOnClickListener(v -> ActivityUtils.addFragmentToActivity(getFragmentManager(), new SearchFragment(), R.id.activity_main_content_frame, FRAGMENT_TRANSITION_ADD, true));
        flAddNew.setOnClickListener(view -> openCreateMediaActivityUi());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        rvContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && flAddNew.getVisibility() == View.VISIBLE) {
                    flAddNew.hide();
                } else if (dy < 0 && flAddNew.getVisibility() != View.VISIBLE) {
                    flAddNew.show();
                }
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
