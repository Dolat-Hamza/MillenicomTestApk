package com.example.millenicomtestapk.ap.zyxel;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

public class ZyxelP_1302_T10DV3 extends AccessPoint {

    private static final String LOGIN_SUFFIX = "/cgi-bin/login.html";
    private static final String DEFAULT_PASSWORD_PASS_SUFFIX = "/cgi-bin/passWarning.html";
    private static final String MAIN_SUFFIX = "/cgi-bin/main.html";
    private static final String SETTINGS_SUFFIX = "/cgi-bin/tabFW.html?tabJson=../pages/network/wireless/tab.json";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "12345";

    private boolean loginAttempted = false;

    public ZyxelP_1302_T10DV3(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        if (!loginAttempted && url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            loginAttempted = true;
            return "javascript:{" +
                    getSendCode() +
                    "amb_sendcode(document.getElementById('Loginuser'), '" + getUsername() + "').then(function(){" +
                    "amb_sendcode(document.getElementById('LoginPassword'), '" + getPassword() + "');}).then(function(){" +
                    "setTimeout(function(){" +
                    "document.getElementById('Login_ID').click();" +
                    "},2000);" +
                    "})" +

                    "};";
        } else if (loginAttempted && url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + DEFAULT_PASSWORD_PASS_SUFFIX))
            return "javascript:{" +
                    "document.getElementById('Skip_ID').click();" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX)) {
            String a = "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function(){" +
                    "if(document.getElementById('ChannelSelection') != null) {" +
                    " document.getElementsByName('ChannelSelection')[0].value= '" + getOptimalChannel() + "';" +
                    "if (document.getElementsByClassName('right_table')[9].innerText !== document.getElementById('ChannelSelection').value){" +
                    "window.doGeneralSave();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "clearInterval(repeatInterval);" +
                    "}" +
                    "}, 2000);" +
                    "};";
            return a;
        }
        return null;
    }
}