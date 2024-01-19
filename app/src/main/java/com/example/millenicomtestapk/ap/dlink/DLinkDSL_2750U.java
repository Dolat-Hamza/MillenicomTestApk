package com.example.millenicomtestapk.ap.dlink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class DLinkDSL_2750U extends AccessPoint {

    public static final String TAG = DLinkDSL_2750U.class.getSimpleName();

    public static final String LOGIN_SUFFIX = "/";
    public static final String MAIN_SUFFIX = "/#start/storInfo";
    public static final String SETTINGS_SUFFIX = "/#wifi/basic";

    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "password";

    public DLinkDSL_2750U(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setInterval(function(){" +
                    "if (document.getElementById('message').children[0].style.display != 'inline'){" +
                    "document.getElementById('A1').value = '" + getUsername() + "';" +
                    "document.getElementById('A2').value = '" + getPassword() + "';" +
                    "document.getElementById('enter').click();" +
                    "}" +
                    "else {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "}, 2000);" +
                    "};";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "if (document.getElementById('menu') != null){" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "}" +
                    "};";

        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "document.getElementsByClassName('nodecontent lightUI')[9].children[0].children[1].children[0][" + getOptimalChannel() + "].selected = true;" +
                    "document.getElementById('pageToolbarButtons').children[0].children[0].children[0].click();" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1500);" +
                    "};";

        return null;
    }
}
