package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TpLinkTL_WR740N extends AccessPoint {

    private static final String TAG = TpLinkTL_WR740N.class.getSimpleName();

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private static final String LOGIN_SUFFIX = "/";
    private static final String MAIN_SUFFIX = "/userRpm/Index.htm";

    public TpLinkTL_WR740N(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "var pure = '" + getFormattedGateway() + "';" +
                    "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getPassword() + "@' + pure.slice(7) + '" + MAIN_SUFFIX + "';" +
                    "window.location.href = link;" +
                    "};";
        else if (url.contains(MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "if (parent.frames['bottomLeftFrame'] == null) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else if (parent.frames['mainFrame'].document.getElementsByClassName('listS')[1] == null) {" +
                    "parent.frames['bottomLeftFrame'].document.getElementById('a7').click();" +
                    "}" +
                    "else {" +
                    initAmbeent() +
                    "clearInterval(repeatInterval);" +
                    "parent.frames['mainFrame'].document.getElementsByClassName('listS')[1][1].selected = true;" +
                    "parent.frames['mainFrame'].document.getElementById('channel').value =" + getOptimalChannel() + ";" +
                    "parent.frames['mainFrame'].document.getElementById('Save').click();" +
                    "setTimeout(function(){" +
                    "parent.frames['mainFrame'].document.getElementById('t_click').click();" +
                    "}, 1500);" +
                    "setTimeout(function(){" +
                    "parent.frames['mainFrame'].confirm = false;" +
                    "parent.frames['mainFrame'].document.getElementsByName('Reboot')[0].click();" +
                    "}, 5000);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 1500);" +
                    "};";
        return null;
    }
}
