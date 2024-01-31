package com.example.millenicomtestapk.ap.zyxel;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;
import com.example.millenicomtestapk.ap.tplink.TPLinkTD_W9970;

public class ZyxelVMG3312_B10B extends AccessPoint {

    private static final String TAG = ZyxelVMG3312_B10B.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login/login.html";
    private static final String ERROR_SUFFIX = "/login/login-page.cgi";
    private static final String MAIN_SUFFIX = "/index.html";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "turktelekom";

    private State state = State.LOGIN_STATE;

    public ZyxelVMG3312_B10B(String gateway, String username, String password, String setupUsername, String setupPassword, int callbackPort) {
        super(gateway, username, password, setupUsername, setupPassword, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        Log.d(TAG, getFormattedGateway());
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX) && state == State.LOGIN_STATE)
            return "javascript:{" +
                    "try{" +
                    "document.getElementById('AuthName').value = '" + getUsername() + "';" +
                    "document.getElementById('AuthPassword').value = '" + getPassword() + "';" +
                    "document.getElementById('login').submit();" +
                    "} catch(err){" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    "setTimeout(function(){" +
                    "if(!document.getElementById('Message')){" +
                    "}" +
                    // Check if there is an error message. If there is, username or pass is wrong
                    "else if(document.getElementById('Message').innerHTML.length > 0){" +
                    // Ask for password and channel
                    "setTimeout(function() {" +
                    "console.log('password wrong!!!!');" +
                    "console.log('AmbeentFailure');" +
                    "},2000);" +
                    "} else if (document.querySelectorAll('input[type=button]')[1] != null){" +
                    "document.querySelectorAll('input[type=button]')[1].click();" +
                    "}" +
                    "}, 1000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX)) {
            state = State.CREDENTIAL_CHECKED_STATE;
            return "javascript:{" +
                    "document.getElementById('network-broadband').click();" +
                    "setTimeout(function(){" +
                    "var iframeDocument = document.getElementById('mainFrame').contentDocument;" +
                    "iframeDocument.getElementById('editBtn').click();" +
                    "setTimeout(function(){" +
                    "PPPotherISP_click();" +
                    "document.getElementById('sysPPPUsernameFull').value = '" + getSetupUsername() + "';" +
                    "document.getElementById('sysPPPPwd').value = '" + getSetupPassword() + "';" +
                    "document.getElementsByClassName('ui-state-default ui-corner-all')[1].click();" +
                    "setTimeout(function(){" +
                    "document.getElementById('logoutName').click();" +
                    "document.getElementsByClassName('ui-state-default ui-corner-all')[1].click();" +
                    "console.log('AmbeentSuccess');" +
                    "}, 5000);" +
                    "}, 3000);" +
                    "}, 5000);" +
                    "};";

        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECKED_STATE
    }

}
