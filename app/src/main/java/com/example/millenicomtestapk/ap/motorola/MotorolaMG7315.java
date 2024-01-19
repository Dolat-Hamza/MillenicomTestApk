package com.example.millenicomtestapk.ap.motorola;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class MotorolaMG7315 extends AccessPoint {

    public static final String TAG = MotorolaMG7315.class.getSimpleName();
    private static final String LOGIN_SUFFIX = "/";
    private static final String ERROR_SUFFIX = "/login.asp";
    private static final String HOME_SUFFIX = "/MotoHome.asp";
    private static final String WIFI_SUFFIX = "/MotoWlanBasic.asp";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "motorola";

    public MotorolaMG7315(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            return "javascript:{" +
                    "if (document.getElementsByName('loginUsername').length > 0) { " +
                    "document.getElementsByName('loginUsername')[0].value = '" + getUsername() + "'; " +
                    "document.getElementsByName('loginPassword')[0].value = '" + getPassword() + "'; " +
                    "document.getElementsByClassName('moto-login-button')[0].click();" +
                    "}" +
                    "};";
        } else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + HOME_SUFFIX))
            return "javascript:{" +
                    "window.location = '" + getFormattedGateway() + WIFI_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + WIFI_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "if (document.getElementsByName('MotoWlanBasicChannel').length > 0){" +
                    "document.getElementsByName('MotoWlanBasicChannel')[0].value = " + getOptimalChannel() + ";" +
                    "Apply(4);" +
                    "clearInterval(repeatInterval);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 1000);" +
                    "};";
        return null;
    }
}
