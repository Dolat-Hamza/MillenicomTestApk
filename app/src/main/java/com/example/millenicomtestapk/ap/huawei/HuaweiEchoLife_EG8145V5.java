package com.example.millenicomtestapk.ap.huawei;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class HuaweiEchoLife_EG8145V5 extends AccessPoint {

    public static final String TAG = HuaweiEchoLife_EG8145V5.class.getSimpleName();
    public static final String LOGIN_SUFFIX = "/";
    public static final String MAIN_SUFFIX = "/index.asp";
    public static final String SETUP_SUFFIX = "/CustomApp/userguideframe.asp";
    public static final String DEFAULT_USERNAME = "root";
    public static final String DEFAULT_PASSWORD = "adminHW";
    private State state = State.LOGIN_STATE;

    public HuaweiEchoLife_EG8145V5(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE;
                return "javascript:{" +
                        getSendCode() +
                        "amb_sendcode(document.getElementById('txt_Username'), '" + getUsername() + "').then(function(){" +
                        "amb_sendcode(document.getElementById('txt_Password'), '" + getPassword() + "');}).then(function(){" +
                        "setTimeout(function(){" +
                        "document.getElementById('loginbutton').click();" +
                        "},2000);" +
                        "})" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent()+
                        "setTimeout(function() {" +
                        "if(document.getElementById('DivErrPromt').innerHTML.length > 50){" +
                        ambeent("failure") +
                        "}" +
                        "},2000);" +
                        "};";
            }

        } else if(url.equals(getFormattedGateway() + SETUP_SUFFIX)){
            return "javascript:{" +
                    "setTimeout(() => {" +
                    "var useItem =  document.getElementById('frameContent').contentDocument;" +
                    "useItem.getElementById('guideinternet').click();" +
                    "}, 2500);" +
                    "};";
        }else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            if (getOptimalChannel() > 13) {

                return "javascript:{" +
                        initAmbeent() +
                        "var Interval = setInterval(() => {" +
                        "document.getElementById('name_addconfig').click();" +
                        "document.getElementById('name_wlanconfig').click();" +
                        "document.getElementById('wlan5adv').click();" +
                        "var menuFrame = document.getElementById('menuIframe').contentDocument;" +
                        "var valueItem = menuFrame.getElementById('Channel');" +
                        "valueItem.value = '" + getOptimalChannel() + "';" +
                        "var fnct = new Event('change');" +
                        "valueItem.dispatchEvent(fnct);" +
                        "menuFrame.getElementById('applyButton').click();" +
                        "if (menuFrame.getElementById('applyButton').click() == undefined) {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "clearInterval(Interval);" +
                        "}" +
                        "}, 1800);" +
                        "};";
            }else{
                return "javascript:{" +
                        initAmbeent() +
                        "var Interval = setInterval(() => {" +
                        "document.getElementById('name_addconfig').click();" +
                        "document.getElementById('name_wlanconfig').click();" +
                        "document.getElementById('wlan2adv').click();" +
                        "var menuFrame = document.getElementById('menuIframe').contentDocument;" +
                        "var valueItem = menuFrame.getElementById('Channel');" +
                        "valueItem.value = '" + getOptimalChannel() + "';" +
                        "var fnct = new Event('change');" +
                        "valueItem.dispatchEvent(fnct);" +
                        "menuFrame.getElementById('applyButton').click();" +
                        "if (menuFrame.getElementById('applyButton').click() == undefined) {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "clearInterval(Interval);" +
                        "}" +
                        "}, 1800);" +
                        "};";
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
                        36, 40, 44, 48, 52, 53, 60, 64, 149, 153, 157, 161, 165
                },
                        {36, 40, 44, 48, 52, 53, 60, 64, 149, 153, 157, 161, },
                        { 36, 40, 44, 48, 52, 53, 60, 64, 149, 153, 157, 161 },
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
        CREDENTIAL_CHECK_STATE
    }
}
