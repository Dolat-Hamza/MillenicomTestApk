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

    public HuaweiHG531s(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
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
                        initAmbeent()+
                        "if(document.getElementById('erroinfoId').innerHTML.length > 75){" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "};";
            }
        } else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "var isSettingsClicked = false;" +
                    "var isWLANClicked = false;" +
                    "var repeat = setInterval(() => {" +
                    "if (!isSettingsClicked) {" +
                    "var menuFrame = document.getElementById('listfrm').contentDocument || document.getElementById('listfrm').contentWindow.document;" +
                    "menuFrame.getElementById('link_User_1').click();" +
                    "isSettingsClicked = true;" +
                    "} else if (!isWLANClicked) {" +
                    "var menuFrame = document.getElementById('listfrm').contentDocument || document.getElementById('listfrm').contentWindow.document;" +
                    "menuFrame.getElementById('link_User_1_2').click();" +
                    "isWLANClicked = true;" +
                    "} else {" +
                    "clearInterval(repeat);" +
                    "var mainFrame = document.getElementById('contentfrm').contentDocument || document.getElementById('contentfrm').contentWindow.document;" +
                    "var bandWidth = mainFrame.getElementsByName('bwControl')[0];" +
                    "var wlChannel = mainFrame.getElementsByName('wlChannel')[0];" +
                    "bandWidth.selectedIndex = 0;" +
                    "wlChannel.selectedIndex = " + getOptimalChannel() + ";" +
                    "mainFrame.getElementsByName('btnApply')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}}, 1300);" +
                    "};";

        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE
    }
}


