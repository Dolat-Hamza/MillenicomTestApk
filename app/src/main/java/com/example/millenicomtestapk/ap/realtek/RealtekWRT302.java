package com.example.millenicomtestapk.ap.realtek;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;


public class RealtekWRT302 extends AccessPoint {


    private static final String TAG = RealtekWRT302.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/";
    private static final String SETTINGS_SUFFIX = "/wlbasic.htm";
    private static final String APPLY_SUFFIX = "/boafrm/formWlanSetup";


    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private static int BGN;

    public RealtekWRT302(String gateway, String password, String username, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);


    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getPassword() + "@' + pure.slice(7) + '/wlbasic.htm';" +
                    "window.location.href = link;" +
                    "};";

        else if (url.contains(SETTINGS_SUFFIX)) {
            if (getOptimalChannel() < 5) {
                BGN = 2;
            } else {
                BGN = 10;
            }
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementsByName('band0')[0] == null){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "var repeatInterval = setInterval(function(){" +
                    "document.getElementsByName('band0')[0].value = '" + BGN + "';" +
                    "document.getElementsByName('band0')[0].onchange();" +
                    "document.getElementsByName('chan0')[0].value = '" + getOptimalChannel() + "';" +
                    "document.getElementsByName('save')[0].click();" +
                    "clearInterval(repeatInterval);" +
                    "}, 2000);" +
                    "}" +
                    "}";
        } else if (url.contains(APPLY_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "document.getElementById('restartNow').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "};";
        return null;
    }
}
