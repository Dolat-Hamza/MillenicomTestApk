package com.example.millenicomtestapk.ap.tenda;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class TendaV1200 extends AccessPoint {

    private static final String TAG = TendaV1200.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String LOGIN_SUFFIX2 = "/";
    private static final String MAIN_SUFFIX = "/quickset.html";
    private static final String WSETTINGS_SUFFIX = "/main.html#info";
    private static final String LOGIN_ERROR_SUFFIX = "/login.html?0";
    private static final String WiSETTINGS_SUFFIX2_4 = "/wlswitchinterface0.wl";
    private static final String WiSETTINGS_SUFFIX5G = "/wlswitchinterface1.wl";

    //private static final int temp = 40;
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "adminn";

    public TendaV1200(String gateway, String password, String username, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX) || url.equals(getFormattedGateway() + LOGIN_SUFFIX2))
            return "javascript:{" +
                    getSendCode() +
                    "amb_sendcode(document.getElementById('login-username'), '" + getUsername() + "').then(function(){" +
                    "amb_sendcode(document.getElementById('login-password'), '" + getPassword() + "');}).then(function(){" +
                    "setTimeout(function(){" +
                    "document.getElementById('submit').click();" +
                    "},2000);" +
                    "})" +
                    "};";
        else if (url.equals(getFormattedGateway() + LOGIN_ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + WSETTINGS_SUFFIX + "';" +
                    "}";
        else if (url.equals(getFormattedGateway() + WSETTINGS_SUFFIX))
            if (getOptimalChannel() < 13) {
                return "javascript:{" +
                        "window.location.href = '" + getFormattedGateway() + WiSETTINGS_SUFFIX2_4 + "';" +
                        "}";
            } else {
                return "javascript:{" +
                        "window.location.href = '" + getFormattedGateway() + WiSETTINGS_SUFFIX5G + "';" +
                        "}";
            }
        else if (url.equals(getFormattedGateway() + WiSETTINGS_SUFFIX2_4)) {
            String a = "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function(){" +
                    "var i = 0 ;" +
                    "var reapeatInterval = setInterval(function(){" +
                    "console.log('sure calisti');" +
                    "if(document.getElementById('wlchannel')){" +
                    "i++;" +
                    "var channel = document.getElementById('wlchannel');" +
                    "channel.value = '" + getOptimalChannel() + "';" +
                    "var evnt = new Event('change');" +
                    "channel.dispatchEvent(evnt);" +
                    "}" +
                    "if(i == 1){" +
                    "window.btnApply();" +
                    "parent.frames['logofrm'].document.getElementById('logout').click();" +
                    "}" +
                    "if(i == 2){" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "clearInterval(repeatInterval2);" +
                    "}" +
                    "},  2000);" +
                    "},2000);" +
                    "};";
            return a;
        } else if (url.equals(getFormattedGateway() + WiSETTINGS_SUFFIX5G)) {
            String b = "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function(){" +
                    "var i = 0 ;" +
                    "var reapeatInterval = setInterval(function(){" +
                    "console.log('sure calisti');" +
                    "if(document.getElementById('wlchannel')){" +
                    "i++;" +
                    "var channel = document.getElementById('wlchannel');" +
                    "channel.value = '" + getOptimalChannel() + "';" +
                    "var evnt = new Event('change');" +
                    "channel.dispatchEvent(evnt);" +
                    "}" +
                    "if(i == 1){" +
                    "window.btnApply();" +
                    "parent.frames['logofrm'].document.getElementById('logout').click();" +
                    "}" +
                    "if(i == 2){" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "clearInterval(repeatInterval2);" +
                    "}" +
                    "}, 2000);" +
                    "}, 2000);" +
                    "};";
            return b;
        }
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
