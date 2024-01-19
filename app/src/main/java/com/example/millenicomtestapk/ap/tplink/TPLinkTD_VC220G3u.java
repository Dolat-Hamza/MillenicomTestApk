package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;


public class TPLinkTD_VC220G3u extends AccessPoint {

    private static final String TAG = TPLinkTD_VC220G3u.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "turktelekom";
    //private static final int temp = 36;

    private State state = State.LOGIN_STATE;

    public TPLinkTD_VC220G3u(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {

        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    getSendCode() +
                    "amb_sendcode(document.getElementById('userName'), '" + getUsername() + "').then(function(){" +
                    "amb_sendcode(document.getElementById('pcPassword'), '" + getPassword() + "');}).then(function(){" +
                    "setTimeout(function(){" +
                    "document.getElementById('loginBtn').click();" +
                    "},2000);" +
                    "})" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANNEL_CHANGE_STATE;
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
        } else if (state == State.CHANNEL_CHANGE_STATE)
            if (getOptimalChannel() < 13) {
                String a = "javascript:{" +
                        initAmbeent()+
                        "var repeatInterval2 = setInterval(function(){" +
                        "if(document.getElementById('menu_wl') != null){" +
                        "document.getElementById('menu_wl').click();" +
                        "clearInterval(repeatInterval2);" +
                        "}" +
                        "}, 1200);" +
                        "var repeatInterval3 = setInterval(function(){" +
                        "if(document.getElementById('channel') != null){" +
                        "clearInterval(repeatInterval3);" +
                        "var getVal = document.getElementsByName('channel')[0];" +
                        "getVal.value = '" + getOptimalChannel() + "'; " +
                        "var change = new Event('change');" +
                        "getVal.dispatchEvent(change);" +
                        "window.changeChannel();" +
                        "document.querySelector('input.button.L.T.T_save').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "}, 1200);" +
                        "};";
                return a;
            } else {
                String b = "javascript:{" +
                        initAmbeent()+
                        "setTimeout(function(){" +
                        "document.getElementById('menu_wl').click();" +
                        "setTimeout(function(){" +
                        "document.getElementById('5g').click();" +
                        "setTimeout(function(){" +
                        "var getVal = document.getElementsByName('channel')[0];" +
                        "getVal.value = '" + getOptimalChannel() + "'; " +
                        "setTimeout(function(){" +
                        "var change = new Event('change');" +
                        "getVal.dispatchEvent(change);" +
                        "window.changeChannel();" +
                        "setTimeout(function(){" +
                        "document.querySelector('input.button.L.T.T_save').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}, 1200); " +
                        "}, 1200); " +
                        "}, 1200); " +
                        "}, 2500);" +
                        "}, 2500);" +
                        "};";
                return b;
            }
        return null;
    }

    @Override
    public boolean channelIsValid(int channelType, int scanResultChannelWith, int channel) {
        switch (channelType) {
            case 0: {
                int[][] channelList = {{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}};
                if (scanResultChannelWith > 1) return false;
                List<Integer> list2 = new ArrayList<>();
                for (int a : channelList[scanResultChannelWith]) list2.add(a);
                return list2.contains(channel);
            }
            case 1: {
                int[][] channelList = {{
                        36, 40, 44, 48, 52, 56, 60, 64},
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
        CHANNEL_CHANGE_STATE
    }
}


