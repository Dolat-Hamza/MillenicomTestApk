package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class TPLinkEAP225 extends AccessPoint {

    private static final String TAG = TPLinkEAP225.class.getSimpleName();
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";
    private State state = State.LOGIN_STATE;

    public TPLinkEAP225(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, "State : " + state);
        Log.d(TAG, url);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "document.getElementById('login-username').value = '" + getUsername() + "';" +
                    "document.getElementById('login-password').value = '" + getPassword() + "';" +
                    "document.getElementById('login-btn').click();" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.SETTINGS_STATE;
            return "javascript:{" +
                    initAmbeent() +
                    "if (document.getElementById('login-password') != null){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "document.getElementsByName('wireless')[0].click();" +
                    "}" +
                    "};";
        } else if (state == State.SETTINGS_STATE) {
            if (getOptimalChannel() < 14) {
                return "javascript:{" +
                        initAmbeent() +
                        "var repeatInterval = setInterval(function(){" +
                        "document.getElementsByClassName('combobox-list')[8].click();" +
                        "document.getElementsByClassName('combobox-list')[8].children[" + getOptimalChannel() + "].children[0].click();" +
                        "document.getElementsByClassName('button-button')[2].click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "clearInterval(repeatInterval);" +
                        "}, 1200);" +
                        "};";
            } else {
                String channel_suffix = "";
                switch (getOptimalChannel()) {
                    case 36:
                        channel_suffix = "1";
                        break;
                    case 40:
                        channel_suffix = "2";
                        break;
                    case 44:
                        channel_suffix = "3";
                        break;
                    case 48:
                        channel_suffix = "4";
                        break;
                    case 52:
                        channel_suffix = "5";
                        break;
                    case 56:
                        channel_suffix = "6";
                        break;
                    case 60:
                        channel_suffix = "7";
                        break;
                    case 64:
                        channel_suffix = "8";
                        break;
                    case 100:
                        channel_suffix = "9";
                        break;
                    case 104:
                        channel_suffix = "10";
                        break;
                    case 108:
                        channel_suffix = "11";
                        break;
                    case 112:
                        channel_suffix = "12";
                        break;
                    case 116:
                        channel_suffix = "13";
                        break;
                    case 120:
                        channel_suffix = "14";
                        break;
                    case 124:
                        channel_suffix = "15";
                        break;
                    case 128:
                        channel_suffix = "16";
                        break;
                    case 132:
                        channel_suffix = "17";
                        break;
                    case 136:
                        channel_suffix = "18";
                        break;
                    case 140:
                        channel_suffix = "19";
                        break;
                }
                return "javascript:{" +
                        getLocalHostLoginScript() +
                        "document.getElementById('show_5g').click();" +
                        "var repeatInterval = setInterval(function(){" +
                        "document.getElementsByClassName('combobox-list')[32].click();" +
                        "document.getElementsByClassName('combobox-list')[32].children[" + channel_suffix + "].children[0].click();" +
                        "document.getElementsByClassName('button-button')[3].click();" +
                        "Ambeent.getOptimalChannel('" + getOptimalChannel() + "');" +
                        //getLocalHostSuccessScript() +
                        "clearInterval(repeatInterval);" +
                        "}, 1200);" +
                        "};";
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
                int[][] channelList = {{
                        36,
                        40,
                        44,
                        48,
                        52,
                        56,
                        60,
                        64,
                        100,
                        104,
                        108,
                        112,
                        116,
                        120,
                        124,
                        128,
                        132,
                        136,
                        140
                },
                        {},
                        {},
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

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        SETTINGS_STATE;
    }
}
