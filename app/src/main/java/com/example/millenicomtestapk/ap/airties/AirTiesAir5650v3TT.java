package com.example.millenicomtestapk.ap.airties;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class AirTiesAir5650v3TT extends AccessPoint {

    private static final String TAG = AirTiesAir5650v3TT.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String ERROR_SUFFIX = "/login.html?ErrorCode=6&redirect=&self=1";
    private static final String MAIN_SUFFIX = "/main.html";
    private static final String SETTINGS_SUFFIX = "/wireless/settings/settings_new.html";

    private static final String DEFAULT_PASSWORD = "ttnet";
    private static final String DEFAULT_USERNAME = "admin";


    public AirTiesAir5650v3TT(String gateway, String username, String password, int callbackPort) {
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
                    initAmbeent()+
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
                    "var repeatInterval = setInterval(function(){" +
                    "document.getElementById('cmb_channel')[" + getOptimalChannel() + "].selected = true;" +
                    "document.getElementById(`cmb_chanbw`).value=20;" +
                    "document.getElementById('__ML_save').click();" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1300);" +
                    "};";

        return null;
    }
}
