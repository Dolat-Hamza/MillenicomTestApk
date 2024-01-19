package com.example.millenicomtestapk.ap.zyxel;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ZyxelVMG1312_B10D extends AccessPoint {

    private static final String TAG = ZyxelVMG1312_B10D.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "1234";
    private State state = State.LOGIN_STATE;

    public ZyxelVMG1312_B10D(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, "State : " + state);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "document.getElementById('username').value = '" + getUsername() + "';" +
                    "document.getElementById('userpassword').value = '" + getPassword() + "';" +
                    "document.getElementById('loginBtn').click();" +
                    "window.location.href = '" + getFormattedGateway() + "';" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANNEL_CHANGE_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function(){" +
                    "if(document.getElementById('zypswd') != null) {" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else if(document.getElementById('Wireless') != null) {" +
                    "clearInterval(repeatInterval);" +
                    "window.location.href = '" + getFormattedGateway() + "';" +
                    "}" +
                    "}, 1000);" +
                    "};";
        } else if (state == State.CHANNEL_CHANGE_STATE) {
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "var iframeDocument = document.getElementById('mainFrame').contentDocument;" +
                    "var channelMenu = iframeDocument.getElementsByName('wifi_channel')[0];" +
                    "if (channelMenu != null){" +
                    "iframeDocument.getElementsByName('wifi_channel')[0][" + getOptimalChannel() + "].selected = true;" +
                    "iframeDocument.getElementsByName('sysWifiSubmit')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "} else { document.getElementById('Wireless').click(); }" +
                    "}, 1000);" +
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
