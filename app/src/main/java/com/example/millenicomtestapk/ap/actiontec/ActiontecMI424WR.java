package com.example.millenicomtestapk.ap.actiontec;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class ActiontecMI424WR extends AccessPoint {

    public static final String TAG = ActiontecMI424WR.class.getSimpleName();
    public static final String WRONG_PASSWORD = "active_page=9074";
    public static final String LOGGED_IN = "active_page=9131";
    public static final String SETTINGS_SUFFIX = "active_page=9120";
    public static final String MID_URL = "/index.cgi?";
    private State state = State.CHANNEL_CHANGE_STATE;
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "FLMFb2BT";

    public ActiontecMI424WR(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
            Log.d(TAG, url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.equals(getFormattedGateway() + "/"))
            return "javascript:{" +
                    "document.getElementsByName('user_name')[0].value = '" + getUsername() + "';" +
                    "document.getElementById('password_div').children[0].value = '" + getPassword() + "';" +
                    "document.getElementsByClassName('actiontec_button')[0].click();" +
                    "};";
        else if (url.contains(WRONG_PASSWORD))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.contains(LOGGED_IN))
            return "javascript:{" +
                    getLocalHostLoginScript() +
                    "window.location.href = '" + getFormattedGateway() + MID_URL + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.contains(SETTINGS_SUFFIX))
            if (state == State.CHANNEL_CHANGE_STATE && (getOptimalChannel() <= 11)) {
                state = State.APPLY_STATE;
                return "javascript:{" +
                        initAmbeent()+
                        "if (document.getElementById('ws_channel') != null){" +
                        "document.getElementById('ws_channel')[" + getOptimalChannel() + "].selected = true;" +
                        "changeDisp_channel();" +
                        "document.getElementsByClassName('actiontec_button')[0].click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent()+
                        "document.getElementsByClassName('actiontec_button')[0].click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "};";
            }

        return null;
    }

    enum State {
        CHANNEL_CHANGE_STATE,
        APPLY_STATE,

    }
}