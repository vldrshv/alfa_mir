package ru.alfabank.alfamir.utility.callbacks;

/**
 * Created by mshvd_000 on 03.10.2017.
 */

public interface Favouring {
    void onFavoured(int responseType);
    void onFavouredFailed(int responseType);
}
