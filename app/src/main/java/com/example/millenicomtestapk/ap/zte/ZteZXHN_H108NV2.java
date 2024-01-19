package com.example.millenicomtestapk.ap.zte;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;


public class ZteZXHN_H108NV2 extends AccessPoint {

    private static final String TAG = ZteZXHN_H108NV2.class.getSimpleName();
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "ttnet";
    private State state = State.LOGIN_STATE;

    public ZteZXHN_H108NV2(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, "State : " + state);
        Log.d(TAG, url);
        if (state == State.LOGIN_STATE) {
            state = State.CREDENTIAL_CHECK_STATE;
            return "javascript:{" +
                    "setTimeout(function(){" +
                    "document.getElementById('Frm_Username').value='" + getUsername() + "';" +
                    "document.getElementById('Frm_Password').value='" + getPassword() + "';" +
                    "window.dosubmit();" +
                    "}, 2000);" +
                    "};";
        } else if (state == State.CREDENTIAL_CHECK_STATE) {
            state = State.SETTINGS_STATE;
            String a = "javascript:{" +
                    initAmbeent()+
                    "var kullan = document.getElementById('msg-table');" +
                    "var kullan2 = document.getElementsByClassName('note')[0];" +
                    "if(kullan){" +
                    "window.pageCancel();" +
                    "}" +
                    "else if (kullan2){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "location = location;" +
                    "}" +
                    "else {" +
                    "}" +
                    "};";
            return a;
        } else if (state == State.SETTINGS_STATE) {
            return "javascript:{" +
                    initAmbeent()+
                    "if(parent.frames['mainFrame'].document.getElementById('mmNet') !== null){" +
                    "parent.frames['mainFrame'].document.getElementById('mmNet').click();" +
                    "}" +
                    "var repeatInterval = setInterval(function(){" +
                    "if(parent.frames['mainFrame'].document.getElementById('smWLAN') !== null) {" +
                    "parent.frames['mainFrame'].document.getElementById('smWLAN').click();" +
                    "parent.frames['mainFrame'].document.getElementById('Frm_Channel').value = " + getOptimalChannel() + ";" +
                    "parent.frames['mainFrame'].document.getElementById('Btn_Submit').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "clearInterval(repeatInterval);" +
                    "}" +
                    "}, 1200);" +
                    "};";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,
        SETTINGS_STATE;

    }
}