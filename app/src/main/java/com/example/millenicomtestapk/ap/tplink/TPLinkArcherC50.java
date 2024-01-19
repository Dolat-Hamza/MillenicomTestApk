package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkArcherC50 extends AccessPoint {
    public static final String TAG = TPLinkArcherC50.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private State state = State.LOGIN_STATE;

    public TPLinkArcherC50(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        Log.d(TAG, state.toString());
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "document.getElementById('userName').value= '" + getUsername() + "';" +
                    "document.getElementById('pcPassword').value= '" + getPassword() + "';" +
                    "document.getElementById('loginBtn').click();" +
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
            if (getOptimalChannel() > 13) {
                return "javascript:{" +
                        initAmbeent()+
                        "var repeatInterval2 = setInterval(function(){" +
                        "var frame1 = document.getElementById('frame1').contentDocument;" +
                        "if(frame1.getElementById('menu_wl5g') != null){" +
                        "frame1.getElementById('menu_wl5g').click();" +
                        "clearInterval(repeatInterval2);" +
                        "}" +
                        "}, 1200);" +
                        "var repeatInterval3 = setInterval(function(){" +
                        "var frame2 = document.getElementById('frame2').contentDocument;" +
                        "if(frame2.getElementById('channel') != null){" +
                        "clearInterval(repeatInterval3);" +
                        "frame2.getElementById('bandWidth')[1].selected = true;" +
                        "frame2.getElementById('bandWidth').onchange();" +
                        "frame2.getElementById('channel').value = " + getOptimalChannel() + ";" +
                        "frame2.getElementById('channel').onchange();" +
                        "frame2.querySelector('input.button.L.T.T_save').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "}, 1200);" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent()+
                        "var repeatInterval2 = setInterval(function(){" +
                        "var frame1 = document.getElementById('frame1').contentDocument;" +
                        "if(frame1.getElementById('menu_wl2g') != null){" +
                        "frame1.getElementById('menu_wl2g').click();" +
                        "clearInterval(repeatInterval2);" +
                        "}" +
                        "}, 1200);" +
                        "var repeatInterval3 = setInterval(function(){" +
                        "var frame2 = document.getElementById('frame2').contentDocument;" +
                        "if(frame2.getElementById('channel') != null){" +
                        "clearInterval(repeatInterval3);" +
                        "frame2.getElementById('bandWidth')[1].selected = true;" +
                        "frame2.getElementById('bandWidth').onchange();" +
                        "frame2.getElementById('channel')[" + getOptimalChannel() + "].selected = true;" +
                        "frame2.getElementById('channel').onchange();" +
                        "frame2.querySelector('input.button.L.T.T_save').click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "}, 1200);" +
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
