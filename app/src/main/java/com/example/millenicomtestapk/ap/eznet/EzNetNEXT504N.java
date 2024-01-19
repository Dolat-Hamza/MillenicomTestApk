package com.example.millenicomtestapk.ap.eznet;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class EzNetNEXT504N extends AccessPoint {

    public static final String TAG = EzNetNEXT504N.class.getSimpleName();


    public static final String LOGIN_SUFFIX = "/login.html";
    public static final String MAIN_SUFFIX = "/index.html";
    public static final String WRONG_SUFFIX = "/login.html?1";

    private static final String DEFAULT_PASSWORD = "000000";

    public EzNetNEXT504N(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "document.getElementById('login-password').value = '" + getPassword() + "';" +
                    "document.getElementById('save').click();" +
                    "};";
        if (url.equals(getFormattedGateway() + WRONG_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() { " +
                    "document.getElementById('wireless').click();" +
                    "console.log('clicked wireless'); " +
                    "}, 5000);" +

                    "setTimeout(function() { " +
                    "document.getElementById('wifiChannel').value = '" + getOptimalChannel() + "';" +
                    "document.getElementById('wifiBandwidth').value = '1';" +
                    "console.log('set channel'); " +
                    "}, 10000);" +

                    "setTimeout(function() { " +
                    "document.getElementById('submit').click();" +
                    "console.log('clicked channel'); " +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 15000);" +

                    "};";

        return null;
    }
}
