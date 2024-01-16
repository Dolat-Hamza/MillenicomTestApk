package com.example.millenicomtestapk.ap.actiontec;

import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ActiontecGT784WNV extends AccessPoint {
    public static final String TAG = ActiontecGT784WNV.class.getSimpleName();
    public static final String DEFAULT_USERNAME = "admin";
    private static final String LOGIN_SUFFIX = "/login.html";
    private State state = State.LOGIN_STATE;

    public ActiontecGT784WNV(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (state == State.LOGIN_STATE) {
            if (getPassword() == null) {
                return getLocalHostAskScript();
            }

            Log.d(TAG, "LOGIN STATE");
            Log.d(TAG, getFormattedGateway());
            if (url.equals(getFormattedGateway() + LOGIN_SUFFIX) || (url.equals(getFormattedGateway() + "/login.html#")) || (url.equals(getFormattedGateway() + "/"))) {
                return "javascript:{" +
                        "document.getElementsByName('adminPassword')[0].value = '" + getPassword() + "';" +
                        "document.getElementsByName('rt_adminPassword')[0].value = '" + getPassword() + "';" +
                        "setTimeout(() => { " +
                        "document.getElementById('apply_btn').click();" +
                        "}, 300);" +
                        "setTimeout(() => { " +
                        "var pure = '" + getFormattedGateway() + "';" +
                        "var link = pure.slice(0, 7) + 'admin:" + getPassword() + "@' + pure.slice(7) + '/wirelesssetup_basicsettings.html';" +
                        "window.location.href = link;" +
                        "}, 1500);" +
                        "};";
            } else {
                state = State.CREDENTIAL_CHECK_STATE;
                return "javascript:{" +
                        "var pure = '" + getFormattedGateway() + "';" +
                        "var link = pure.slice(0, 7) + 'admin:" + getPassword() + "@' + pure.slice(7) + '/wirelesssetup_basicsettings.html';" +
                        "window.location.href = link;" +
                        "};";
            }
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            if (url.equals(getFormattedGateway() + "/login.html") || (url.equals(getFormattedGateway() + "/login.html#"))) {
                return getLocalHostAskScript();
            }
            state = State.CHANNEL_CHANGE_STATE;
            Log.d(TAG, "CRED STATE");
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(() => {" +
                    "console.log(document.title);" +
                    "if (document.title.includes('401')) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else {" +
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + 'admin:" + getPassword() + "@' + pure.slice(7) + '/wirelesssetup_basicsettings.html';" +
                    "window.location.href = link;" +
                    "}" +
                    "}, 2000);" +
                    "};";
        } else if (state == State.CHANNEL_CHANGE_STATE) {
            Log.d(TAG, "CHANGE STATE");
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(() => {" +
                    "document.getElementById('id_channel').selectedIndex = " + getOptimalChannel() + ";" +
                    "}, 1000);" +
                    "setTimeout(() => {" +
                    "onChangeChannel();" +
                    "}, 1300);" +
                    "setTimeout(() => {" +
                    "document.getElementById('apply_btn').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1500);" +
                    "};";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        CHANNEL_CHANGE_STATE
    }
}

