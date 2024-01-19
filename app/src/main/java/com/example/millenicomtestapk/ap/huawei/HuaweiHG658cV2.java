package com.example.millenicomtestapk.ap.huawei;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class HuaweiHG658cV2 extends AccessPoint {

    private static final String TAG = HuaweiHG658cV2.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/";
    private static final String MAIN_SUFFIX = "/html/wizard/wizard.html";
    private static final String SETTINGS_SUFFIX = "/html/advance.html#wlan";

    private static final String DEFAULT_USERNAME = "vodafone";
    private static final String DEFAULT_PASSWORD = "vodafone";

    public HuaweiHG658cV2(String gateway, String username, String password, int callbackPort) {
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
                    "document.getElementById('index_username').value = '" + getUsername() + "';" +
                    "document.getElementById('password').value = '" + getPassword() + "';" +
                    "document.getElementById('loginbtn').click();" +

                    "setTimeout(function () { " +
                    "if(document.getElementById('errorCategory').innerHTML.length > 0){ " +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "}, 1000);" +

                    "var repeatInterval = setInterval(function() {" +
                    "if(document.getElementById('setfirstbutton') != null){" +
                    "document.getElementById('setfirstbutton').click();" +
                    "clearInterval(repeatInterval);" +
                    "}" +
                    "}, 1200) " +
                    "};";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function(){" +
                    "if (document.getElementById('channel24g_ctrl') == null){" +
                    "document.getElementById('wlan_ss_title_ctrl').click();" +
                    "}" +
                    "else {" +
                    "clearInterval(repeatInterval);" +
                    "document.getElementById('channel24g_ctrl').value = '" + getOptimalChannel() + "';" +
                    "$(channel24g_ctrl).change();" +
                    "document.getElementById('SendSettings_submitbutton').children[1].click();" +
                    "}" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1000);" +
                    "};";

        return null;
    }
}