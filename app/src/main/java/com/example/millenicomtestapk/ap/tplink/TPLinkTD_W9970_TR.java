package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;

public class TPLinkTD_W9970_TR extends TPLinkTD_W9970 {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private State state = State.LOGIN_STATE;

    public TPLinkTD_W9970_TR(String gateway, String username, String password, String setupUsername, String setupPassword, int callbackPort) {
        super(gateway, username, password, setupUsername, setupPassword, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }


    @Override
    public String getJSCodeForUrl(String url) {
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "setTimeout(function(){" +
                    "document.getElementById('userName').value= '" + getUsername() + "';" +
                    "document.getElementById('pcPassword').value= '" + getPassword() + "';" +
                    "setTimeout(function(){" +
                    "document.getElementById('loginBtn').click();" +
                    "},2000);" +
                    "},2000);" +
                    "};";
        } else if (state == state.CREDENTIAL_CHECK_STATE) {
            state = state.CHANNEL_CHANGE_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function(){" +
                    "if(document.getElementById('skipBtn') != null){" +
                    "document.getElementById('skipBtn').click();" +
                    "clearInterval(repeatInterval);" +
                    "}" +
                    "else if(document.getElementsByClassName('noteDiv')[0] != null) {" +
                    "clearInterval(repeatInterval);" +
                    ambeent("failure") +
                    "} else {" +
                    "window.location.href = '" + getFormattedGateway() + "';" +
                    "}" +
                    "}, 1200);" +
                    "};";
        } else if (state == state.CHANNEL_CHANGE_STATE)
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
                    ambeent(String.valueOf(getOptimalChannel())) +
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