package ru.alfabank.alfamir.data.source.repositories.favorite_people;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.alfabank.alfamir.data.dto.FavoritePerson;
import ru.alfabank.alfamir.di.qualifiers.Remote;

/**
 * Created by mshvdvsk on 25/02/2018.
 */

@Singleton
public class FavoritePeopleRepository implements FavoritePeopleDataSource {

    private final FavoritePeopleDataSource mFavouritesRemoteDataSource;

    private List <FavoritePerson> mCachedPersons;

    @Inject
    FavoritePeopleRepository(@Remote FavoritePeopleDataSource favouritesRemoteDataSource) {
        mFavouritesRemoteDataSource = favouritesRemoteDataSource;
    }

    @Override
    public void addPerson(String id, AddPersonCallback callback) {
        mFavouritesRemoteDataSource.addPerson(id, new AddPersonCallback() {
            @Override
            public void onPersonAdded() {
                saveToCache(id);
                callback.onPersonAdded();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void removePerson(String id, RemovePersonCallback callback) {
        mFavouritesRemoteDataSource.removePerson(id, new RemovePersonCallback() {
            @Override
            public void onPersonRemoved() {
                removeFromCache(id);
                callback.onPersonRemoved();
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getFavouritePersons(LoadFavouritePersonsCallback callback) {
        getPersonsFromRemoteDataSource(callback);
    }

    private void getPersonsFromRemoteDataSource(LoadFavouritePersonsCallback callback) {
        mFavouritesRemoteDataSource.getFavouritePersons(new LoadFavouritePersonsCallback() {
            @Override
            public void onFavouritePersonsLoaded(List<FavoritePerson> persons) {
                refreshCache(persons);
                callback.onFavouritePersonsLoaded(persons);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void saveToCache(String id){
        FavoritePerson fix = new FavoritePerson();
        fix.setId(id);
        if (mCachedPersons == null) mCachedPersons = new ArrayList<>();  // TODO it's a fix, not a solution
        mCachedPersons.add(fix);
    }

    private void removeFromCache(String id){
        int itemToDelete = -1;
        for (int i = 0; i < mCachedPersons.size(); i++){
            if(mCachedPersons.get(i).getId().equals(id)){
                itemToDelete = i;
            }
        }
        if(itemToDelete!=-1){
            mCachedPersons.remove(itemToDelete);
        }
    }

    private void refreshCache (List<FavoritePerson> persons){
        if (mCachedPersons == null){
            mCachedPersons = new ArrayList<>();
        }
        mCachedPersons.clear();
        for (FavoritePerson person : persons){
            mCachedPersons.add(person);
        }
    }

    public boolean isFavoured (String id){
        if (mCachedPersons != null){
            for (FavoritePerson person : mCachedPersons){
                if (id.equals(person.getId())){
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

}
