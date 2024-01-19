package com.example.millenicomtestapk.ap.arris;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ArrisNVG589 extends AccessPoint {

    private static final String LOGIN_SUFFIX = "/cgi-bin/home.ha";
    private static final String ERROR_SUFFIX = "/cgi-bin/login.ha";
    private static final String SETTINGS_SUFFIX = "/cgi-bin/wconfig.ha";
    private static final String APPROVE_SUFFIX = "/cgi-bin/wifiwarn.ha";
    private static final String DEFAULT_PASSWORD = "<1*4*66&6#";
    public String TAG = ArrisNVG589.class.getSimpleName();

    public ArrisNVG589(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementsByName('Continue').length > 0) {" +
                    "document.getElementById('password').value = '" + getPassword() + "';" +
                    "document.getElementsByName('Continue')[0].click();" +
                    "} else {" +
                    "document.getElementById('channel').value = " + getOptimalChannel() + ";" +
                    "document.getElementsByName('Save')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + APPROVE_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "document.getElementsByName('Continue')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "};";
        return null;
    }
}
