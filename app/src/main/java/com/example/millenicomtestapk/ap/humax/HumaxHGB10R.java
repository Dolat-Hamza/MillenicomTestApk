package com.example.millenicomtestapk.ap.humax;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HumaxHGB10R extends AccessPoint {

    private static final String TAG = HumaxHGB10R.class.getSimpleName();

    public static final String LOGIN_SUFFIX = "/";
    public static final String MAIN_SUFFIX = "/wlswitchinterface0.wl";
    public static final String WIFI24_SUFFIX = "/wlcfgadv.html";
    // public static final String WIFI50_SUFFIX = "/wlcfgadv1.html";

    public static final String DEFAULT_USERNAME = "NET_EEAC14";
    public static final String DEFAULT_PASSWORD = "942CB3EEAC14";

    private State state = State.LOGIN_STATE;

    public HumaxHGB10R(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @NotNull
    @Override
    public String forPopUp() {
        return "";
    }

    //return "/wlcfgadv.html";
    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, "State on HUMAXHGBR10R : " + state);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            if (state == State.LOGIN_STATE) {
                state = State.OPTIMIZE_STATE;
                return "javascript:{" +
                        "setTimeout(() => { " +
                        "document.location.href= '" + getFormattedGateway() + WIFI24_SUFFIX + "';" +
                        "}, 1000);" +
                        "};";

            } else if (state == State.OPTIMIZE_STATE) {
                state = State.LOGIN_STATE;
                return "javascript:{" +
                        initAmbeent()+
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "};";
            }
        } else if (url.equals(getFormattedGateway() + WIFI24_SUFFIX)) {
            if (state == State.OPTIMIZE_STATE || state == State.OPTIMIZATION_SUCCESS_STATE) {
                state = State.OPTIMIZATION_SUCCESS_STATE;
                String a = "javascript:{" +
                        initAmbeent()+
                        //2.4
                        "setTimeout(() => { " +
                        "var define = document.getElementsByName('wlChannel')[0];" +
                        "define.value = '" + getOptimalChannel() + "';" +
                        "window.wl_recalc(false);" +
                        "setTimeout(function () {" +
                        "window.btnApply();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        " }, 3000); }, 2000);" +
                        "};";
                return a;
            }
        }

        return null;
    }

    @Override
    public boolean channelIsValid(int channelType, int scanResultChannelWith, int channel) {
        switch (channelType) {
            case 0: {
                int[][] channelList = {{1, 2, 3, 4, 5, 6, 7, 8, 9},
                        {1, 2, 3, 4, 5, 6, 7, 8, 9}};
                if (scanResultChannelWith > 1) return false;
                List<Integer> list2 = new ArrayList<>();
                for (int a : channelList[scanResultChannelWith]) list2.add(a);
                return list2.contains(channel);
            }
            case 1: {
                int[][] channelList = {{
                        36, 40, 44, 48, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161, 165
                },
                        {36, 44, 52, 60, 100, 108, 116, 124, 132, 140, 149, 157},
                        {36, 40, 44, 52, 56, 60, 64, 100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144, 149, 153, 157, 161},
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

    enum State {
        LOGIN_STATE,
        OPTIMIZE_STATE,
        CREDENTIAL_CHECK_STATE,
        LOGIN_AGAIN_STATE,
        OPTIMIZATION_SUCCESS_STATE
    }
}