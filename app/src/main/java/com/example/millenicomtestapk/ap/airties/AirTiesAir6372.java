package com.example.millenicomtestapk.ap.airties;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class AirTiesAir6372 extends AccessPoint {

    private static final String TAG = AirTiesAir6372.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String ERROR_SUFFIX = "/login.html?ErrorCode=6&redirect=";
    private static final String MAIN_SUFFIX = "/main.html";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "superonline";


    public AirTiesAir6372(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setPassword(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "parent.frames['mainFrame'].document.getElementById('uiPostGetPage').value = '" + getUsername() + "';" +
                    "parent.frames['mainFrame'].document.getElementById('uiPostPassword').value = '" + getPassword() + "';" +
                    "setTimeout(function () {" +
                    "parent.frames['mainFrame'].document.getElementById('__ML_ok').click();" +
                    "}, 300); " +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function () {" +
                    "if (parent.frames['mainFrame'].document.getElementById('cmb_channel') == null){" +
                    "parent.frames['menuFrame'].document.getElementById('__ML_wireless').click(); " +
                    "parent.frames['menuFrame'].document.getElementById('__ML_wireless_setup').click();" +
                    "}" +
                    "else {" +
                    "parent.frames['mainFrame'].document.getElementById('cmb_channel')[" + getOptimalChannel() + "].selected=true;" +
                    "parent.frames['mainFrame'].document.getElementById('SaveButton').click();" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 2000); " +
                    "};";
        return null;
    }
}

