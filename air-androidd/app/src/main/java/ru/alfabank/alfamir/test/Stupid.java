package ru.alfabank.alfamir.test;

import com.google.gson.annotations.SerializedName;

public class Stupid {
    @SerializedName("text")
    String text;

    public String getText() {
        return text;
    }
}
