package com.example.millenicomtestapk.ap.zyxel;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

public class ZyxelVMG1312_T20B extends AccessPoint {

    private static final String TAG = ZyxelVMG1312_T20B.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "1234";
    private State state = State.LOGIN_STATE;

    public ZyxelVMG1312_T20B(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
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
                    "if(document.getElementById('username') != null) {" +
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
                    // Initializing function to check whether target frame exists and do work.
                    "var repeatInterval = setInterval(function(){" +
                    // Getting the current document content in iFrame.
                    "var iframeDocument = document.getElementById('mainFrame').contentDocument;" +
                    //Trying to get target checkbox.
                    "var channelMenu = iframeDocument.getElementsByName('wifi_channel')[0];" +
                    // Checking whether the checkbox exists.
                    "if (channelMenu != null){" +
                    "iframeDocument.getElementsByName('wifi_channel')[0][" + getOptimalChannel() + "].selected = true;" +
                    "iframeDocument.getElementsByName('sysWifiSubmit')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    // End of if statement.
                    "} else { document.getElementById('Wireless').click(); }" +
                    // End of setInterval function.
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
