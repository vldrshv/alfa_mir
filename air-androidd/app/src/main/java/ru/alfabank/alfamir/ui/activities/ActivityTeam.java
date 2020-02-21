package ru.alfabank.alfamir.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.data.dto.old_trash.models.ModelFavorite;
import ru.alfabank.alfamir.ui.adapters.AdapterTeam;
import ru.alfabank.alfamir.utility.callbacks.JsonGetter;
import ru.alfabank.alfamir.utility.logging.firebase.FirebaseWrapper;
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by U_M0WY5 on 23.10.2017.
 */

public class ActivityTeam extends BaseActivity implements JsonGetter, LoggerContract.Client.People {

    private App app;
    private RecyclerView recycler;
    private String id;
    private String type;
    private FrameLayout flTeam;
    private TextView tvTitle;
    private SwipeRefreshLayout swipeToRefresh;
    private boolean hasContent;

    @Inject
    LoggerContract.Provider logger;
    @Inject
    FirebaseWrapper mFirebaseWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_activity);

        recycler = findViewById(R.id.activity_news_container);
        tvTitle = findViewById(R.id.toolbar_tx_title);
        LinearLayout back = findViewById(R.id.activity_team_ll_back);
        swipeToRefresh = findViewById(R.id.activity_team_swipe);
        flTeam = findViewById(R.id.activity_team_fl_team);

        swipeToRefresh.setOnRefreshListener(() -> swipeToRefresh.setRefreshing(false));

        back.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");

        app = (App) getApplication();

        mFirebaseWrapper.logEvent("team_screen_open", new Bundle());

        flTeam.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(ActivityTeam.this, flTeam);
            popup.inflate(R.menu.options_teams);

            MenuItem bedMenuItem = popup.getMenu().findItem(R.id.two_options_first);

            if (type.equals("func")) {
                bedMenuItem.setTitle("Административная команда");
            } else if (type.equals("dep")) {
                bedMenuItem.setTitle("Функциональная команда");
            }


            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.two_options_first) {
                    if (type.equals("func")) {
                        swipeToRefresh.setRefreshing(true);
                        type = "dep";
                        tvTitle.setText("Административная команда");
                        app.getDProvider().getTeam(ActivityTeam.this, type, id);
                    } else if (type.equals("dep")) {
                        swipeToRefresh.setRefreshing(true);
                        type = "func";
                        tvTitle.setText("Функциональная команда");
                        app.getDProvider().getTeam(ActivityTeam.this, type, id);
                    }
                }
                return false;
            });
            popup.show();
        });
    }

    private void inflateRecycler(List<ModelFavorite> favoriteItems) {
        if (favoriteItems.size() != 0) {
            favoriteItems = sortTeam(favoriteItems);
            AdapterTeam favoritesAdapter = new AdapterTeam(favoriteItems, app);
            recycler.setAdapter(favoritesAdapter);
            LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler.setLayoutManager(lm);
        }
        Handler handler = new Handler();
        handler.post(() -> swipeToRefresh.setRefreshing(false));
    }

    private List<ModelFavorite> sortTeam(List<ModelFavorite> favoriteItems) {
        for (int i = 0; i < favoriteItems.size(); i++) {
            if (favoriteItems.get(i).getIshead() == 1) {
                favoriteItems.add(0, favoriteItems.get(i));
                favoriteItems.remove(i + 1);
            }
        }
        return favoriteItems;
    }

    @Override
    public void onResponse(JsonWrapper jsonWrapper, int responseType) {
        List<ModelFavorite> favorites = jsonWrapper.getFavorites();
        hasContent = true;
        inflateRecycler(favorites);
    }

    @Override
    public void onFailure(JsonWrapper jsonWrapper, int responseType) {
        Handler handler = new Handler();
        handler.post(() -> swipeToRefresh.setRefreshing(false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;
        if (!hasContent) {
            if (type.equals("func")) {
                tvTitle.setText("Функциональная команда");
                logOpen("functeam");
                swipeToRefresh.setRefreshing(true);
                app.getDProvider().getTeam(this, type, id);
            } else if (type.equals("dep")) {
                tvTitle.setText("Административная команда");
                logOpen("adminteam");
                swipeToRefresh.setRefreshing(true);
                app.getDProvider().getTeam(this, type, id);
            }
        }
    }

    @Override
    public void logOpen(String name) {
        logger.openPeople(name);
    }
}
