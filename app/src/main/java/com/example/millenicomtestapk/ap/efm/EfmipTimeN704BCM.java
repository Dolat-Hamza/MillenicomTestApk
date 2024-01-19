package com.example.millenicomtestapk.ap.efm;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class EfmipTimeN704BCM extends AccessPoint {

    public static final String TAG = EfmipTimeN704BCM.class.getSimpleName();

    public static final String LOGIN_SUFFIX = "/sess-bin/login_session.cgi";
    public static final String MAIN_SUFFIX = "/sess-bin/login.cgi?";
    public static final String WRONG_SUFFIX = "/sess-bin/login_session.cgi?noauto=1";
    public static final String WIFI_SUFFIX = "/sess-bin/timepro.cgi?tmenu=wirelessconf&smenu=basicsetup";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    public EfmipTimeN704BCM(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);

        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "if(document.getElementsByName('iframe_captcha').length > 0) {" +
                    "}" +
                    "document.getElementsByName('username')[0].value= '" + getUsername() + "';" +
                    "document.getElementsByName('passwd')[0].value= '" + getPassword() + "';" +
                    "LoginProcess();" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + WIFI_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + WRONG_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";

        else if (url.equals(getFormattedGateway() + WIFI_SUFFIX)) {
            String channel_suffix = "";
            switch (getOptimalChannel()) {
                case 0:
                    channel_suffix = "";
                    break;
                case 1:
                    channel_suffix = ".3";
                    break;
                case 2:
                    channel_suffix = ".4";
                    break;
                case 3:
                    channel_suffix = ".5";
                    break;
                case 4:
                    channel_suffix = ".6";
                    break;
                case 5:
                    channel_suffix = ".7";
                    break;
                case 6:
                    channel_suffix = ".8";
                    break;
                case 7:
                    channel_suffix = ".9";
                    break;
                case 8:
                    channel_suffix = ".10";
                    break;
                case 9:
                    channel_suffix = ".11";
                    break;
                case 10:
                    channel_suffix = ".8";
                    break;
                case 11:
                    channel_suffix = ".9";
                    break;
                case 12:
                    channel_suffix = ".10";
                    break;
                case 13:
                    channel_suffix = ".11";
                    break;
            }
            return "javascript:{" +
                    initAmbeent()+
                    "var iframeDocument = document.getElementsByName('basicsetup_iframe')[0].contentDocument;" +
                    "iframeDocument.getElementsByName('channel')[0].value = '" + getOptimalChannel() + channel_suffix + "';" +
                    "submit_all_data();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
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
                        116,
                        120,
                        124,
                        128,
                        132,
                        136,
                        149,
                        153,
                        157,
                        161
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
