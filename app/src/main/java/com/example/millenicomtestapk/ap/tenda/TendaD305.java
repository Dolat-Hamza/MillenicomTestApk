package com.example.millenicomtestapk.ap.tenda;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TendaD305 extends AccessPoint {

    private static final String TAG = TendaD305.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String MAIN_SUFFIX = "/index.html";
    private static final String LOGIN_ERROR_SUFFIX = "/login.html?0";
    private static final String WSETTINGS_SUFFIX = "/wlswitchinterface0.wl";
    private static final String WiSETTINGS_SUFFIX = "/wlcfgrefresh.wl?wlRefresh=0";


    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    public TendaD305(String gateway, String password, String username, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "document.getElementById('login-username').value= '" + getUsername() + "';" +
                    "document.getElementById('login-password').value= '" + getPassword() + "';" +
                    "setTimeout(() => {" +
                    "document.getElementById('submit').click();" +
                    "}, 300);" +
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

        else if (url.equals(getFormattedGateway() + WSETTINGS_SUFFIX) || url.equals(getFormattedGateway() + WiSETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "if (document.getElementById('wlchannel').selectedIndex != null){" +
                    "document.getElementById('wlchannel').selectedIndex=" + getOptimalChannel() + ";" +
                    "}" +
                    "if(document.getElementById('wlNBwCap').selectedIndex != '1'){" +
                    "document.getElementById('wlNBwCap').selectedIndex='1';" +
                    "}" +
                    "document.getElementById('apply_btn').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1300);" +
                    "};";

        return null;
    }
}
