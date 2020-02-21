package ru.alfabank.alfamir.utility.callbacks;

import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;

/**
 * Created by U_M0WY5 on 04.09.2017.
 */

public interface FullIdGetter {
    void onFullIdReceived(JsonWrapper jsonWrapper, String marsId);
}
