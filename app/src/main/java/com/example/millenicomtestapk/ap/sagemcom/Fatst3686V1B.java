package com.example.millenicomtestapk.ap.sagemcom;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class Fatst3686V1B extends AccessPoint {

    private static final String INTRO_SUFFIX = "/";
    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String SECURITY_SUFFIX = "/config.html";
    private static final String WIFI_SUFFIX = "/wifi.html";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "QMTMJNE9U2M1";

    private boolean loginAttempted = false;
    private boolean channelChanged = false;

    public Fatst3686V1B(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        if (url.equals(getFormattedGateway() + INTRO_SUFFIX)) {
            return "javascript:{" +
                    "document.getElementsByClassName('num-button2')[0].click();}";
        }

        else if ((url.equals(getFormattedGateway() + LOGIN_SUFFIX)) && !loginAttempted && !channelChanged) {
            loginAttempted = true;

            return "javascript:{" +
                    "document.getElementsByName('loginUsername')[0].value = '" + getUsername() + "';" +
                    "document.getElementsByName('loginPassword')[0].value = '" + getPassword() + "';" +
                    "document.getElementsByClassName('num-button2')[0].click();"+"}";
        }

        else if (loginAttempted && url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            loginAttempted = false;
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        }

        else if (url.equals(getFormattedGateway() + SECURITY_SUFFIX)) {
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + WIFI_SUFFIX  + "';}";
        }

        else if (url.equals(getFormattedGateway() + WIFI_SUFFIX)) {
            channelChanged = true;
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function () {" +
                    "document.getElementById('RgWiFi2GChannel').value = " + getOptimalChannel() + ";" +
                    "}, 1000); " +

                    "setTimeout(function () {" +
                    "document.getElementById('RgWiFi5GChannel').value = " + getOptimalChannel() + ";" +
                    "}, 3000); " +

                    "setTimeout(function () {" +
                    "document.getElementById('RgWiFiApplyConfig').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 5000); " +
                    "setTimeout(function () {" +
                    "document.getElementsByClassName('menulogout')[0].click();" +
                    //getLocalHostSuccessScript() +
                    "}, 15000); "+"}";
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
                        36,40,44,48,52,56,60,64,100,104,108,112,116,132,136,140
                },
                        {36,44,52,60,100,108,132},
                        {36,52,100},
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