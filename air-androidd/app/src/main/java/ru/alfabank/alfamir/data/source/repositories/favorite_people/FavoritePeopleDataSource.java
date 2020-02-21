package ru.alfabank.alfamir.data.source.repositories.favorite_people;

import java.util.List;

import ru.alfabank.alfamir.data.dto.FavoritePerson;

/**
 * Created by mshvdvsk on 25/02/2018.
 */

public interface FavoritePeopleDataSource {

    interface AddPersonCallback {
        void onPersonAdded();
        void onDataNotAvailable();
    }

    interface RemovePersonCallback {
        void onPersonRemoved();
        void onDataNotAvailable();
    }

    interface LoadFavouritePersonsCallback {
        void onFavouritePersonsLoaded(List<FavoritePerson> persons);
        void onDataNotAvailable();
    }

    void addPerson(String id, AddPersonCallback callback);

    void removePerson(String id, RemovePersonCallback callback);

    void getFavouritePersons(LoadFavouritePersonsCallback callback);

}
