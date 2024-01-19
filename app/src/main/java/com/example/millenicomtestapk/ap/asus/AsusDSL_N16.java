package com.example.millenicomtestapk.ap.asus;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class AsusDSL_N16 extends AccessPoint {
    public static final String TAG = AsusDSL_N16.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/Main_Login.asp";
    private static final String MAIN_SUFFIX = "/index2.asp";
    private static final String SETTINGS_SUFFIX = "/Advanced_Wireless_Content.asp";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private boolean loginAttempted = false;

    public AsusDSL_N16(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (!loginAttempted && url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            loginAttempted = true;
            return "javascript:{" +
                    "document.getElementById('login_username').value = '" + getUsername().trim() + "';" +
                    "document.getElementsByName('login_passwd')[0].value = '" + getPassword().trim() + "';" +
                    "login();" +
                    "};";
        }
        if (loginAttempted && url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "document.getElementsByName('wl_channel')[0].value = " + getOptimalChannel() + ";" +
                    "change_channel();" +
                    // work around for invisible webview
                    "winWidth = 123;" +
                    "applyRule();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "};";
        return null;
    }
}
