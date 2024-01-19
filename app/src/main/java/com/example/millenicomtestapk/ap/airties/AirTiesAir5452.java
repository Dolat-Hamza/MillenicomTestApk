package com.example.millenicomtestapk.ap.airties;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

//import timber.log.Timber;

public class AirTiesAir5452 extends AccessPoint {

    private static final String TAG = AirTiesAir5452.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String ERROR_SUFFIX = "/login.html?ErrorCode=6&redirect=&self=1";
    private static final String MAIN_SUFFIX = "/main.html";
    private static final String SETTINGS_SUFFIX = "/wireless/settings.html";

    private static final String DEFAULT_PASSWORD = "";

    public AirTiesAir5452(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (password == null || password.isEmpty()) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
       // Timber.e(TAG + ", " + url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "parent.frames['mainFrame'].document.getElementById('uiPostPassword').value = '" + getPassword() + "';" +
                    "setTimeout(function () {" +
                    "parent.frames['mainFrame'].document.getElementById('__ML_ok').click();" +
                    "}, 300); " +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "}";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            if (getOptimalChannel() < 14) {
                return "javascript:{" +
                        initAmbeent()+
                        "var repeatInterval = setInterval(function () {" +
                        "if (document.getElementById('cmb_freq').value == 2400){" +
                        "document.getElementById('cmb_channel').value = '" + getOptimalChannel() + "';" +
                        "document.getElementById('SaveButton').click();" +
                        "clearInterval(repeatInterval);" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "else {" +
                        "document.getElementById('cmb_freq').value == 2400;" +
                        "cmb_freq.onchange('2400');" +
                        "}" +
                        "}, 3500); " +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent()+
                        "var repeatInterval = setInterval(function () {" +
                        "if (document.getElementById('cmb_freq').value == 5000){" +
                        "document.getElementById('cmb_channel').value = '" + getOptimalChannel() + "';" +
                        "document.getElementById('SaveButton').click();" +
                        "clearInterval(repeatInterval);" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "else {" +
                        "document.getElementById('cmb_freq').value == 5000;" +
                        "cmb_freq.onchange('5000');" +
                        "}" +
                        "}, 3500); " +
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
                        36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136
                },
                        {36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136},
                        {36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136},
                        {36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136}};
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