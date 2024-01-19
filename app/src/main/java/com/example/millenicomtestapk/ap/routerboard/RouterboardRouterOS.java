package com.example.millenicomtestapk.ap.routerboard;

import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;


public class RouterboardRouterOS extends AccessPoint {

    public static final String TAG = RouterboardRouterOS.class.getSimpleName();
    private static final String SETTINGS_SUFFIX = "/webfig/#Wireless.Interfaces.5";
    private State state = State.LOGIN_STATE;

    public RouterboardRouterOS(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (state == State.LOGIN_STATE) {
            state = State.SETTINGS_STATE;
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX)) {
            String channel_suffix = "";
            switch (getOptimalChannel()) {
                case 1:
                    channel_suffix = "2412";
                    break;
                case 2:
                    channel_suffix = "2417";
                    break;
                case 3:
                    channel_suffix = "2422";
                    break;
                case 4:
                    channel_suffix = "2427";
                    break;
                case 5:
                    channel_suffix = "2432";
                    break;
                case 6:
                    channel_suffix = "2437";
                    break;
                case 7:
                    channel_suffix = "2442";
                    break;
                case 8:
                    channel_suffix = "2447";
                    break;
                case 9:
                    channel_suffix = "2452";
                    break;
                case 10:
                    channel_suffix = "2457";
                    break;
                case 11:
                    channel_suffix = "2462";
                    break;
                case 12:
                    channel_suffix = "2467";
                    break;
                case 13:
                    channel_suffix = "2472";
                    break;
            }
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval2 = setInterval(function(){" +
                    "document.getElementsByClassName('value')[13].children[0].children[0].nextSibling.value = " + channel_suffix + ";" +
                    "if(document.getElementsByClassName('value')[13].children[0].children[0].nextSibling.value == " + channel_suffix + ") {" +
                    "clearInterval(repeatInterval2);" +
                    "document.getElementsByClassName('button')[5].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 10000);" +
                    "};";
        }
        return null;
    }

    enum State {
        LOGIN_STATE,
        SETTINGS_STATE
    }
}