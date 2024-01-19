package com.example.millenicomtestapk.ap.netgear;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class NetgearR6080 extends AccessPoint {

    public static final String TAG = NetgearR6080.class.getSimpleName();
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String LOGIN_SUFFIX = "/";
    public static final String MAIN_SUFFIX = "/index.htm";
    public static final String RECOVERY_SUFFIX = "/401_recovery.htm";
    private State state = State.LOGIN_STATE;

    public NetgearR6080(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        Log.d(TAG, "State : " + state);

        // Checking already loggedIn or not
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            return "javascript:{" +
                    "if (document.getElementById('formframe') == null){" +
                    "window.location.href = '" + getFormattedGateway() + RECOVERY_SUFFIX + "';" +
                    "}" +
                    "else {" +
                    "window.location.href = '" + getFormattedGateway() + MAIN_SUFFIX + "';" +
                    "}" +
                    "};";
        }
        // Loging In with basic authentication because of popup
        else if (url.equals(getFormattedGateway() + RECOVERY_SUFFIX)) {
            state = State.RESTART_STATE;
            return "javascript:{" +
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getPassword() + "@' + pure.slice(7) + '/index.htm';" +
                    "window.location.href = link;" +
                    "};";

        }
        // Checking username-password and changing url to normal format
        else if (state == State.RESTART_STATE) {
            state = State.SETTINGS_STATE;
            return "javascript:{" +
                    initAmbeent() +
                    "if (document.getElementById('wireless') == null){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "location.replace('" + getFormattedGateway() + MAIN_SUFFIX + "');" +
                    "}" +
                    "};";
        }
        // Optimazing channel
        else if ((state == State.SETTINGS_STATE) || (url.equals(getFormattedGateway() + MAIN_SUFFIX))) {
            state = State.APPLY_STATE;
            if (getOptimalChannel() <= 11) {
                //For 2.4ghz
                return "javascript:{" +
                        initAmbeent() +
                        "document.getElementById('wireless').click();" +
                        "var repeatInterval = setInterval(function(){" +
                        "if(document.getElementById('formframe').contentDocument.getElementById('2g_setting').contentDocument.getElementsByName('w_channel')[0] != null){" +
                        "document.getElementById('formframe').contentDocument.getElementById('2g_setting').contentDocument.getElementsByName('w_channel')[0][" + getOptimalChannel() + "].selected = true;" +
                        "document.getElementById('formframe').contentDocument.getElementsByName('Apply')[0].click();" +
                        "}" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "clearInterval(repeatInterval);" +
                        "}, 10000);" +
                        "};";
            } else {
                // For 5ghz
                return "javascript:{" +
                        initAmbeent() +
                        "document.getElementById('wireless').click();" +
                        "var repeatInterval = setInterval(function(){" +
                        "if(document.getElementById('formframe').contentDocument.getElementById('5g_setting').contentDocument.getElementsByName('w_channel_an')[0] != null){" +
                        "document.getElementById('formframe').contentDocument.getElementById('5g_setting').contentDocument.getElementsByName('w_channel_an')[0][" + getOptimalChannel() + "].selected = true;" +
                        "document.getElementById('formframe').contentDocument.getElementsByName('Apply')[0].click();" +
                        "}" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "clearInterval(repeatInterval);" +
                        "}, 10000);" +
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
                        149,
                        153,
                        157,
                        161,
                        165
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
        RESTART_STATE,
        SETTINGS_STATE,
        APPLY_STATE
    }
}