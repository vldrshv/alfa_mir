package ru.alfabank.alfamir.utility.callbacks;

import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;

/**
 * Created by U_M0WY5 on 15.08.2017.
 */

public interface JsonGetter {
    void onResponse(JsonWrapper jsonWrapper, int responseType);
    default void onFailure(JsonWrapper jsonWrapper, int responseType){}
}
