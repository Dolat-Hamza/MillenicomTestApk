package com.example.millenicomtestapk.ap.huawei;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;


public class HuaweiHG531s extends AccessPoint {

    public static final String TAG = HuaweiHG531s.class.getSimpleName();
    public static final String LOGIN_SUFFIX = "/";
    public static final String MAIN_SUFFIX = "/html/content1.asp";
    public static final String DEFAULT_USERNAME = "vodafone";
    public static final String DEFAULT_PASSWORD = "vodafone";
    private State state = State.LOGIN_STATE;

    public HuaweiHG531s(String gateway, String username, String password, String setupUsername, String setupPassword, int callbackPort) {
        super(gateway, username, password, setupUsername, setupPassword, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE;
                return "javascript:{" +
                        getSendCode() +
                        "amb_sendcode(document.getElementById('txt_Username'), '" + getUsername() + "').then(function(){" +
                        "amb_sendcode(document.getElementById('txt_Password'), '" + getPassword() + "');}).then(function(){" +
                        "setTimeout(function(){" +
                        "document.getElementById('btnLogin').click();},2000);" +
                        "})" +
                        "};";
            } else {
                return "javascript:{" +
                        "if(document.getElementById('erroinfoId').innerHTML.length > 75){" +
                        "setTimeout(function() {" +
                        "console.log('AmbeentFailure');" +
                        "},2000);" +
                        "}" +
                        "};";
            }
        } else if (url.equals(getFormattedGateway() + MAIN_SUFFIX) && state == State.CREDENTIAL_CHECK_STATE) {
            state = State.SETUP_STATE;
            return "javascript:{" +
                    "var mainFrame = document.getElementById('contentfrm').contentDocument || document.getElementById('contentfrm').contentWindow.document;" +
                    "var logoFrame = document.getElementById('logofrm').contentDocument || document.getElementById('logofrm').contentWindow.document;" +
                    "mainFrame.defaultView.clickHereSubmit();" +
                    "mainFrame.getElementById('pppUserName_2').value = '" + getSetupUsername() + "';" +
                    "mainFrame.getElementById('pppPassword').value = '" + getSetupPassword() + "';" +
                    "mainFrame.getElementById('btnApply').click();" +
                    "setTimeout(function() {" +
                    "logoFrame.getElementById('setlogin').click();" +
                    "console.log('AmbeentSuccess');" +
                    "},5000);" +
                    "};";
        }



        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        SETUP_STATE
    }
}


