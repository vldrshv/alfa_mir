package ru.alfabank.alfamir.utility.network;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.util.Log;
import ru.alfabank.alfamir.di.qualifiers.AppContext;

import static android.content.Context.WIFI_SERVICE;

@Singleton
public class IpProviderImp implements IpProvider {

    private Context mContext;

    @Inject
    IpProviderImp(@AppContext Context context){
        mContext = context;
    }

    @Override
    public String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress address : addresses) {
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return  address.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), ex.getMessage());
        }
        return "";
    }

    @Override
    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return  Formatter.formatIpAddress(ip);
    }
}
