package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkTD_W8951ND extends AccessPoint {

    public static final String TAG = TPLinkTD_W8951ND.class.getSimpleName();
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private State state = State.LOGIN_STATE;

    public TPLinkTD_W8951ND(String gateway, String username, String password, int callbackPort) {
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
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getPassword() + "@' + pure.slice(7) + '/basic/home_wlan.htm';" +
                    "window.location.href = link;" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANGE_CHANNEL_STATE;
            return "javascript:{" +
                    initAmbeent()+
//                    "setTimeout(() => {" +
                    "if (document.getElementsByName('Channel_ID').length === 0) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else {" +
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getPassword() + "@' + pure.slice(7) + '/basic/home_wlan.htm';" +
                    "window.location.href = link;" +
                    "}" +
//                    "}, 1000);" +
                    "};";
        } else if (state == State.CHANGE_CHANNEL_STATE) {
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(() => {" +
                    "document.getElementsByName('Channel_ID')[0].selectedIndex = " + getOptimalChannel() + ";" +
                    "}, 500);" +
                    "setTimeout(() => {" +
                    "doSave();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1000);" +
                    "};";
        }

        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        CHANGE_CHANNEL_STATE
    }
}
