package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class TPLinkArcherC5V extends AccessPoint {

    private static final String TAG = TPLinkArcherC5V.class.getSimpleName();
    public static final String LOGIN_SUFFIX = "/";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private State state = State.LOGIN_STATE;

    public TPLinkArcherC5V(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE;
                String a = "javascript:{" +
                        getSendCode() +
                        "amb_sendcode(document.getElementById('userName'), '" + getUsername() + "').then(function(){" +
                        "amb_sendcode(document.getElementById('pcPassword'), '" + getPassword() + "');}).then(function(){" +
                        "setTimeout(function(){" +
                        "document.getElementById('loginBtn').click();" +
                        "},2000);" +
                        "})" +
                        "};";
                return a;

            } else if (state == State.CREDENTIAL_CHECK_STATE) {
                state = State.SETTINGS_STATE;
                String a = "javascript:{" +
                        initAmbeent() +
                        "setTimeout(function(){" +
                        "if(document.getElementsByClassName('noteDiv')[0].innerHTML.length == 118)" +
                        "{" +
                        "window.skip();" +
                        "}" +
                        "setTimeout(function(){" +
                        "if(document.getElementById('note')){" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "},3000);" +
                        "},3000);" +
                        "};";
                return a;
            } else if (state == State.SETTINGS_STATE) {

                String a = "setTimeout(function(){" +
                        "if (document.getElementsByClassName('ml1 ez')[0].innerHTML.length == 1275){";
                if (getOptimalChannel() < 14) {

                    a += initAmbeent() +
                            "if(document.getElementsByClassName('ml1')[5]){" +
                            "setTimeout(function(){" +
                            "document.getElementById('menu_wl').click();" +
                            "setTimeout(function(){" +
                            "var combo = document.getElementById('channel');" +
                            "combo.value = " + getOptimalChannel() + ";" +
                            "var kullan = new Event('change');" +
                            "combo.dispatchEvent(kullan);" +
                            "setTimeout(function(){" +
                            " document.getElementsByClassName('button L T T_save')[0].click();" +
                            "setTimeout(function() {" +
                            ambeent(String.valueOf(getOptimalChannel())) +
                            "},2000);" +
                            "},2000);" +
                            "},2000);" +
                            "},2000);" +
                            "}";
                } else {

                    a += initAmbeent() +
                            "if(document.getElementsByClassName('ml1')[5]){" +
                            "setTimeout(function(){" +
                            "document.getElementById('menu_wl').click();" +
                            "setTimeout(function(){" +
                            "document.getElementById('5g').click();" +
                            "setTimeout(function(){" +
                            "var combo = document.getElementById('channel');" +
                            "combo.value = " + getOptimalChannel() + ";" +
                            "var kullan = new Event('change');" +
                            "combo.dispatchEvent(kullan);" +
                            "setTimeout(function(){" +
                            "document.getElementsByClassName('button L T T_save')[0].click();" +
                            "setTimeout(function() {" +
                            ambeent(String.valueOf(getOptimalChannel())) +
                            "},2000);" +
                            "},2000);" +
                            "},2000);" +
                            "},2000);" +
                            "},2000);" +
                            "}";
                }
                a += "}" +
                        "},4000);";
                return a;
            }
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
                int[][] channelList = {
                        {36, 40, 44, 48, 52, 56, 60, 64},
                        {36, 40, 44, 48, 52, 56, 60, 64},
                        {36, 40, 44, 48, 52, 56, 60, 64},
                        {36, 40, 44, 48, 52, 56, 60, 64}};
                if (scanResultChannelWith > 3) return false;
                List<Integer> list = new ArrayList<>();
                for (int a : channelList[scanResultChannelWith]) list.add(a);
                return list.contains(channel);
            }
            default:
                return false;
        }
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        SETTINGS_STATE
    }
}
