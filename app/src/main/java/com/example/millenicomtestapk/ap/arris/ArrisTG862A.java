package com.example.millenicomtestapk.ap.arris;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ArrisTG862A extends AccessPoint {

    public static final String TAG = ArrisTG862A.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/";
    private static final String SETTINGS_SUFFIX = "/?wifi_basic";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    private boolean loginAttempted = false;
    private boolean pageChangeAttempted = false;
    private boolean channelChangeAttempted = false;

    public ArrisTG862A(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (!loginAttempted && (url.equals(getFormattedGateway() + LOGIN_SUFFIX))) {
            loginAttempted = true;
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function () {" +
                    "if (login('admin', '" + getPassword() + "')) {" +
                    "document.getElementById('UserName').value = '" + getUsername() + "';" +
                    "document.getElementById('Password').value = '" + getPassword() + "';" +
                    "document.getElementsByClassName('submitBtn')[0].click();" +
                    "setTimeout(function () {" +
                    "console.log('');" +
                    "}, 12000); " +
                    "} else {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "}, 1000); " + "}";
        } else if (loginAttempted && !pageChangeAttempted && (url.equals(getFormattedGateway() + LOGIN_SUFFIX))) {
            pageChangeAttempted = true;
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        } else if ((url.equals(getFormattedGateway() + SETTINGS_SUFFIX)) && !channelChangeAttempted) {
            channelChangeAttempted = true;
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function() {" +
                    "if(document.getElementById('Channel') != null){" +
                    "document.getElementById('Channel').selectedIndex = " + getOptimalChannel() + ";" +
                    "document.getElementsByClassName('submitBtn')[0].click();" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 1000);" +
                    "};";
        }
        return null;
    }
}