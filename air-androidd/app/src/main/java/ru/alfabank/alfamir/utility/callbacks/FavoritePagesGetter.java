package ru.alfabank.alfamir.utility.callbacks;

import java.util.List;

import ru.alfabank.alfamir.data.dto.old_trash.models.MFavoritePage;

/**
 * Created by U_M0WY5 on 10.10.2017.
 */

public interface FavoritePagesGetter {
    void onResponse(List<MFavoritePage> pages);
    void onFailure();
}
