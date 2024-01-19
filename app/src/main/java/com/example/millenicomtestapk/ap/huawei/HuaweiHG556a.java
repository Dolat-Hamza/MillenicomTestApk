package com.example.millenicomtestapk.ap.huawei;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class HuaweiHG556a extends AccessPoint {

    public static final String TAG = HuaweiHG556a.class.getSimpleName();
    public static final String LOGIN_SUFFIX = "/";
    public static final String INDEX_SUFFIX = "/tr_TR/basic/index.htm";
    public static final String SETTINGS_SUFFIX = "/tr_TR/expert/config_wifi.htm";
    public static final String APPLY_SUFFIX = "/tr_TR/expert/valid_wifi.htm";
    public static final String DEFAULT_USERNAME = "vodafone";
    public static final String DEFAULT_PASSWORD = "vodafone";
    private State state = State.LOGIN_STATE;

    public HuaweiHG556a(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "if (document.getElementById('formframe') == null){" +
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getPassword() + "@' + pure.slice(7) + '/tr_TR/expert/config_wifi.htm';" +
                    "window.location.href = link;" +
                    "}" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHECK_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementById('menu_wifi') == null){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "location.replace('" + getFormattedGateway() + SETTINGS_SUFFIX + "');" +
                    "}" +
                    "};";
        } else if (url.equals(getFormattedGateway() + INDEX_SUFFIX)) {
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX)) {
            return "javascript:{" +
                    "document.getElementsByName('iWifiChannel')[0].value = " + getOptimalChannel() + ";" +
                    "window.PrepareSubmit();" +
                    "};";
        } else if (url.equals(getFormattedGateway() + APPLY_SUFFIX)) {
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "};";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        CHECK_STATE
    }
}