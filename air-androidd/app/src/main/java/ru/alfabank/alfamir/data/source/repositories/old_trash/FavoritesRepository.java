package ru.alfabank.alfamir.data.source.repositories.old_trash;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.utility.callbacks.Favouring;
import ru.alfabank.alfamir.utility.callbacks.JsonGetter;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;

/**
 * Created by mshvd_000 on 03.10.2017.
 */

public class FavoritesRepository implements JsonGetter{

    private App app;
    private Favouring callback;
    private List<String> favoritesId;
    private String id;
    private SwipeRefreshLayout.OnRefreshListener refreshCallback;

    public FavoritesRepository(App app){
        this.app = app;
        favoritesId = new ArrayList<>();
    }

    public void setRefreshCallback(SwipeRefreshLayout.OnRefreshListener refreshCallback){
        this.refreshCallback = refreshCallback;
    }

    public void addFavoritesPerson(Favouring callback, String id){
        this.callback = callback;
        this.id = id.toLowerCase();
        app.getDProvider().addFavoritePerson(this, id);
    }

    public void removeFavoritesPerson(Favouring callback, String id){
        this.callback = callback;
        this.id = id.toLowerCase();

        app.getDProvider().deleteFavoritePerson(this, id);
    }

    @Override
    public void onResponse(JsonWrapper jsonWrapper, int responseType) {
        if(responseType==0){
            favoritesId.remove(id);
        } else if (responseType==1) {
            if(!favoritesId.contains(id)){
                favoritesId.add(id);
            }
        }
        callback.onFavoured(responseType);
        refreshCallback.onRefresh();
    }

    @Override
    public void onFailure(JsonWrapper jsonWrapper, int responseType) {
        callback.onFavouredFailed(responseType);
    }

    public void addElement(String id){
        if(!favoritesId.contains(id.toLowerCase())){
            favoritesId.add(id.toLowerCase());
        }
    }
    public boolean checkIfFavorite(String id){
        if(favoritesId.contains(id.toLowerCase())){
            return true;
        } else {
            return false;
        }
    }

}
