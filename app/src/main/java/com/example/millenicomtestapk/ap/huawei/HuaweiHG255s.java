package com.example.millenicomtestapk.ap.huawei;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class HuaweiHG255s extends AccessPoint {

    private static final String TAG = HuaweiHG255s.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/";
    private static final String MAIN_SUFFIX = "/html/wizard/wizard.html";
    private static final String SETTINGS_SUFFIX = "/html/wizard/wifi.html";
    private static final String DONE_SUFFIX = "/html/wizard/network.html";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "superonline";

    private boolean isLoggedIn;

    public HuaweiHG255s(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        if (!isLoggedIn && url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    // Writing username.
                    "document.getElementById('index_username').value= '" + getUsername() + "';" +
                    // Writing password.
                    "document.getElementById('password').value = '" + getPassword() + "';" +
                    // Login click.
                    "document.getElementById('loginbtn').click();" +

                    "setTimeout(function () { " +
                    // Check if there is an error message. If there is, username or pass is wrong
                    "if(document.getElementById('errorCategory').innerHTML.length > 0){ " +
                    // Ask for password and channel
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    //getLocalHostAskScript() +
                    "} " +
                    "}, 1000);" +

                    // If the default password is not changed, a dialog box that warns you about changing password appears. So we need interval to be able to click it.
                    "var repeatInterval = setInterval(function() {" +
                    "if(document.getElementById('setfirstbutton') != null){" +
                    // Click the dialog box button to login.
                    "document.getElementById('setfirstbutton').click();" +
                    "clearInterval(repeatInterval);" +
                    // Closing if braces
                    "}" +
                    // End of setInterval function.
                    "}, 1200) " +
                    // Javascript braces.
                    "};";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX)) {
            isLoggedIn = true;
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "try {" +
                    "var repeatInterval = setInterval(function(){" +
                    (
                            getOptimalChannel() <= 13 ?
                                    "if (document.getElementById('channel24g_ctrl') != null){" +
                                            "document.getElementById('channel24g_ctrl').value = " + getOptimalChannel() + ";" +
                                            "$(channel24g_ctrl).change();" +
                                            "document.getElementById('wifi_wizard_save').click();" +
                                            "setTimeout(function() {" +
                                            ambeent(String.valueOf(getOptimalChannel())) +
                                            "},2000);" +
                                            "clearInterval(repeatInterval);" +
                                            "}"
                                    :
                                    "if (document.getElementById('channel5g_ctrl') != null){" +
                                            "document.getElementById('channel5g_ctrl').value = " + getOptimalChannel() + ";" +
                                            "$(channel5g_ctrl).change();" +
                                            "document.getElementById('wifi_wizard_save').click();" +
                                            "setTimeout(function() {" +
                                            ambeent(String.valueOf(getOptimalChannel())) +
                                            "},2000);" +
                                            "clearInterval(repeatInterval);" +
                                            "}"
                    ) +
                    "}, 1000);" +
                    "} catch(err) {" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + DONE_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "};";
        return null;
    }

    @Override
    public boolean channelIsValid(int channelType, int scanResultChannelWith, int channel) {
        switch (channelType) {
            case 0: {
                int[][] channelList = {{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13},
                        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}};
                if (scanResultChannelWith > 1) return false;
                List<Integer> list2 = new ArrayList<>();
                for (int a : channelList[scanResultChannelWith]) list2.add(a);
                return list2.contains(channel);
            }
            case 1: {
                int[][] channelList = {{
                        36,
                        40,
                        44,
                        48,
                        52,
                        56,
                        60,
                        64,
                        100,
                        104,
                        108,
                        112,
                        116
                },
                        {},
                        {},
                        {36, 40, 44, 52, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161}};
                if (scanResultChannelWith > 3) return false;
                List<Integer> list = new ArrayList<>();
                for (int a : channelList[scanResultChannelWith]) list.add(a);
                return list.contains(channel);
            }
            default:
                return false;
        }
    }
}

