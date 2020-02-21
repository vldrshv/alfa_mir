package ru.alfabank.alfamir.utility.callbacks;

import ru.alfabank.alfamir.data.dto.old_trash.models.ResponseSearch;

/**
 * Created by U_M0WY5 on 27.09.2017.
 */

public interface SearchResultRequester {
    void onResultReceived(Searcher callBack, ResponseSearch searchResults, String keyWord);
}
