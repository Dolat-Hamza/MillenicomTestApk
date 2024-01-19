package com.example.millenicomtestapk.ap.castlenet;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class CastlenetInfinity401 extends AccessPoint {

    public static final String TAG = CastlenetInfinity401.class.getSimpleName();

    public static final String DEFAULT_USERNAME = "vodafone";
    public static final String DEFAULT_PASSWORD = "vodafone";

    public static final String LOGIN_SUFFIX = "/";
    public static final String ERROR_SUFFIX = "/login.asp";
    public static final String CREDENTIALS_SUFFIX = "/RgPopup.asp";
    public static final String SETUP_SUFFIX = "/easySetup.asp";
    public static final String MAIN_SUFFIX = "/RgSwInfo.asp";
    public static final String SETTINGS_SUFFIX = "/wlanRadio.asp";

    public static final String fiveG = "/80";

    public CastlenetInfinity401(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            return "javascript:{" +
                    "document.getElementsByName('loginUsername')[0].value ='" + getUsername() + "';" +
                    "document.getElementsByName('loginPassword')[0].value = '" + getPassword() + "';" +
                    "document.getElementById('btnLogin').click();" +
                    "};";
        } else if (url.equals(getFormattedGateway() + ERROR_SUFFIX)) {
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        } else if (url.equals(getFormattedGateway() + CREDENTIALS_SUFFIX)) {
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        } else if (url.equals(getFormattedGateway() + SETUP_SUFFIX)) {
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        } else if (url.equals(getFormattedGateway() + MAIN_SUFFIX)) {
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX)) {
            if (getOptimalChannel() <= 13) {
                return "javascript:{" +
                        initAmbeent()+
                        "var repeatInterval = setInterval(function(){" +
                        "if (document.getElementsByName('WirelessMacAddress')[0].value == 0) {" +
                        "clearInterval(repeatInterval);" +
                        "document.getElementById('channelList').value = '" + getOptimalChannel() + "';" +
                        "document.getElementById('btnApply').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "else {" +
                        "document.getElementsByName('WirelessMacAddress')[0].value = 0;" +
                        "document.getElementById('btnApply').click();" +
                        "}" +
                        "}, 1200);" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent()+
                        "var repeatInterval = setInterval(function(){" +
                        "if (document.getElementsByName('WirelessMacAddress')[0].value == 1) {" +
                        "clearInterval(repeatInterval);" +
                        "document.getElementsByName('ChannelNumber')[0].value ='" + getOptimalChannel() + fiveG + "';" +
                        "document.getElementById('btnApply').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "else {" +
                        "document.getElementsByName('WirelessMacAddress')[0].value = 1;" +
                        "document.getElementById('btnApply').click();" +
                        "}" +
                        "}, 1200);" +
                        "};";
            }
        }
        return null;
    }
}