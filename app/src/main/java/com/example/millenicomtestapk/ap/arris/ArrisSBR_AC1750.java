package com.example.millenicomtestapk.ap.arris;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;


public class ArrisSBR_AC1750 extends AccessPoint {


    public static final String TAG = ArrisSBR_AC1750.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/home.html";
    private static final String MAIN_SUFFIX = "/setup.cgi";
    private static final String SETTINGS_SUFFIX = "/wls_chan.html";

    private static final String DEFAULT_PASSWORD = "password";

    public ArrisSBR_AC1750(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            return "javascript:{" +
                    "if (document.getElementById('uberAwesomeMenu')=== null) {" +
                    "document.getElementsByName('pws')[0].value = '" + getPassword() + "';" +
                    "document.getElementsByName('submit')[0].click();" +
                    "} else { " +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "}" +
                    "};";

        } else if (url.equals(getFormattedGateway() + MAIN_SUFFIX)) {
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementById('invalid_password')!== null) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else { " +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "}" +
                    "};";

        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX)) {
            return "javascript:{" +
                    initAmbeent()+
                    ((getOptimalChannel() <= 11) ?
                            "document.getElementsByName('wl_channel')[0].selectedIndex = " + getOptimalChannel() + ";" :
                            "document.getElementsByName('wl_channel_5g')[0].value = " + getOptimalChannel() + ";") +
                    "document.getElementsByName('submit')[0].click();" +
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
                        44,
                        149,
                        157
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
