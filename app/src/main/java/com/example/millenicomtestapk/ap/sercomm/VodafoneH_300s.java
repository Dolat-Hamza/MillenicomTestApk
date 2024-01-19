package com.example.millenicomtestapk.ap.sercomm;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class VodafoneH_300s extends AccessPoint {

    private static final String TAG = VodafoneH_300s.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String MAIN_SUFFIX = "/overview.html";
    private static final String SETTINGS_SUFFIX = "/wifi.html#sub=35";

    private static final String DEFAULT_PASSWORD = "hh9k8a5u";
    private static final String DEFAULT_USERNAME = "admin";

    public VodafoneH_300s(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "document.getElementsByClassName('left')[1].children[0].value = '" + getUsername() + "';" +
                    "document.getElementsByClassName('left')[2].children[0].value = '" + getPassword() + "';" +
                    "document.getElementsByClassName('right')[0].children[0].click();" +
                    "setTimeout(function() { " +
                    "if (document.getElementsByClassName('left')[1].children[0] != null) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "}, 10000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            if (getOptimalChannel() <= 13) {
                return "javascript:{" +
                        initAmbeent()+
                        "var repeatInterval = setInterval(function(){" +
                        "document.getElementById('channelSelect2_4').value = " + getOptimalChannel() + ";" +
                        "document.getElementById('WiFiOnOff_guest_5g3').click();" +
                        "if(document.getElementsByClassName('message-arrowbox')[0].style.display == 'block') {" +
                        "clearInterval(repeatInterval);" +
                        "document.getElementById('WiFiOnOff_guest_5g3').click();" +
                        "document.getElementById('applyButton').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "}, 3000);" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent()+
                        "var repeatInterval = setInterval(function(){" +
                        "document.getElementById('channelSelect_5').value = " + getOptimalChannel() + ";" +
                        "document.getElementById('WiFiOnOff_guest_5g3').click();" +
                        "if(document.getElementsByClassName('message-arrowbox')[0].style.display == 'block') {" +
                        "clearInterval(repeatInterval);" +
                        "document.getElementById('WiFiOnOff_guest_5g3').click();" +
                        "document.getElementById('applyButton').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "}, 3000);" +
                        "};";
            }
        return null;
    }
}