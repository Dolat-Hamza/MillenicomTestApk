package com.example.millenicomtestapk.ap.linksys;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class LinksysWAG310g extends AccessPoint {

    public static final String TAG = LinksysWAG310g.class.getSimpleName();

    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "admin";

    public static final String MAIN_SUFFIX = "/Wireless.bsp";
    private State state = State.LOGIN_STATE;

    public LinksysWAG310g(String gateway, String username, String password, int callbackPort) {
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
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getPassword() + "@' + pure.slice(7) + '/';" +
                    "window.location.href = link;" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANNEL_CHANGE_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementsByClassName('mainmenu')[0] == null){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "location.replace('" + getFormattedGateway() + MAIN_SUFFIX + "');" +
                    "}" +
                    "};";
        } else if (state == State.CHANNEL_CHANGE_STATE) {
            state = State.CHANGE_STATE;
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "document.getElementsByName('wl_channel')[0].value = " + getOptimalChannel() + ";" +
                    "document.getElementsByName('save')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "clearInterval(repeatInterval)" +
                    "}, 1000);" +
                    "};";
        }

        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        CHANNEL_CHANGE_STATE,
        CHANGE_STATE,
    }
}

