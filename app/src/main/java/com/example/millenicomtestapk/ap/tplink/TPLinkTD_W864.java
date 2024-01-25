package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkTD_W864 extends AccessPoint {

    private static final String TAG = TPLinkTD_W864.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "ttnet";

    private static final String LOGIN_SUFFIX = "/login.htm";
    private static final String ERROR_SUFFIX = "/login.cgi";
    private static final String DEFAULT_SUFFIX = "/";
    private static final String PASSWORD_SETTINGS_SUFFIX = "/modifyPassword.htm";
    private static final String MAIN_SUFFIX = "/index.htm";
    private static final String SETTINGS_SUFFIX = "/wlbasic.htm";


    public TPLinkTD_W864(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);

    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "document.getElementById('username').value= '" + getUsername() + "';" +
                    "document.getElementById('password').value= '" + getPassword() + "';" +
                    "document.getElementById('loginBtn').click();" +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + DEFAULT_SUFFIX))
            return "javascript:{" +
                    "window.location.href ='" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + PASSWORD_SETTINGS_SUFFIX))
            return "javascript:{" +
                    "window.location.href ='" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href ='" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "document.getElementsByName('chanwid')[0].selectedIndex='0';" +
                    "document.getElementsByName('chan')[0].value=" + getOptimalChannel() + ";" +
                    "setTimeout(() => {" +
                    "document.getElementsByName('save')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 300);" +
                    "};";
        return null;
    }
}