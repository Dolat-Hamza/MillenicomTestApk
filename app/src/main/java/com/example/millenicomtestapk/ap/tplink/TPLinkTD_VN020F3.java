package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkTD_VN020F3 extends AccessPoint {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "dsmart";

    private static final String LOGIN_SUFFIX = "/";
    private static final String MAIN_SUFFIX = "/index.mobile.htm";
    private State state = State.LOGIN_STATE;

    public TPLinkTD_VN020F3(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {

        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE;
                return "javascript:{" +
                        getSendCode() +
                        "amb_sendcode(document.getElementById('ph-login-user'), '" + getUsername() + "').then(function(){" +
                        "amb_sendcode(document.getElementById('ph-login-password'), '" + getPassword() + "');}).then(function(){" +
                        "setTimeout(function(){" +
                        "document.getElementById('ph-login-btn').click();" +
                        "setTimeout(function(){" +
                        "document.getElementById('pc-skip-btn').click();" +
                        "},2000);" +
                        "},2000);" +
                        "})" +
                        "};";
            } else if(state == State.CREDENTIAL_CHECK_STATE) {
                state = State.SETTINGS_STATE;
                String a = "javascript:{" +
                        initAmbeent()+
                        "setTimeout(function(){" +
                        "if(document.getElementById('errorContent').innerHTML.length > 25)" +
                        "{" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "else{" +
                        "document.getElementById('pc-skip-btn').click();" +
                        "}" +
                        "},3000);" +
                        "};";
                return a;
            }
        }
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))

        {
            return "javascript:{" +
                     initAmbeent() +
                    "setTimeout(function () {" +
                    "document.getElementById('top-menuIcon-container').click();" +
                    "setTimeout(function () {" +
                    "document.getElementById('wireless').click();" +
                    "setTimeout(function () {" +
                    "document.getElementsByClassName('turn-page grow-part')[0].click();" +
                    "setTimeout(function () {" +
                    "var channelValue = document.getElementById('channel');" +
                    "channelValue.value = '" + getOptimalChannel() + "';" +
                    "var evnt = new Event('change');" +
                    "channelValue.dispatchEvent(evnt);" +
                    "document.getElementById('save').click();" +
                    "var i = 0;" +
                    "var repeatInterval = setInterval(function(){" +
                    "console.log(document.getElementsByClassName('msg-content')[0]);" +
                    "if(document.getElementsByClassName('msg-content')[0] == undefined){" +
                    "i++;" +
                    "console.log(i);" +
                    "if(i == 2){" +
                    "document.getElementsByClassName('section-box')[4].click();" +

                    "document.getElementById('confirm-ok').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "clearInterval(repeatInterval);" +
                    "}" +
                    "document.getElementById('top-menuIcon-container').click();" +
                    "document.getElementsByClassName('top-menuList-item')[7].click();" +
                    "}"+
                    "}, 5000);" +
                    "}, 3000);" +
                    "}, 3000);" +
                    "}, 3000);" +
                    "}, 3000);" +
                    "};";
        }
        return null;
    }
    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        SETTINGS_STATE
    }

}
