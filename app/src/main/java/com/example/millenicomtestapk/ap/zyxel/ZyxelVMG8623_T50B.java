package com.example.millenicomtestapk.ap.zyxel;

import static com.example.millenicomtestapk.ap.zyxel.ZyxelVMG8623_T50B.State.LOGIN_STATE;
import static com.example.millenicomtestapk.ap.zyxel.ZyxelVMG8623_T50B.State.*;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;


public class ZyxelVMG8623_T50B extends AccessPoint {

    private static final String TAG = ZyxelVMG8623_T50B.class.getSimpleName();



    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "TKT3TF8R";

    private static final String LOGIN_SUFFIX = "/login";
    private static final String NETWORK_SUFFIX = "/Wireless";

    private State state = LOGIN_STATE;

    public ZyxelVMG8623_T50B(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, "State on T50B : " + state);
        if (url.equals(getFormattedGateway()+"/") )
            return "Javascript:{" +
                    "document.location.href = '" + getFormattedGateway() + LOGIN_SUFFIX + "'" +
                    "}";
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)){
            if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE;
                String str= "javascript:{" +
                        initAmbeent()+
                        "setTimeout(function () {" +
                        getSendCode() +
                        "amb_sendcode(document.getElementById('username'),'" + getUsername() + "').then(" +
                        "function(){ amb_sendcode(document.getElementById('userpassword'),'" + getPassword() + "');}).then(" +
                        "function(){setTimeout(function(){document.getElementById('loginBtn').click();setTimeout(function(){location = location},2000);},2000); " +
                            "setTimeout(function(){" +
                                "if (document.getElementById('zypswd')?.innerHTML.length > 0 || document.getElementsByClassName('memo')[0]?.innerText.length>0){" +
                                    "setTimeout(function() {" +
                                     ambeent("failure") +
                                     "},2000);" +
                                "} else { document.location.href='" + getFormattedGateway() + NETWORK_SUFFIX + "';}" +
                            "},3000); " +
                        "});" +
                        "},5000);"  +
                        "}";
                return str;
            } else if (state == State.CREDENTIAL_CHECK_STATE) { // password is wrong
                state = State.LOGIN_STATE;
                return  "javascript:{" +
                        initAmbeent()+
                        "if (document.getElementById('zypswd').innerHTML.length > 0){ " +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "}";
            }
            else return null;
        }
        else if (url.equals(getFormattedGateway() + NETWORK_SUFFIX)){
            if (state == State.CREDENTIAL_CHECK_STATE) {
                String channelValue="0";
                String bandwithValue = "40";
                if (getOptimalChannel()>=15) {
                    channelValue = "4";
                    bandwithValue = "80";
                }
                String retVal= "javascript:{" +
                        initAmbeent()+
                        "setTimeout(function(){" +
                        "var wrg = document.getElementById('wifi_radio_general');" +
                        "wrg.value = '" + channelValue + "';" +
                        "var event = new Event('change');" +
                        "wrg.dispatchEvent(event);" +
                        "setTimeout(function(){" +
                        "var wcg = document.getElementById('wifi_channel_general');" +
                        "wcg.value = '" + getOptimalChannel()  +"';" +
                        "var eventt = new Event('change');" +
                        "wcg.dispatchEvent(eventt);" +
                        "setTimeout(function(){" +
                        "var dcc = document.getElementById('app').__vue__.$children[7].$children[1];"+
                        "setTimeout(function(){" +
                        "dcc.doWifiApply(dcc);" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "setTimeout(function(){" +
                        "document.getElementById('navbar_logout').click();" +
                        "setTimeout(function(){" +
                        "document.getElementsByClassName('normalbtn active')[2].click();" +
                        "},1000);" +
                        "},1000);" +
                        "},1000);" +
                        "},2000);" +
                        "},1000);" +
                        "}, 3000);" +
                        "}";
                return retVal;

            }
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
                        36,40,44,48,52,56,60,64,100,104,108,112,116,132,136,140},
                        {36,44,52,60,100,108,116,132},
                        {36,40,44,48,52,56,60,64,100,104,108,112,116},
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
        CREDENTIAL_CHECK_STATE,
        CHANNEL_CHANGE_STATE
    }
}