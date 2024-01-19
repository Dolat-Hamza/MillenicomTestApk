package com.example.millenicomtestapk.ap.airties;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class AirTiesRT_205 extends AccessPoint {

    private static final String TAG = AirTiesRT_205.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/";
    private static final String MAIN_SUFFIX = "/cgi-bin/webcm";
    private static final String SETTINGS_SUFFIX = "/cgi-bin/webcm?getpage=../html/wireless/index.html&var:pagename=wireless_setup";

    private static final String DEFAULT_PASSWORD = "";


    public AirTiesRT_205(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "parent.frames['mainFrame'].document.getElementsByName('login:command/password')[0].value ='" + getPassword() + "';" +
                    "parent.frames['mainFrame'].document.getElementsByClassName('inputButton')[0].click();" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if(parent.frames['mainFrame'].document.getElementsByName('login:command/password')[0] != null){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                   initAmbeent() +
                    "setInterval(() => {" +
                    "parent.frames['mainFrame'].document.getElementById('uiViewChannelBG').value = " + getOptimalChannel() + ";" +
                    "parent.frames['mainFrame'].document.getElementById('uiViewApplyButton').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1000);" +
                    "};";
        return null;
    }
}
