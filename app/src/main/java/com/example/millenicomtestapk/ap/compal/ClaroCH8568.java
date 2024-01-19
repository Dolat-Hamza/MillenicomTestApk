package com.example.millenicomtestapk.ap.compal;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ClaroCH8568 extends AccessPoint {

    private static final String TAG = ClaroCH8568.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/common_page/login.php";
    private static final String MAIN_SUFFIX = "/";

    private static final String DEFAULT_USERNAME = "CLARO_EE42A2";
    private static final String DEFAULT_PASSWORD = "342CC4EE42A2";


    public ClaroCH8568(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "document.getElementById('Username').value = '" + getUsername() + "';" +
                    //                Test Modem Username: CLARO_EE42A2
                    "document.getElementById('Password').value = '" + getPassword() + "';" +
                    //                Test Modem Password: 342CC4EE42A2
                    "document.getElementsByClassName('holder-icon')[0].click();" +

                    "setTimeout(function () {" +
                    "if (document.getElementsByClassName('label')[0].textContent == 'Login incorreto'){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "}, 4000); " + "}";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function () {" +
                    "window.scrollTo(0,0);" +
                    "document.getElementsByClassName('next config')[0].click();" +
                    "}, 5000); " +

                    "setTimeout(function () {" +
                    "document.getElementsByClassName('main-menu-button')[0].click();" +
                    "document.getElementById('menu-wi-fi').click();" +
                    "document.getElementsByClassName('item-menu')[33].children[0].click();" +
                    "}, 7000); " +

                    "setTimeout(function () {" +
                    "document.getElementById('ChannelSetting').value = " + getOptimalChannel() + ";" +
                    "document.getElementsByClassName('button icon')[0].click();" +
                    "}, 11000); " +
                    "setTimeout(function () {" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 16000); " + "}";
        return null;
    }
}