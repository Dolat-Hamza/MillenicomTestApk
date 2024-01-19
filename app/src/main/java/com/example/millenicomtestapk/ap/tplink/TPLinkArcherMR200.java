package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkArcherMR200 extends AccessPoint {

    private State state = State.LOGIN_STATE;

    public TPLinkArcherMR200(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword("mergen.16");
    }

    @Override
    public String getJSCodeForUrl(String url) {
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "if (document.getElementById('pcPassword') != null){" +   // Checking whether the checkbox exists.
                    "document.getElementById('pcPassword').value = '" + getPassword() + "';" + // Writing password.
                    "document.getElementById('login-btn').click();" + // Login click. This does not reload the page if the password is true.
                    "}" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CHANNEL_CHANGE_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementById('advanced') == null){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else {" +
                    "window.location.href = '" + getFormattedGateway() + "';" +
                    "}" +
                    "};";
        } else if (state == State.CHANNEL_CHANGE_STATE) {
            return "javascript:{" +
                    initAmbeent()+
                    "var isAdvancedClicked = false;" +
                    "var isAtWireless = false;" +
                    "var repeatInterval = setInterval(function() {" +
                    "if (!isAdvancedClicked) {" +
                    "document.getElementById('advanced').click();" +
                    "isAdvancedClicked = true;" +
                    "}" +
                    "else if (!isAtWireless) {" +
                    "document.getElementById('menuTree').children[4].children[1].children[0].children[0].click();" +
                    "isAtWireless = true;" +
                    "}" +
                    "else if(document.getElementsByClassName('tp-select')[4].children[2].children[2] != null && isAtWireless){" +
                    "clearInterval(repeatInterval);" +
                    "document.getElementsByClassName('tp-select')[4].children[2].children[2].click();" + //Selecting 20Mhz channel width
                    // Select optimized channel. Because of list structure, we add one to channel number for correct channel.
                    "document.getElementsByClassName('tp-select')[3].children[2].children[" + (getOptimalChannel() + 1) + "].click();" +
                    "document.getElementById('save').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 2000);" + // End of setInterval function.
                    "}";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        CHANNEL_CHANGE_STATE
    }
}
