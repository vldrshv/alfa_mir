package ru.alfabank.alfamir.alfa_tv.presentation.dto;

import static ru.alfabank.alfamir.Constants.Show.SHOW_SEPARATOR;

public class ShowSeparator implements ShowListElement {

    private static final int VIEW_TYPE = SHOW_SEPARATOR;
    private String mDate;

    public ShowSeparator(String date){
        mDate = date;
    }

    public String getDate() {
        return mDate;
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE;
    }


}
