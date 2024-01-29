package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkTD_ArcherC6v2 extends AccessPoint {
    private static final String TAG = TPLinkTD_VN020G2u.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private State state = State.LOGIN_STATE;
    public TPLinkTD_ArcherC6v2(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Nullable
    @Override
    public String getJSCodeForUrl(@Nullable String url) {


        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    getSendCode() +
                    "amb_sendcode(document.getElementById('userName'), '" + getUsername() + "').then(function(){" +
                    "amb_sendcode(document.getElementById('pcPassword'), '" + getPassword() + "');}).then(function(){" +
                    "setTimeout(function(){" +
                    "document.getElementById('loginBtn').click();" +
                    "},2000);" +
                    "})" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANNEL_CHANGE_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function(){" +
                    "if(document.getElementById('skipBtn') != null){" +
                    "document.getElementById('skipBtn').click();" +
                    "clearInterval(repeatInterval);" +
                    "}" +
                    "else if(document.getElementsByClassName('noteDiv')[0] != null) {" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else {" +
                    "window.location.href = '" + getFormattedGateway() + "';" +
                    "}" +
                    "}, 1200);" +
                    "};";
        } else if (state == State.CHANNEL_CHANGE_STATE)
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval2 = setInterval(function(){" +
                    "if(document.getElementById('menu_wl') != null){" +
                    "document.getElementById('menu_wl').click();" +
                    "clearInterval(repeatInterval2);" +
                    "}" +
                    "}, 1200);" +
                    "var repeatInterval3 = setInterval(function(){" +
                    "if(document.getElementById('channel') != null){" +
                    "clearInterval(repeatInterval3);" +
                    "document.getElementById('channel')[" + getOptimalChannel() + "].selected = true;" +
                    "changeChannel();" +
                    "document.querySelector('input.button.L.T.T_save').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 1200);" +
                    "};";

        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        CHANNEL_CHANGE_STATE
    }
}