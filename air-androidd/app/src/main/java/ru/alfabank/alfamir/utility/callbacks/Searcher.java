package ru.alfabank.alfamir.utility.callbacks;

import ru.alfabank.alfamir.ui.adapters.data_wrappers.SearchResultWrapper;

/**
 * Created by U_M0WY5 on 26.09.2017.
 */

public interface Searcher {
    void onSearched(SearchResultWrapper result, boolean isAdvanced);
}
