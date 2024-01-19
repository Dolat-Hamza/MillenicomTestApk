package com.example.millenicomtestapk.ap.tenda;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TendaF3 extends AccessPoint {

    private static final String TAG = TendaF3.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String MAIN_SUFFIX = "/index.html";
    private static final String LOGIN_ERROR_SUFFIX = "/login.html?1";
    private static final String CREDENTIALS_SUFFIX = "/quickset.html";

    private static final String DEFAULT_PASSWORD = "admin";

    public TendaF3(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "document.getElementById('login-password').value= '" + getPassword() + "';" +
                    "setTimeout(() => {" +
                    "document.getElementById('save').click();" +
                    "}, 300);" +
                    "};";

        else if (url.equals(getFormattedGateway() + LOGIN_ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";

        else if (url.equals(getFormattedGateway() + CREDENTIALS_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setInterval(() => {" +
                    "document.getElementById('wanPPPoEUser').value= '" + getPassword() + "';" +
                    "document.getElementById('wanPPPoEPwd').value= '" + getPassword() + "';" +
                    "document.getElementById('save').click();" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}, 1000);" +
                    "}";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function(){" +
                    "document.getElementById('wireless').click();" +
                    "if (document.getElementById('wifiChannel') != null){" +
                    "document.getElementById('wifiChannel').value = " + getOptimalChannel() + ";" +
                    "document.getElementById('wifiBandwidth').value = '20';" +
                    "document.getElementById('submit').click();" +
                    "clearInterval(repeatInterval);" +
                    "}" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1500);" +
                    "};";

        return null;
    }
}


