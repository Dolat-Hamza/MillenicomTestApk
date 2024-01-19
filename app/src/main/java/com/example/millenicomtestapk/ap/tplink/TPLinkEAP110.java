package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkEAP110 extends AccessPoint {

    private static final String TAG = TPLinkEAP110.class.getSimpleName();
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";
    private State state = State.LOGIN_STATE;

    public TPLinkEAP110(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, "State : " + state);
        Log.d(TAG, url);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "document.getElementById('login-username').value = '" + getUsername() + "';" +
                    "document.getElementById('login-password').value = '" + getPassword() + "';" +
                    "document.getElementById('login-btn').click();" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.SETTINGS_STATE;
            return "javascript:{" +
                    initAmbeent() +
                    "if (document.getElementById('login-password') != null){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "document.getElementsByName('wireless')[0].click();" +
                    "}" +
                    "};";
        } else if (state == State.SETTINGS_STATE) {
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "document.getElementsByClassName('combobox-list')[8].click();" +
                    "document.getElementsByClassName('combobox-list')[8].children[" + getOptimalChannel() + "].children[0].click();" +
                    "document.getElementsByClassName('button-button')[2].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "clearInterval(repeatInterval);" +
                    "}, 1200);" +
                    "};";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        SETTINGS_STATE;
    }
}
