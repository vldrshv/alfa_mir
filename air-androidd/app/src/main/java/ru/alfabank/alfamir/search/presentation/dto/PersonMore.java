package ru.alfabank.alfamir.search.presentation.dto;

import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PERSON_MORE;

public class PersonMore implements DisplayableSearchItem {

    private static final int VIEW_TYPE = VIEW_TYPE_PERSON_MORE;


    @Override
    public int getType() {
        return VIEW_TYPE;
    }

    @Override
    public long getViewId() {
        return -VIEW_TYPE;
    }
}
