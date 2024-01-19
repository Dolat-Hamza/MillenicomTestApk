package com.example.millenicomtestapk.ap.huawei;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;


public class HuaweiHG552e extends AccessPoint {


    public static final String LOGIN_SUFFIX = "/";
    public static final String DEFAULT_PASSWORD_SUFFIX = "/html/management/admin_content.asp";
    public static final String MAIN_SUFFIX = "/html/content1.asp";
    public static final String SETTINGS_SUFFIX = "/html/wizard/quickwlan.asp";
    private static final String TAG = HuaweiHG552e.class.getSimpleName();
    private static final String DEFAULT_USERNAME = "vodafone";
    private static final String DEFAULT_PASSWORD = "vodafone";
    private State state = State.LOGIN_STATE;

    public HuaweiHG552e(String gateway, String username, String password, int callbackPort) {
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
                String a = "javascript:{" +
                        "setTimeout (() => {" +
                        "document.getElementById('txt_Username').value ='" + getUsername() + "';" +
                        "document.getElementById('txt_Password').value ='" + getPassword() + "';" +
                        "setTimeout (() => {" +
                        "document.getElementById('btnLogin').click();" +
                        "}, 2000);" +
                        "}, 2000);" +
                        "};";
                return a;
            } else {
                return "javascript:{" +
                        initAmbeent()+
                        "if (document.getElementById('erroinfoId') !==null) {" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "};";
            }
        } else if (url.equals(getFormattedGateway() + DEFAULT_PASSWORD_SUFFIX))
            return "javascript:{" +
                    "parent.frames['cofrm'].document.getElementsByClassName('changebutton')[1].click();" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return
                    "javascript:{" +
                            initAmbeent()+
                            "var isSettingsClicked = false;" +
                            "var isWLANClicked = false;" +
                            "var repeat = setInterval(() => {" +
                            "if (!isSettingsClicked) {" +
                            "var menuFrame = document.getElementById('listfrm').contentDocument || document.getElementById('listfrm').contentWindow.document;" +
                            "menuFrame.getElementById('link_User_1').click();" +
                            "isSettingsClicked = true;" +
                            "} else if (!isWLANClicked) {" +
                            "var menuFrame = document.getElementById('listfrm').contentDocument || document.getElementById('listfrm').contentWindow.document;" +
                            "menuFrame.getElementById('link_User_1_2').click();" +
                            "isWLANClicked = true;" +
                            "} else {" +
                            "clearInterval(repeat);" +
                            "var mainFrame = document.getElementById('contentfrm').contentDocument || document.getElementById('contentfrm').contentWindow.document;" +
                            "var bandWidth = mainFrame.getElementsByName('bwControl')[0];" +
                            "var wlChannel = mainFrame.getElementsByName('wlChannel')[0];" +
                            "bandWidth.selectedIndex = 0;" +
                            "wlChannel.selectedIndex = " + getOptimalChannel() + ";" +
                            "mainFrame.getElementsByName('btnApply')[0].click();" +
                            "setTimeout(function() {" +
                            ambeent(String.valueOf(getOptimalChannel())) +
                            "},2000);" +
                            "}}, 1300);" +
                            "}";

        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE

    }
}