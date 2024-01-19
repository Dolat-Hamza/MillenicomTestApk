package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class TPLinkArcherC20 extends AccessPoint {

    private static final String TAG = TPLinkArcherC20.class.getSimpleName();
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private State state = State.LOGIN_STATE;

    public TPLinkArcherC20(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    getSendCode() +
                    "amb_sendcode(document.getElementById('userName'), '" + getUsername() + "').then(function(){" +
                    "amb_sendcode(document.getElementById('pcPassword'), '" + getPassword() + "');}).then(function(){" +
                    "setTimeout(function(){" +
                    "document.getElementById('loginBtn').click();},2000);" +
                    "})" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.SETTINGS_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function(){" +
                    "if(document.getElementById('skipBtn') != null){" +
                    "document.getElementById('skipBtn').click();" +
                    "clearInterval(repeatInterval);" +
                    "}" +
                    "else if(document.getElementsByClassName('noteDiv')[0] != null) {" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else {" +
                    "window.location.href = '" + getFormattedGateway() + "';" +
                    "}" +
                    "}, 1200);" +
                    "};";
        } else if (state == State.SETTINGS_STATE)
            if (getOptimalChannel() > 13) {
                return "javascript:{" +
                        initAmbeent() +
                        "setTimeout(function(){" +
                        "var frame1 = document.getElementById('frame1').contentDocument;" +
                        "if(frame1.getElementById('menu_wl5g') != null){" +
                        "frame1.getElementById('menu_wl5g').click();" +
                        "}" +
                        "},2000);" +
                        "var repeatInterval3 = setInterval(function(){" +
                        "var frame2 = document.getElementById('frame2').contentDocument;" +
                        "if(frame2.getElementById('channel') != null){" +
                        "clearInterval(repeatInterval3);" +
                        "frame2.getElementById('bandWidth')[1].selected = true;" +
                        "frame2.getElementById('bandWidth').onchange();" +
                        "frame2.getElementById('channel').value = " + getOptimalChannel() + ";" +
                        "frame2.getElementById('channel').onchange();" +
                        "frame2.querySelector('input.button.L.T.T_save').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "document.getElementById('menu_logout').click();" +
                        "}" +
                        "}, 1200);" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent() +
                        "setTimeout(function(){" +
                        "var frame1 = document.getElementById('frame1').contentDocument;" +
                        "if(frame1.getElementById('menu_wl2g') != null){" +
                        "frame1.getElementById('menu_wl2g').click();" +
                        "}" +
                        "},2000);" +
                        "var repeatInterval3 = setInterval(function(){" +
                        "var frame2 = document.getElementById('frame2').contentDocument;" +
                        "if(frame2.getElementById('channel') != null){" +
                        "clearInterval(repeatInterval3);" +
                        "frame2.getElementById('bandWidth')[1].selected = true;" +
                        "frame2.getElementById('bandWidth').onchange();" +
                        "frame2.getElementById('channel').value = " + getOptimalChannel() + ";" +
                        "frame2.getElementById('channel').onchange();" +
                        "frame2.querySelector('input.button.L.T.T_save').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "document.getElementById('menu_logout').click();" +
                        "}" +
                        "}, 1200);" +
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
                int[][] channelList = {
                        {36, 40, 44, 48},
                        {36, 40, 44, 48},
                        {36, 40, 44, 48},
                        {36, 40, 44, 48}};
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
        SETTINGS_STATE;
    }
}