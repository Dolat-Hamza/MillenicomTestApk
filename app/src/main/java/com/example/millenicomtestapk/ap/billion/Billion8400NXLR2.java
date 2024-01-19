package com.example.millenicomtestapk.ap.billion;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

public class Billion8400NXLR2 extends AccessPoint {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private State state = State.LOGIN_STATE;

    public Billion8400NXLR2(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getUsername() + "@' + pure.slice(7) + '/cgi-bin/home_wireless.asp';" +
                    "window.location.href = link;" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANNEL_CHANGE_STATE;
            return "javascript:{" +
                    initAmbeent() +
                    "if (document.title.includes('401')) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else {" +
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getUsername() + "@' + pure.slice(7) + '/cgi-bin/home_wireless.asp';" +
                    "window.location.href = link;" +
                    "}" +
                    "};";
        } else if (state == State.CHANNEL_CHANGE_STATE) {
            return "javascript:{" +
                    initAmbeent() +
                    "document.getElementsByName('Countries_Channels')[0].selectedIndex = 86;" +
                    "document.getElementsByName('Channel_ID')[0].selectedIndex = " + getOptimalChannel() + ";" +
                    "setTimeout(() => {" +
                    "document.getElementsByName('BUTTON')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 500);" +
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
