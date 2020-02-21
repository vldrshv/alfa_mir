package ru.alfabank.alfamir.data.dto.old_trash.models;

/**
 * Created by U_m0wy5 on 15.01.2018.
 */

public class SubscriptionModel {
    String subid;
    String subtype;

    public SubscriptionModel(){};

    public SubscriptionModel(String subid, String subtype){
        this.subid = subid;
        this.subtype = subtype;
    }

    public String getSubid() {
        return subid;
    }

    public String getSubtype() {
        return subtype;
    }
}
