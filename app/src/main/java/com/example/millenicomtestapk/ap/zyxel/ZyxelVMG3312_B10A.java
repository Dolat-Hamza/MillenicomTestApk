package com.example.millenicomtestapk.ap.zyxel;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ZyxelVMG3312_B10A extends AccessPoint {

    private static final String TAG = ZyxelVMG3312_B10A.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login/login.html";
    private static final String ERROR_SUFFIX = "/login/login-page.cgi";
    private static final String MAIN_SUFFIX = "/index.html";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "ttnet";


    public ZyxelVMG3312_B10A(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }


    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if(document.getElementsByClassName(`error`)[2] == null && document.getElementsByClassName(`error`)[3] == null){" +
                    "document.getElementById('AuthName').value = '" + getUsername() + "';" +
                    "document.getElementById('AuthPassword').value = '" + getPassword() + "';" +
                    "document.getElementById('login').submit();" +
                    "}" +
                    "else {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementById('Message').innerHTML.length > 0){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "window.location.href = '" + getFormattedGateway() + MAIN_SUFFIX + "';" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "document.getElementById(`network-wireless`).click();" +
                    "var repeatInterval = setInterval(function(){" +
                    "parent.frames['mainFrame'].document.getElementById('btn_more').click();" +
                    "parent.frames['mainFrame'].document.getElementsByName('wlNBwCap')[0][0].selected = true;" +
                    "parent.frames['mainFrame'].sidebandChange(false);" +
                    "parent.frames['mainFrame'].document.getElementsByName('wlChannel')[0][" + getOptimalChannel() + "].selected = true;" +
                    "parent.frames['mainFrame'].document.getElementsByName('sysSubmit')[0].click();" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1300);" +
                    "};";

        return null;
    }
}