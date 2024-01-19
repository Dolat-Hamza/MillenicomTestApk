package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkArcherVR300 extends AccessPoint {

    private static final String TAG = TPLinkArcherVR300.class.getSimpleName();
    private static final String DEFAULT_PASSWORD = "ttnet";
    private static final String SETTINGS_SUFFIX = "/index.mobile.htm";
    private State state = State.LOGIN_STATE;

    public TPLinkArcherVR300(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        Log.d(TAG, "State : " + state);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "document.getElementById('ph-login-password').value = '" + getPassword() + "';" +
                    "document.getElementById('ph-login-btn').click();" +
                    "setInterval(() => {" +
                    "document.getElementById('confirm-yes').click();" +
                    "}, 1000);" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.PC_FORMAT_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementById('ph-login-password') == null){" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "}" +
                    "else {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "};";
        } else if (state == State.PC_FORMAT_STATE) {
            state = State.CHANNEL_CHANGE_STATE;
            return "javascript:{" +
                    "var repeatInterval = setInterval(()=>{" +
                    "if (document.getElementById('changeToPC') != null) {" +
                    "clearInterval(repeatInterval);" +
                    "document.getElementById('changeToPC').click();" +
                    "}" +
                    "}, 4000);" +
                    "};";
        } else if (state == State.CHANNEL_CHANGE_STATE) {
            return "javascript:{" +
                    initAmbeent()+
                    "var isAdvancedClicked = false;" +
                    "var isAtWireless = false;" +
                    "var repeatInterval = setInterval(()=>{" +
                    "if (!isAdvancedClicked) {" +
                    "document.getElementById('advanced').click();" +
                    "isAdvancedClicked = true;" +
                    "}" +
                    "else if (!isAtWireless) {" +
                    "document.getElementById('menuTree').children[4].children[1].children[0].children[0].click();" +
                    "isAtWireless = true;" +
                    "}" +
                    "else if(document.getElementsByClassName('tp-select')[4].children[2].children[2] != null && isAtWireless){" +
                    "clearInterval(repeatInterval);" +
                    "document.getElementsByClassName('tp-select')[5].children[2].children[2].click();" +
                    "document.getElementsByClassName('tp-select')[4].children[2].children[" + (getOptimalChannel() + 1) + "].click();" +
                    "document.getElementById('save').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 1300);" +
                    "}";
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
