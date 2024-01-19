package com.example.millenicomtestapk.ap.efm;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class EfmipTimeA604M extends AccessPoint {

    public static final String TAG = EfmipTimeA604M.class.getSimpleName();
    public static final String LOGIN_SUFFIX = "/m_login.cgi";
    public static final String MAIN_SUFFIX = "/sysconf/info/iux.cgi";
    public static final String WRONG_SUFFIX = "/m_login.cgi?noauto=1";
    public static final String WIFI_SUFFIX = "/sess-bin/timepro.cgi?tmenu=wirelessconf&smenu=basicsetup";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";


    public EfmipTimeA604M(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
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
                        36,40,149,153,157,161
                },
                        {36, 44, 52, 60, 100, 108, 116, 124, 132, 140, 149, 157},
                        {36, 52, 100, 116, 132, 149},
                        {36, 100}};


                if (scanResultChannelWith > 3) return false;
                List<Integer> list = new ArrayList<>();
                for (int a : channelList[scanResultChannelWith]) list.add(a);
                return list.contains(channel);
            }
            default:
                return false;
        }
    }


    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);

        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "if(document.getElementById('captcha_div').style.display == '') {" +
                    "} else {" +
                    "document.getElementById('loginid').value = '" + getUsername() + "';" +
                    "document.getElementById('loginpw').value = '" + getPassword() + "';" +
                    "document.getElementById('submit_button').click();" +
                    "};" +
                    "};";

        else if (url.equals(getFormattedGateway() + WRONG_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + WIFI_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + WIFI_SUFFIX)) {

            if (getOptimalChannel() <= 13) {

                return "javascript:{" +
                        initAmbeent()+
                        "var ghzframeDocument = document.getElementsByName('multibssid_iframe')[0].contentDocument;" +
                        "ghzframeDocument.getElementsByClassName('wl_mbssid_td')[6].click();" +
                        "var extendDocument = document.getElementsByName('extendsetup_iframe')[0].contentDocument;" +
                        "var repeatInterval = setInterval(function(){" +
                        "if (extendDocument.getElementsByName('wirelessmode')[0].value == 5){" +
                        "var iframeDocument = document.getElementsByName('basicsetup_iframe')[0].contentDocument;" +
                        "iframeDocument.getElementsByName('channel')[0].value  = '" + getOptimalChannel() + "." + getOptimalChannel() + "';" +
                        "clearInterval(repeatInterval);" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "submit_all_data();" +
                        "}" +
                        "else {" +
                        "extendDocument.getElementsByName('wirelessmode')[0].value = 5;" +
                        "submit_all_data();" +
                        "}" +
                        "}, 3000);" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent()+
                        "var ghzframeDocument = document.getElementsByName('multibssid_iframe')[0].contentDocument;" +
                        "ghzframeDocument.getElementsByClassName('wl_mbssid_td')[0].click();" +
                        "var iframeDocument = document.getElementsByName('basicsetup_iframe')[0].contentDocument;" +
                        // 36, 40, 149, 153, 157, 161
                        "iframeDocument.getElementsByName('channel')[0].value  = '" + getOptimalChannel() + "." + getOptimalChannel() + "';" +
                        "submit_all_data();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "};";
            }
        }
        return null;
    }
}
