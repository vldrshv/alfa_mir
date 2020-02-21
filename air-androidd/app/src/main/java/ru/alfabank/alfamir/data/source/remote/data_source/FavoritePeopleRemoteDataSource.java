package ru.alfabank.alfamir.data.source.remote.data_source;

import java.util.List;

import javax.inject.Inject;

import ru.alfabank.alfamir.data.dto.FavoritePerson;
import ru.alfabank.alfamir.data.source.repositories.favorite_people.FavoritePeopleDataSource;
import ru.alfabank.alfamir.data.source.remote.api.WebService;

/**
 * Created by mshvdvsk on 25/02/2018.
 */

public class FavoritePeopleRemoteDataSource implements FavoritePeopleDataSource {

    private final WebService service;

    @Inject
    FavoritePeopleRemoteDataSource(WebService service){
        this.service = service;
    }


    @Override
    public void addPerson(String id, AddPersonCallback callback) {
        service.addFavoritePerson(id, new AddPersonCallback() {
            @Override
            public void onPersonAdded() {
                callback.onPersonAdded();
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void removePerson(String id, RemovePersonCallback callback) {
        service.removeFavoritePerson(id, new RemovePersonCallback() {
            @Override
            public void onPersonRemoved() {
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
        service.getFavouritePersons(new LoadFavouritePersonsCallback() {
            @Override
            public void onFavouritePersonsLoaded(List<FavoritePerson> persons) {
                callback.onFavouritePersonsLoaded(persons);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
