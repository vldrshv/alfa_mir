package ru.alfabank.alfamir.utility.static_utilities;

/**
 * Created by U_M0WY5 on 17.05.2018.
 */

public class NetworkConnectionStatus {

    private static NetworkConnectionStatus INSTANCE;
    private static boolean isNetworkConnectionPresent = true;

    public static NetworkConnectionStatus getInstance(){
        if(INSTANCE==null){
            INSTANCE = new NetworkConnectionStatus();
        }
        return INSTANCE;
    }

    public boolean isConnected(){
        return isNetworkConnectionPresent;
    }

    public void reportConnectionLost(){
        isNetworkConnectionPresent = false;
    }

    public void reportConnectionAvailible(){
        isNetworkConnectionPresent = true;
    }

}
