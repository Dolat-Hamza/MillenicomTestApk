package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkTD_W854 extends AccessPoint {

    private static final String TAG = TPLinkTD_W854.class.getSimpleName();
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "ttgalaksi";
    private State state = State.LOGIN_STATE;

    public TPLinkTD_W854(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        Log.d(TAG, "State : " + state);
        if (state == State.LOGIN_STATE) {
            state = State.SETTINGS_STATE;
            return "javascript:{" +
                    "if (document.getElementById('formframe') == null){" +
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getPassword() + "@' + pure.slice(7) + '/wlbasic.htm';" +
                    "window.location.href = link;" +
                    "}" +
                    "};";
        } else if (state == State.SETTINGS_STATE) {
            state = State.APPLY_STATE;
            if (getOptimalChannel() <= 11) {
                return "javascript:{" +
                        initAmbeent()+
                        "if (document.getElementsByName('chan')[0] == null){" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "else {" +
                      initAmbeent()+
                        "var repeatInterval = setInterval(function(){" +
                        "if (document.getElementsByName('chanwid')[0] != null){" +
                        "document.getElementsByName('chanwid')[0].value = 0;" +
                        "}" +
                        "document.getElementsByName('chan')[0].value = " + getOptimalChannel() + ";" +
                        "document.getElementsByName('save')[0].click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "clearInterval(repeatInterval);" +
                        "}, 1300);" +
                        "}" +
                        "};";
            }
        } else if (state == State.APPLY_STATE) {
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
        SETTINGS_STATE,
        APPLY_STATE;
    }
}
