package com.example.millenicomtestapk.ap.asus;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class AsusDSL_AC52U extends AccessPoint {

    public static final String TAG = AsusDSL_AC52U.class.getSimpleName();
    public static final String MAIN_SUFFIX = "/cgi-bin/index2.asp";
    public static final String SETTINGS_SUFFIX = "/cgi-bin/Advanced_Wireless_Content.asp";
    public static final String REDIRECT_2GHZ = "/cgi-bin/Wireless_Content_redirect.asp?refresh=2";
    public static final String REDIRECT_5GHZ = "/cgi-bin/Wireless_Content_redirect.asp?refresh=5";
    private static final String LOGIN_SUFFIX = "/Main_Login.asp";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    public AsusDSL_AC52U(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "if (document.getElementById('error_status_field').innerHTML.length > 0) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else { " +
                    "document.getElementById('login_username').value = '" + getUsername() + "';" +
                    "document.getElementsByName('login_passwd')[0].value = '" + getPassword() + "';" +
                    "document.getElementsByClassName('button')[0].click();" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + ((getOptimalChannel() <= 13) ? REDIRECT_2GHZ : REDIRECT_5GHZ) + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "if (document.getElementById('applyButton') != null) {" +
                    "document.getElementsByName('wl_channel')[0].value = " + getOptimalChannel() + ";" +
                    "winWidth = 123;" +
                    "applyRule();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
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
                        116,
                        120,
                        124,
                        128,
                        132,
                        136,
                        140,
                },
                        {},
                        {},
                        {}};
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
