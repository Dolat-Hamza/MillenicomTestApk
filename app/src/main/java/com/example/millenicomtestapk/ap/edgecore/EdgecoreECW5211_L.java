package com.example.millenicomtestapk.ap.edgecore;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;


public class EdgecoreECW5211_L extends AccessPoint {

    private static final String TAG = EdgecoreECW5211_L.class.getSimpleName();

    public static final String LOGIN_SUFFIX = "/cgi-bin/acn";


    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private State state = State.LOGIN_STATE;


    public EdgecoreECW5211_L(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            state = State.MAIN_STATE;
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function(){" +
                    "document.getElementsByName('username')[0].defaultValue = '" + getUsername() + "';" +
                    "document.getElementsByName('password')[0].defaultValue = '" + getPassword() + "';" +
                    "var used = document.getElementById('err_msg');" +
                    "if(getComputedStyle(used).display == 'block'){" +
                    ambeent("failure") +
                    "}" +
                    "else{" +
                    "document.getElementById('Apply').click();" +
                    "}" +
                    "}, 4000);" +
                    "};";
        } else if (state == State.MAIN_STATE) {
            state = State.GET_READY_CHANGE_CHANNEL;
            return "javascript:{" +
                    "setTimeout(function () {" +
                    "console.log('asdasdasd');" +
                    "location.reload();" +
                    "}, 4000);" +
                    "};";
        } else if (state == State.GET_READY_CHANGE_CHANNEL) {
            state = State.CHANNEL_CHANGE_STATE;
            if (getOptimalChannel() <= 13) {
                return "javascript:{" +
                        "document.getElementById('main_menu').querySelectorAll('li')[10].querySelector('li').querySelector('a').click();" +
                        "};";
            } else {
                return "javascript:{" +
                        "document.getElementById('main_menu').querySelectorAll('li')[10].querySelector('ul').childNodes[3].querySelector('a').click();" +
                        "};";
            }
        } else if (state == State.CHANNEL_CHANGE_STATE) {
            state = State.SAVE_CHANGE;

            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(() => {" +
                    "var repeatInterval = setInterval(function(){" +

                    "if(document.getElementById('select_all').checked == false){" +
                    "document.getElementById('select_all').click();" +
                    "document.getElementById('select_all').click();" +

                    "}else {" +
                    "document.getElementById('select_all').click();" +
                    "}" +
                    "setTimeout(() => {" +
                    "console.log(" + getOptimalChannel() + ");" +
                    "if(" + getOptimalChannel() + " == 0){" +
                    "document.getElementById('select_all').click();" +
                    "}" +
                    "else{" +
                    "document.getElementById('cbi-wifi_channel-" + getOptimalChannel() + "-checkbox').click();" +
                    "}" +
                    " document.getElementById('btn_wifi_frequency').click();" +
                    "document.getElementById('btn_save').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 2000);" +
                    "}, 2000);" +
                    "}, 10000);" +
                    "};";
        } else if (state == State.SAVE_CHANGE) {
            return "javascript:{" +
                    initAmbeent() +
                    "document.getElementById('btn_chgs_apply').click();" +
                    "setTimeout(function() {" +
                    "document.getElementById('mgmt_btn').click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 2000);" +
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
                        36,40,44,48,149,153,157,161},
                        {36,40,44,48,149,513,157,161},
                        {36,40,44,48,149,153,157,161},
                        {36,40,44,48,149,153,157,161}};
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
        CHANNEL_CHANGE_STATE,
        PASSWORD_CHANGE_STATE,
        MAIN_STATE,
        GET_READY_CHANGE_CHANNEL,
        CHECK_FORM,
        SAVE_CHANGE
    }
}
