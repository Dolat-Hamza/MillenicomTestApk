package com.example.millenicomtestapk.ap.zte;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ZteZXHN_H267N extends AccessPoint {

    private static final String TAG = ZteZXHN_H267N.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private State state = State.LOGIN_STATE;

    public ZteZXHN_H267N(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "if(document.getElementById('mmLocalnet') == null && document.getElementById('Username') != null) {" +
                    "document.getElementById('Username').value = '" + getUsername() + "';" +
                    "document.getElementById('Password').value = '" + getPassword() + "';" +
                    "document.getElementById('LoginId').click();" +
                    "} else {" +
                    "window.location.href = '" + getFormattedGateway() + "';" +
                    "}" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANNEL_CHANGE_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function(){" +
                    "if(document.getElementById('Username') != null) {" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else if(document.getElementById('mmLocalnet') != null) {" +
                    initAmbeent() +
                    "clearInterval(repeatInterval);" +
                    "document.getElementById('mmLocalnet').click();" +

                    "var repeatInterval2 = setInterval(function(){" +
                    "if(document.getElementById('smLocalWLAN') != null) {" +
                    "clearInterval(repeatInterval2);" +
                    "document.getElementById('smLocalWLAN').click();" +
                    "}" +
                    "}, 1000);" +

                    "var repeatInterval3 = setInterval(function(){" +
                    "if(document.getElementById('Channel') != null) {" +
                    "clearInterval(repeatInterval3);" +
                    "document.getElementById('Channel')[" + getOptimalChannel() + "].selected = true;" +
                    "document.getElementById('Btn_apply_WlanBasicAdConf').click();" +
                    "}" +
                    "}, 1000);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 1000);" +
                    "};";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        CHANNEL_CHANGE_STATE
    }
}
