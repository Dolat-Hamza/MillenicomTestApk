package com.example.millenicomtestapk.ap.everest;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class EverestSG_1600 extends AccessPoint {
    private static final String TAG = EverestSG_1600.class.getSimpleName();
    private static final String WSETTINGS_SUFFIX = "/wlswitchinterface0.wl";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private State state = State.LOGIN_STATE;

    public EverestSG_1600(String gateway, String password, String username, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        Log.d(TAG, "State : " + state);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "document.getElementById('username').value= '" + getUsername() + "';" +
                    "document.getElementById('password').value= '" + getPassword() + "';" +
                    "setTimeout(() => {" +
                    "document.getElementById('loginBtn').click();" +
                    "}, 300);" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.SETTINGS_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementById('username') != null) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "window.location.href = '" + getFormattedGateway() + WSETTINGS_SUFFIX + "';" +
                    "}" +
                    "};";
        } else if (state == State.SETTINGS_STATE) {
            state = State.SUCCES_STATE;
            return "javascript:{" +
                    "document.getElementsByName('wlchannel')[0].value = " + getOptimalChannel() + ";" +
                    "btnApply();" +
                    "}";
        } else if (state == State.SUCCES_STATE) {
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
        SETTINGS_STATE,
        SUCCES_STATE,

    }

}

