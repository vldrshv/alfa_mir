package ru.alfabank.alfamir.search.data.dto;

import com.google.gson.annotations.SerializedName;

public class SearchResultRaw {
    @SerializedName("peoplesearchdata")
    private PersonRaw[] mPersons;
    @SerializedName("pagesearchdata")
    private PageRaw[] mPages;

    public PersonRaw[] getPersons() {
        return mPersons;
    }

    public PageRaw[] getPages() {
        return mPages;
    }
}
