package com.example.millenicomtestapk.ap.tenda;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;


public class D301_v2 extends AccessPoint {
    private static final String TAG = D301_v2.class.getSimpleName();
    private static final String MAIN_SUFFIX = "/index.html";
    private static final String LOGIN_ERROR_SUFFIX = "/login.html?0";
    private static final String WSETTINGS_SUFFIX = "/wlswitchinterface0.wl";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private State state = State.LOGIN_STATE;

    public D301_v2(String gateway, String password, String username, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);


    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
//        Log.d(TAG, "State : " + state);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "setTimeout(() => {" +
                    "document.getElementById('login-username').value= '" + getUsername() + "';" +
                    "document.getElementById('login-password').value= '" + getPassword() + "';" +
                    "}, 300);" +
                    "setTimeout(() => {" +
                    "document.getElementById('submit').click();" +
                    "}, 800);" +
                    "};";
        }
        if (url.equals(getFormattedGateway() + LOGIN_ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX) && state == State.CREDENTIAL_CHECK_STATE)
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + WSETTINGS_SUFFIX + "';" +
                    "}";

        else if (url.equals(getFormattedGateway() + WSETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setInterval(() => {" +
                    "if (document.getElementById('wlchannel').selectedIndex !== null)" +
                    "document.getElementById('wlchannel').selectedIndex=" + getOptimalChannel() + ";" +
                    "if(document.getElementById('wlNBwCap').selectedIndex !== '1'){" +
                    "document.getElementById('wlNBwCap').selectedIndex='1';" +
                    "}" +
                    "initNCtrlsb();" +
                    "document.getElementById('apply_btn').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1300);" +
                    "};";


        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,

    }


}