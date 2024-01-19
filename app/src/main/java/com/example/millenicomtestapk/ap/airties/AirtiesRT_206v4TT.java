package com.example.millenicomtestapk.ap.airties;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class AirtiesRT_206v4TT extends AccessPoint {

    private static final String TAG = AirtiesRT_206v4TT.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String ERROR_SUFFIX = "/login.html?ErrorCode=6";
    private static final String MAIN_SUFFIX = "/main.html";
    private static final String SETTINGS_SUFFIX = "/wireless/settings.html";

    private static final String DEFAULT_PASSWORD = "ttnet";
    private static final String DEFAULT_USERNAME = "admin";


    public AirtiesRT_206v4TT(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "parent.frames['mainFrame'].document.getElementById('uiPostGetPage').value= '" + getUsername() + "';" +
                    "parent.frames['mainFrame'].document.getElementById('uiPostPassword').value='" + getPassword() + "';" +
                    "parent.frames['mainFrame'].document.getElementById('__ML_ok').click();" +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setInterval(() => {" +
                    "document.getElementById('cmb_channel').value= " + getOptimalChannel() + ";" +
                    "document.getElementById('SaveButton').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1000);" +
                    "};";
        return null;
    }
}
