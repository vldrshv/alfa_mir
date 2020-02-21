package ru.alfabank.alfamir.notification.old;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.data.source.repositories.old_trash.NotificationsRepositoryOld;
import ru.alfabank.alfamir.notification.data.dto.ModelNotification;
import ru.alfabank.alfamir.settings.presentation.settings.SettingsActivity;

import java.util.List;

public class ActivityNotifications extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener,
        NotificationsRepositoryOld.NotificationsInflater, AdapterNotifications.AdapterNotificationCallback {

    private App app;
    private NotificationsRepositoryOld notificationsRepositoryOld;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout llBack;

    private AdapterNotifications adapter;
    private LinearLayoutManager lm;
    private FrameLayout flMore;
    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleThreshold = 4;
    private boolean loading;
    boolean hasContent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_activity);

        app = (App) getApplication();
        notificationsRepositoryOld = app.getNotificationsRepositoryOld();

        recyclerView = findViewById(R.id.activity_notifications_container);
        swipeLayout = findViewById(R.id.activity_notifications_swipe_container);
        llBack = findViewById(R.id.activity_notifications_ll_back);
        flMore = findViewById(R.id.activity_notifications_fl_more);

        setListeners();
    }

    private void inflateLayout(List<ModelNotification> notifications){
        adapter = new AdapterNotifications(notifications, app.getPostPhotoRepository(),
                app.getProfilePhotoRepository(), this);
        recyclerView.setAdapter(adapter);
        lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
    }

    private void setListeners(){
        swipeLayout.setOnRefreshListener(this);

        llBack.setOnClickListener(view -> onBackPressed());

        flMore.setOnClickListener(view -> {

            PopupMenu popup = new PopupMenu(this, flMore);
            popup.inflate(R.menu.popup_menu_two_options);
            MenuItem bedMenuItem0 = popup.getMenu().findItem(R.id.two_options_second);
            bedMenuItem0.setTitle("Пометить все как прочитанные");

            MenuItem bedMenuItem1 = popup.getMenu().findItem(R.id.two_options_first);
            bedMenuItem1.setTitle("Настройки");

            popup.setOnMenuItemClickListener(item -> {
                switch(item.getItemId()){
                    case R.id.two_options_second:{
                        if(!loading && adapter!=null){
                            notificationsRepositoryOld.markAllAsRead();
                            adapter.notifyAllItemsChanged(Constants.ITEM_STATE_UNMARKED);
                        }
                        break;
                    }
                    case R.id.two_options_first:{
                        Intent intent = new Intent(this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                return false;
            });
            popup.show();
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = lm.getItemCount();
                lastVisibleItem = lm.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loading = true;
                    loadMoreNotifications();
                }
            }
        });

    }

    private void loadMoreNotifications(){
        adapter.changeLoadingState(true);
        notificationsRepositoryOld.getNotifications(ActivityNotifications.this, Constants.REQUEST_TYPE_LOAD_MORE, adapter.getLastNotificationId());
    }

    @Override
    public void onRefresh() {
        notificationsRepositoryOld.getNotifications(ActivityNotifications.this, Constants.REQUEST_TYPE_RELOAD, 0);
    }

    @Override
    public void onNotificationsReceived(List<ModelNotification> notifications, int requestType) {
        swipeLayout.setRefreshing(false);
        hasContent = true;

        if(notifications.isEmpty()){
            if(adapter==null){
                return;
            } else {
                adapter.changeLoadingState(false);
                return;
            }
        }
        loading = false;
        switch (requestType){
            case Constants.REQUEST_TYPE_LOAD:
            case Constants.REQUEST_TYPE_RELOAD:
                inflateLayout(notifications);
                break;
            case Constants.REQUEST_TYPE_LOAD_MORE:{
                adapter.uploadNotifications(notifications);
            }
        }
    }

    @Override
    public void onError() {
        swipeLayout.setRefreshing(false);
        hasContent = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkIfInitialized()) return;

        if(!hasContent){
            swipeLayout.setRefreshing(true);
            notificationsRepositoryOld.getNotifications(ActivityNotifications.this, Constants.REQUEST_TYPE_LOAD,0);
        }
    }

    @Override
    public void markAsRead(int messageId, int position) {
        adapter.notifyItemChanged(position);
        notificationsRepositoryOld.markAsRead(messageId);
    }

}
