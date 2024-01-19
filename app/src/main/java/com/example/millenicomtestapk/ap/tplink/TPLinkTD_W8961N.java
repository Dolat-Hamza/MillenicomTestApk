package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;


public class TPLinkTD_W8961N extends AccessPoint {

    private static final String TAG = TPLinkTD_W8961N.class.getSimpleName();
    private static final String LOGIN_SUFFIX = "/login_security.html";
    private static final String PASSWORD_SETTINGS_SUFFIX = "/wizard/wizardStart.html";
    private static final String MAIN_SUFFIX = "/rpSys.html";
    private static final String SETTINGS_SUFFIX = "/basic/home_wlan.htm";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private State state = State.LOGIN_STATE;

    public TPLinkTD_W8961N(String gateway, String password, String username, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);

    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE;
                return "javascript:{" +
                        "document.getElementsByName('Login_Name')[0].value= '" + getUsername() + "';" +
                        "document.getElementsByName('Login_Pwd')[0].value= '" + getPassword() + "';" +
                        "setTimeout(() => {" +
                        "document.getElementsByName('texttpLoginBtn')[0].click();" +
                        "}, 300);" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent()+
                        "if (document.getElementById('tr1')!==null){" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "  }" +
                        "};";
            }
        } else if (url.equals(getFormattedGateway() + PASSWORD_SETTINGS_SUFFIX))
            return "javascript:{" +
                    "if (document.getElementsByName('ExitBtn')[0] !== null){" +
                    "document.getElementsByName('ExitBtn')[0].click()" +
                    "}" +
                    "}";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "if (parent.frames['navigation'].document.getElementsByClassName('nav-main-sel') !== null){" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "if (document.getElementsByName('Channel_ID') !== null){" +
                    "document.getElementsByName('Channel_ID')[0].selectedIndex= " + getOptimalChannel() + ";" +
                    "}" +
                    "setTimeout(() => {" +
                    "document.getElementsByName('SaveBtn')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1000);" +
                    "};";


        return null;

    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE
    }
}






