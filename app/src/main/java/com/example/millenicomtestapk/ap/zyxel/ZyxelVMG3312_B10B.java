package com.example.millenicomtestapk.ap.zyxel;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ZyxelVMG3312_B10B extends AccessPoint {

    private static final String TAG = ZyxelVMG3312_B10B.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login/login.html";
    private static final String ERROR_SUFFIX = "/login/login-page.cgi";
    private static final String MAIN_SUFFIX = "/index.html";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "turktelekom";

    public ZyxelVMG3312_B10B(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
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
                    initAmbeent()+
                    "setTimeout(function(){" +
                    "if(!document.getElementById('Message')){" +
                    "}" +
                    // Check if there is an error message. If there is, username or pass is wrong
                    "else if(document.getElementById('Message').innerHTML.length > 0){" +
                    // Ask for password and channel
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else if (document.querySelectorAll('input[type=button]')[1] != null){" +
                    "document.querySelectorAll('input[type=button]')[1].click();" +
                    "}" +
                    "}, 1000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "document.getElementById('network-wireless').click();" +
                    // Initializing function to check whether target frame exists and do work.
                    "var repeatInterval = setInterval(function(){" +
                    // Getting the current document content in iFrame.
                    "var iframeDocument = document.getElementById('mainFrame').contentDocument;" +
                    //Trying to get target checkbox.
                    "var channelMenu = iframeDocument.getElementsByName('wlChannel')[0];" +
                    // Checking whether the checkbox exists.
                    "if (channelMenu != null){" +
                    "iframeDocument.getElementsByName('wlChannel')[0][" + getOptimalChannel() + "].selected = true;" +
                    "iframeDocument.getElementsByName('sysSubmit')[0].click();" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    // End of if statement.
                    "}" +
                    // End of setInterval function.
                    "}, 1000);" +
                    "};";

        return null;
    }
}
