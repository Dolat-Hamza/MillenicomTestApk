package com.example.millenicomtestapk.ap.asus;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class AsusDSL_N17U extends AccessPoint {

    public static final String TAG = AsusDSL_N17U.class.getSimpleName();

    public static final String LOGIN_SUFFIX = "/Main_Login.asp";
    public static final String MAIN_SUFFIX = "/index2.asp";
    public static final String SETTINGS_SUFFIX = "/Advanced_Wireless_Content.asp";
    public static final String ASETTINGS_SUFFIX = "/cgi-bin/Advanced_Wireless_Content.asp";

    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "password";

    public AsusDSL_N17U(String gateway, String username, String password, int callbackPort) {
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
                    "if (document.getElementById('error_status_field').innerHTML.length > 0) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else { " +
                    "document.getElementById('login_username').value = '" + getUsername() + "';" +
                    "document.getElementsByName('login_passwd')[0].value = '" + getPassword() + "';" +
                    "document.getElementsByClassName('button')[0].click();" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + ASETTINGS_SUFFIX) || url.equals(SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementById('applyButton') != null) {" +
                    "document.getElementsByName('wl_channel')[0].value = " + getOptimalChannel() + ";" +
                    "winWidth = 123;" +
                    "applyRule();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "};";

        return null;
    }
}
