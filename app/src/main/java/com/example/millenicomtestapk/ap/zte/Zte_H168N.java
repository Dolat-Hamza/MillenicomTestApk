package com.example.millenicomtestapk.ap.zte;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class Zte_H168N extends AccessPoint {

    public static final String TAG = Zte_H168N.class.getSimpleName();
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "ttnet";
    public static final String RELOAD_SUFFIX = "/template.gch";
    public static final String MAIN_SUFFIX = "/getpage.gch?pid=1002&nextpage=welcome.gch";
    private static final String LOGIN_SUFFIX = "/";
    private static final String SETTINGS_SUFFIX = "/template.gch?pid=1002&nextpage=net_11n_conf_t.gch";
    private State state = State.LOGIN_STATE;

    public Zte_H168N(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);

    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + RELOAD_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + RELOAD_SUFFIX))
            if (state == State.LOGIN_STATE) {
                state = State.PASS_STATE;
                return "javascript:{" +
                        "document.getElementById('Frm_Username').value = '" + getUsername() + "';" +
                        "document.getElementById('Frm_Password').value = '" + getPassword() + "';" +
                        "document.getElementById('Frm_Login').click();" +
                        "};";
            } else {
                return "javascript:{" +
                        "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                        "};";
            }
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if(document.getElementById('meta_menu_mmNet') == null ){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else {" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "}" +
                    "};";

        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setInterval(() => {" +
                    "document.getElementById('Frm_BandWidth').selectedIndex=0;" +
                    "document.getElementById('Frm_Channel').selectedIndex= " + getOptimalChannel() + ";" +
                    "document.getElementById('Btn_Submit').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1300);" +
                    "};";


        return null;
    }

    enum State {
        LOGIN_STATE,
        PASS_STATE,


    }
}
