package com.example.millenicomtestapk.ap.arris;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class ArrisNVG599 extends AccessPoint {

    public static final String TAG = ArrisNVG599.class.getSimpleName();

    public static final String MAIN_SUFFIX = "/cgi-bin/home.ha";
    public static final String WCONFIG_SUFFIX = "/cgi-bin/wconfig.ha";
    public static final String WADVANCEDCONFIG_SUFFIX = "/cgi-bin/wconfig-adv.ha";
    public static final String INCORRECT_LOGIN = "/cgi-bin/login.ha";
    public static final String LAST_WARNING = "/cgi-bin/wifiwarn.ha";
    public static final String LASTADV_WARNING = "/cgi-bin/wifiwarn-adv.ha";
    private static final String DEFAULT_PASSWORD = "=&=1//39&9";

    public ArrisNVG599(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + WCONFIG_SUFFIX + "';" +
                    "};";

        else if (url.equals(getFormattedGateway() + INCORRECT_LOGIN))
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";

        else if (url.equals(getFormattedGateway() + LAST_WARNING))
            return "javascript:{" +
                    initAmbeent()+
                    "document.getElementsByName('Continue')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + LASTADV_WARNING))
            return "javascript:{" +
                    initAmbeent()+
                    "document.getElementsByName('Continue')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "};";

        else if (url.equals(getFormattedGateway() + WCONFIG_SUFFIX))
            return "javascript:{" +
                    "if (document.getElementById('selectradio')!= null) {" +
                    "document.getElementById('channel').value = " + getOptimalChannel() + ";" +
                    "document.getElementsByName('Save')[0].click();" +
                    "} else if (document.getElementById('password')!= null) {" +
                    "document.getElementById('password').value = '" + getPassword() + "';" +
                    "document.getElementsByName('Continue')[0].click();" +
                    "}" +
                    "else {" +
                    "window.location.href = '" + getFormattedGateway() + WADVANCEDCONFIG_SUFFIX + "';" +
                    "}" +
                    "};";

        else if (url.equals(getFormattedGateway() + WADVANCEDCONFIG_SUFFIX))
            if (getOptimalChannel() <= 11) {
                return "javascript:{" +
                        "setInterval(() => {" +
                        "document.getElementById('obandwidth').value = 20;" +
                        "document.getElementById('ochannelplusauto').value = " + getOptimalChannel() + ";" +
                        "document.getElementsByName('Save')[0].click();" +
                        "}, 1300);" +
                        "};";
            } else {
                return "javascript:{" +
                        getLocalHostLoginScript() +
                        "setInterval(() => {" +
                        "document.getElementById('tbandwidth').value = 20;" +
                        "document.getElementById('tchannelplusauto').value = " + getOptimalChannel() + ";" +
                        "document.getElementsByName('Save')[0].click();" +
                        "}, 1300);" +
                        "};";

            }

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
                        132,
                        136,
                        140,
                        144,
                        149,
                        153,
                        157,
                        161,
                        165
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
