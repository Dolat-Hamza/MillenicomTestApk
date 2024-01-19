package com.example.millenicomtestapk.ap.nokia;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class NokiaG_240W_C extends AccessPoint {

    public static final String TAG = NokiaG_240W_C.class.getSimpleName();
    private static final String SETTINGS_SUFFIX = "/wlan_config.cgi";
    private static final String FIVEGHZ_SETTINGS_SUFFIX = "/wlan_config.cgi?v=11ac";
    private static final String DEFAULT_USERNAME = "VodAdmin";
    private static final String DEFAULT_PASSWORD = "Voda556Net12";
    private State state = State.LOGIN_STATE;

    public NokiaG_240W_C(String gateway, String username, String password, int callbackPort) {
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
                    "document.getElementById('username').value = '" + getUsername() + "';" +
                    "document.getElementById('password').value = '" + getPassword() + "';" +
                    "setTimeout(() => {" +
                    "document.querySelectorAll('input')[2].click();" +
                    "}, 300);" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANGE_CHANNEL_STATE;
            if (getOptimalChannel() <= 13)
                return "javascript:{" +
                        initAmbeent()+
                        "if (document.getElementById('username') != null) {" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "else {" +
                        "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                        "}" +
                        "}";
            else
                return "javascript:{" +
                        "window.location.href = '" + getFormattedGateway() + FIVEGHZ_SETTINGS_SUFFIX + "';" +
                        "};";
        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX)) {

            new NotifyLogin().execute();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new NotifySuccess().execute();
                }
            }, 3000);


            return "javascript:{" +
                    initAmbeent()+
                    "var repeat = setInterval(() => {" +
                    "if (document.getElementsByName('wl_channel') != null) {" +
                    "document.getElementsByName('wl_NChannelwidth')[0].value = 0;" +
                    "document.getElementsByName('wl_channel')[0].value = " + getOptimalChannel() + ";" +
                    "clearInterval(repeat);" +
                    "document.getElementsByClassName('buttonX button-sm')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 2500);" +
                    "};";
        } else if (url.equals(getFormattedGateway() + FIVEGHZ_SETTINGS_SUFFIX)) {

            new NotifyLogin().execute();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new NotifySuccess().execute();
                }
            }, 5000);

            return "javascript:{" +
                    initAmbeent()+
                    "var repeat = setInterval(() => {" +
                    "if (document.getElementsByName('wl_channel') != null) {" +
                    "document.getElementsByName('wl_channel')[0].value = " + getOptimalChannel() + ";" +
                    "clearInterval(repeat);" +
                    "document.getElementsByClassName('buttonX button-sm')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 2500);" +
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
                        36,40,44,48,52,56,60,
                                64,100,104,108,112,116,
                                120,124,128,132,136
                },
                        {36,40,44,48,52,56,60,
                                64,100,104,108,112,116,
                                120,124,128,132,136},
                        {36,40,44,48,52,56,60,
                                64,100,104,108,112,116,
                                120,124,128},
                        {36,40,44,48,52,56,60,
                                64,100,104,108,112,116,
                                120,124,128}};
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
        CHANGE_CHANNEL_STATE,
    }
}