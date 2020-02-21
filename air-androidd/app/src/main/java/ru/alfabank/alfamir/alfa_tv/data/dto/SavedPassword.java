package ru.alfabank.alfamir.alfa_tv.data.dto;

public class SavedPassword {

    private int mId;
    private String mPassword;

    public SavedPassword(int id, String password){
        mId = id;
        mPassword = password;
    }

    public int getId() {
        return mId;
    }

    public String getPassword() {
        return mPassword;
    }
}
