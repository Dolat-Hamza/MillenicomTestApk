package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkTD_W9970 extends AccessPoint {

    private static final String TAG = TPLinkTD_W9970.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "turktelekom";
    private State state = State.LOGIN_STATE;

    public TPLinkTD_W9970(String gateway, String username, String password, String setupUsername, String setupPassword, int callbackPort) {
        super(gateway, username, password, setupUsername, setupPassword, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "document.getElementById('userName').value= '" + getUsername() + "';" +
                    "document.getElementById('pcPassword').value= '" + getPassword() + "';" +
                    "document.getElementById('loginBtn').click();" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.CREDENTIAL_CHECK_STATE2;
            return "javascript:{" +
                    "console.log('test CREDENTIAL_CHECK_STATE');" +
                    "setTimeout(function() {" +
                    "try {" +
                    "skip();" +
                    "} catch (error) {" +
                    "if (document.getElementById('userName') !== 'null') {" +
                    "console.log('AmbeentFailure');" +
                    "}" +
                    "else {" +
                    "window.location.href = '" + getFormattedGateway() + "';" +
                    "}" +
                    "}" +
                    "},1500);" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE2) {
            state = State.SETUP_STATE;
            return "javascript:{" +
                    "console.log('test CREDENTIAL_CHECK_STATE2');" +
                    "setTimeout(function() {" +
                    "try {" +
                    "skip();" +
                    "} catch (error) {" +
                    "if (document.getElementById('userName') !== 'null') {" +
                    "console.log('AmbeentFailure');" +
                    "}" +
                    "else {" +
                    "window.location.href = '" + getFormattedGateway() + "';" +
                    "}" +
                    "}" +
                    "},1500);" +
                    "};";
        } else if (state == State.SETUP_STATE) {
            state = State.SETUP_STATE2;
            return "javascript:{" +
                    "console.log('test SETUP_STATE');" +

                    "var repeatInterval = setInterval(function(){" +
                    "console.log('test repeatInterval');" +
                    "if(document.getElementById('menu_qs') !== null && document !== null){" +
                    "clearInterval(repeatInterval);" +
                    "document.getElementById('menu_qs').click();" +
                    "}" +
                    "}, 1200);" +

                    "var repeatInterval2 = setInterval(function(){" +
                    "console.log('test repeatInterval2');" +
                    "if(document.getElementsByClassName('button L T T_next')[0] !== undefined){" +
                    "clearInterval(repeatInterval2);" +
                    "document.getElementsByClassName('button L T T_next')[0].click();" +
                    "}" +

                    "}, 1200);" +
                    "setTimeout(function() {" +
                    "doChgDomain();" +
                    "document.getElementById('usr').value = '" + getSetupUsername() + "';" +
                    "document.getElementById('pwd').value= '" + getSetupPassword() + "';" +
                    "document.getElementById('cfm').value= '" + getSetupPassword() + "';" +
                    "document.getElementsByClassName('button L T T_next')[0].click();" +
                    "setTimeout(function() {" +
                    "document.getElementsByClassName('button L T T_next')[0].click();" +
                    "},2000);" +

                    "var repeatInterval3 = setInterval(function(){" +
                    "if(document.getElementsByClassName('button L T T_save')[0] !== undefined){" +
                    "clearInterval(repeatInterval3);" +
                    "document.getElementsByClassName('button L T T_save')[0].click()" +
                    "}" +
                    "}, 1200);" +

                    "var repeatInterval4 = setInterval(function(){" +
                    "if(document.getElementsByClassName('button L T T_finish')[0] !== undefined){" +
                    "clearInterval(repeatInterval4);" +
                    "document.getElementsByClassName('button L T T_finish')[0].click()" +
                    "}" +
                    "}, 2000);" +
                    "},7000);" +
                    "};";
        }

        else if (state == State.SETUP_STATE2) {
            return "javascript:{" +
                    "console.log('test SETUP_STATE2');" +
                    "$.act(ACT_CGI, '/cgi/logout');" +
                    "$.exe();" +
                    "$.refresh();" +
                    "console.log('AmbeentSuccess');" +
                    "};";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        CREDENTIAL_CHECK_STATE2,
        SETUP_STATE,
        SETUP_STATE2
    }
}
