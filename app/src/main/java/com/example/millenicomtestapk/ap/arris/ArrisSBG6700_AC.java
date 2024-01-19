package com.example.millenicomtestapk.ap.arris;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class ArrisSBG6700_AC extends AccessPoint {

    public static final String TAG = ArrisSBG6700_AC.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/";
    private static final String ERROR_SUFFIX = "/login.asp";
    private static final String MAIN_SUFFIX = "/home.asp";
    private static final String SETTINGS_SUFFIX = "/wlanRadio.asp";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    private boolean is2GHz = true;
    private boolean isDone = false;

    public ArrisSBG6700_AC(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "document.getElementsByName('loginUsername')[0].value = '" + getUsername() + "';" +
                    "document.getElementsByName('loginPassword')[0].value = '" + getPassword() + "';" +
                    "document.getElementsByName('login')[0].submit();" +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX) && is2GHz && !isDone) {
            if (getOptimalChannel() <= 13) {
                isDone = true;
                return "javascript:{" +
                       initAmbeent()+
                        "document.getElementsByName('ChannelNumber')[0].selectedIndex = " + getOptimalChannel() + ";" +
                        "commitRadio();submitPage();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "};";
            } else {
                is2GHz = false;
                return "javascript:{" +
                        "document.getElementById('r2Tab').click();" +
                        "};";
            }
        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX) && !is2GHz && !isDone) {

            isDone = true;
            return "javascript:{" +
                    initAmbeent()+
                    "document.getElementsByName('ChannelNumber')[0].value = " + getOptimalChannel() + ";" +
                    "commitRadio();submitPage();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "};";

        }
        return null;
    }

    @Override
    public boolean channelIsValid(int channelType, int scanResultChannelWith, int channel) {
        switch (channelType) {
            case 0: {
                int[][] channelList = {{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13},
                        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}};
                if (scanResultChannelWith > 1) return false;
                List<Integer> list2 = new ArrayList<>();
                for (int a : channelList[scanResultChannelWith]) list2.add(a);
                return list2.contains(channel);
            }
            case 1: {
                int[][] channelList = {{
                        36,40,44,48,149,153,157,161,165
                },
                        {36-44-149-157},
                        {36-149-40-153-44-157-48-161},
                        {36-149-40-153-44-157-48-161}};
                if (scanResultChannelWith > 3) return false;
                List<Integer> list = new ArrayList<>();
                for (int a : channelList[scanResultChannelWith]) list.add(a);
                return list.contains(channel);
            }
            default:
                return false;
        }
    }
}
