package com.example.millenicomtestapk.ap.tenda;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class EverestSG_V300  extends AccessPoint {

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String MAIN_SUFFIX = "/index.html";
    private static final String LOGIN_ERROR_SUFFIX = "/login.html?0";
    private static final String WSETTINGS_SUFFIX = "/main.html";
    private static final String WiSETTINGS_SUFFIX = "/wlcfgrefresh.wl?wlRefresh=0";

    private static final String TAG = EverestSG_V300.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "adminn";

        public EverestSG_V300(String gateway, String password, String username, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);

    }
    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    getSendCode() +
                    "amb_sendcode(document.getElementById('login-username'), '" + getUsername() + "').then(function(){" +
                    "amb_sendcode(document.getElementById('login-password'), '" + getPassword() + "');}).then(function(){" +
                    "setTimeout(function(){" +
                    "document.getElementById('submit').click();" +
                    "},2000);" +
                    "})" +
                    "};";

        else if (url.equals(getFormattedGateway() + LOGIN_ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + WSETTINGS_SUFFIX + "';" +
                    "}";

        else if (url.equals(getFormattedGateway() + WSETTINGS_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + WiSETTINGS_SUFFIX + "';" +
                    "}";


        else if (url.equals(getFormattedGateway() + WiSETTINGS_SUFFIX )) {
            String a = "javascript:{" +
                    initAmbeent()+
                    "var i = 0 ;" +
                    "var reapeatInterval = setInterval(function(){" +
                    "console.log('sure calisti');" +
                    "if(document.getElementById('wlchannel') != null){" +
                    "i++;" +
                    "var channel = document.getElementById('wlchannel');" +
                    "channel.value = '" + getOptimalChannel() + "';" +
                    "var evnt = new Event('change');" +
                    "channel.dispatchEvent(evnt);" +
                    "}" +
                    "if(i == 1){" +
                    "window.btnApply();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "},  3000);" +
                    "};";
            return a;
        }
        return  null;
    }
}
