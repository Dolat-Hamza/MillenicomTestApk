package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;


public class TPLinkTD_W9960 extends AccessPoint {

    private static final String TAG = TPLinkTD_W9960.class.getSimpleName();
    private static final String DEFAULT_PASSWORD = "ttnet";
    private static final String SETTINGS_SUFFIX = "/index.mobile.htm";
    private State state = State.LOGIN_STATE;
    ///PAROLASI sadece bizim modem de admin

    public TPLinkTD_W9960(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        Log.d(TAG, "State : " + state);
        Log.d(TAG, "State : " + DEFAULT_PASSWORD);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "document.getElementById('ph-login-password').value = '" + getPassword() + "';" +
                    "document.getElementById('ph-login-btn').click();" +
                    "setTimeout(function(){" +
                    "document.getElementById('confirm-yes').click();" +
                    "},3000);" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANNEL_CHANGE_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function(){" +
                    "if (document.getElementsByClassName('errTextP')[0].innerHTML.length == 7){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "},3000);" +
                    "setTimeout(function(){" +
                    "if (document.getElementById('changeToPC') != null) {" +
                    "document.getElementById('changeToPC').click();" +
                    "}" +
                    "},3000);" +
                    "};";
        } else if (state == State.CHANNEL_CHANGE_STATE) {
            String a = "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function(){" +
                    "document.getElementById('advanced').click();" +
                    "setTimeout(function(){" +
                    "document.getElementsByClassName('text T')[10].click();" +
                    "setTimeout(function(){" +
                    "document.getElementsByClassName('text T')[11].click();" +
                    "setTimeout(function(){" +
                    "document.getElementsByClassName('tp-select')[4].children[2].children[" + (getOptimalChannel() + 1) + "].click();" +
                    "setTimeout(function(){" +
                    "document.getElementById('save').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "setTimeout(function(){" +
                    "document.getElementById('topLogout').click();" +
                    "setTimeout(function(){" +
                    "document.getElementsByClassName('button-button green pure-button btn-msg btn-msg-ok btn-confirm')[0].click();" +
                    "},1500);" +
                    "},1500);" +
                    "},3000);" +
                    "},3000);" +
                    "},2500);" +
                    "},2000);" +
                    "},2000);" +
                    "}";
            return a;
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        CHANNEL_CHANGE_STATE,
        PC_FORMAT_STATE
    }
}
