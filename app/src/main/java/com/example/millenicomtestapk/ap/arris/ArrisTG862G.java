package com.example.millenicomtestapk.ap.arris;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class ArrisTG862G extends AccessPoint {

    public static final String TAG = ArrisTG862G.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/";
    private static final String SETTINGS_SUFFIX = "/?wifi_basic";

    private static final String DEFAULT_PASSWORD = "password";

    private boolean loginAttempted = false;

    public ArrisTG862G(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (!loginAttempted && url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            loginAttempted = true;
            return "javascript:{" +
                    initAmbeent() +
                    "if (login('admin', '" + getPassword() + "')) {" +
                    "document.getElementById('UserName').value = '" + getUsername() + "';" +
                    "document.getElementById('Password').value = '" + getPassword() + "';" +
                    "document.getElementsByClassName('submitBtn')[0].click();" +
                    "} else {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "};";
        }
        if (loginAttempted && url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function() {" +
                    "if(document.getElementById('Channel') != null){" +
                    "document.getElementById('Channel').selectedIndex = " + getOptimalChannel() + ";" +
                    "document.getElementsByClassName('submitBtn')[0].click();" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 1000);" +
                    "};";
        return null;
    }

    @Override
    public boolean channelIsValid(int channelType, int scanResultChannelWith, int channel) {
        switch (channelType) {
            case 0: {
                int[][] channelList = {{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
                        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}};
                if (scanResultChannelWith > 1) return false;
                List<Integer> list2 = new ArrayList<>();
                for (int a : channelList[scanResultChannelWith]) list2.add(a);
                return list2.contains(channel);
            }
            case 1: {
                int[][] channelList = {{
                        36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165
                },
                        {36, 44, 52, 60, 100, 108, 116, 124, 132, 140, 149, 157},
                        {36, 40, 44, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161},
                        {}};
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
