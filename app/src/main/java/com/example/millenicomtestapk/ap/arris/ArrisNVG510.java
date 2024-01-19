package com.example.millenicomtestapk.ap.arris;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ArrisNVG510 extends AccessPoint {

    public static final String TAG = ArrisNVG510.class.getSimpleName();

    public static final String MAIN_SUFFIX = "/cgi-bin/home.ha";
    public static final String WCONFIG_SUFFIX = "/cgi-bin/wconfig.ha";
    public static final String INCORRECT_LOGIN = "/cgi-bin/login.ha";
    public static final String LAST_WARNING = "/cgi-bin/wifiwarn.ha";
    private static final String DEFAULT_PASSWORD = "5232600926";

    public ArrisNVG510(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + WCONFIG_SUFFIX + "';" +
                    "};";

        else if (url.equals(getFormattedGateway() + INCORRECT_LOGIN))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";

        else if (url.equals(getFormattedGateway() + LAST_WARNING))
            return "javascript:{" +
                    "document.getElementsByName('Continue')[0].click();" +
                    "};";

        else if (url.equals(getFormattedGateway() + WCONFIG_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementsByName('channelplusauto').length == 1) {" +
                    "document.getElementsByName('channelplusauto')[0].value = " + getOptimalChannel() + ";" +
                    "document.getElementsByName('Save')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "} else if (document.getElementsByName('password').length == 1) {" +
                    "document.getElementsByName('password')[0].value = '" + getPassword() + "';" +
                    "document.getElementsByName('Continue')[0].click();" +
                    "}" +
                    "};";

        return null;
    }
}
