package com.example.millenicomtestapk.ap.dlink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class DLinkDSL_2740U extends AccessPoint {

    public static final String TAG = DLinkDSL_2740U.class.getSimpleName();
    public static final String LOGIN_SUFFIX = "/admin/index.html#/home";
    public static final String SETTINGS_SUFFIX = "/admin/index.html#/wifi/common";
    public static final String WSETTINGS_SUFFIX = "/admin/index.html#/wifi/common?freq=2.4GHz";
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "password";
    private State state = State.LOGIN_STATE;

    public DLinkDSL_2740U(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        Log.d(TAG, "State : " + state);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE;
                return "javascript:{" +
                        "angular.element(document.getElementById('loginDialogUsername')).scope().fm.username = '" + getUsername() + "';" +
                        "angular.element(document.getElementById('loginDialogUsername')).scope().fm.password = '" + getPassword() + "';" +
                        "setTimeout(function(){" +
                        "angular.element(document.getElementsByClassName('dialog_button_panel button_block right')[0]).scope().login()" +
                        "}, 300);" +
                        "};";
            } else if (state == State.CREDENTIAL_CHECK_STATE) {
                return "javascript:{" +
                        initAmbeent()+
                        "if (angular.element(document.getElementById('loginDialogUsername')).scope() != null) {" +
                        "angular.element(document.getElementById('loginDialogUsername')).scope().fm.username = '" + getUsername() + "';" +
                        "angular.element(document.getElementById('loginDialogUsername')).scope().fm.password = '" + getPassword() + "';" +
                        "setTimeout(function(){" +
                        "angular.element(document.getElementsByClassName('dialog_button_panel button_block right')[0]).scope().login()" +
                        "}, 300);" +
                        "}" +

                        "setTimeout(function(){" +
                        "if (angular.element(document.getElementById('loginDialogUsername')).scope() == null){" +
                        "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                        "}" +
                        "else{" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "}, 4000);" +
                        "};";
            }
        } else if (url.equals(getFormattedGateway() + WSETTINGS_SUFFIX)) {
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "if(document.getElementsByClassName('full-width input input_button ng-scope ng-binding disabled')[0] != null){" +
                    "document.getElementsByClassName('nw_labeled_switch switch ng-scope')[1].click();" +
                    "}" +
                    "angular.element(document.getElementsByClassName('nwfield_element nwfield_element ng-scope ng-isolate-scope')[2]).scope().wifi.APInfo.radio.Channel = " + getOptimalChannel() + ";" +
                    "clearInterval(repeatInterval);" +
                    "}, 1300);" +
                    "setTimeout (function(){" +
                    "document.getElementsByClassName('button_block left')[0].children[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 8000);" +
                    "};";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
    }
}
