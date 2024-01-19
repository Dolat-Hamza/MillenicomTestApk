package com.example.millenicomtestapk.ap.samsung;

import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class SamsungSWW_3100BG extends AccessPoint {

    public static final String TAG = SamsungSWW_3100BG.class.getSimpleName();
    public static final String LOGIN_SUFFIX = "/cgi-bin/command.cgi?m1=viewinfo";
    public static final String SETTINGS_SUFFIX = "/cgi-bin/command.cgi?m1=wireless_setup&m2=";

    public SamsungSWW_3100BG(String gateway, int callbackPort, String username, String password) {
        super(gateway, callbackPort);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";

        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX)) {
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
                    "document.getElementsByName('channel')[0].value = '" + getOptimalChannel() + channel_suffix + "';" +
                    "document.getElementsByName('apply_bt')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "};";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE
    }

}
