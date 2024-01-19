package com.example.millenicomtestapk.ap.tplink;


import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TPLinkTL_WR841N extends AccessPoint {

    private static final String TAG = TPLinkTL_WR841N.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private static final String LOGIN_SUFFIX = "/";
    private static final String ERROR_SUFFIX = "/LoginRpm.htm?Save=Save";
    private static final String MAIN_SUFFIX = "/Index.htm";

    public TPLinkTL_WR841N(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username))setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "document.getElementById('userName').value= '" + getUsername() + "';" +
                    "document.getElementById('pcPassword').value= '" + getPassword() + "';" +
                    "document.getElementById('loginBtn').click();" +
                    "};";
        else if (url.contains(ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.contains(MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "if (mainFrame.document.getElementById('channel') == null) {" +
                    "parent.frames['bottomLeftFrame'].document.getElementById('a7').click();" +
                    "}" +
                    "else {" +
                    "clearInterval(repeatInterval);" +
                    "parent.frames['mainFrame'].document.getElementsByName('chanWidth')[0][1].selected = true;" +
                    "parent.frames['mainFrame'].document.getElementsByName('channel')[0].value =" + getOptimalChannel() + ";" +
                    "parent.frames['mainFrame'].document.getElementsByName('Save')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 2000);" +
                    "};";
        return null;
    }
}