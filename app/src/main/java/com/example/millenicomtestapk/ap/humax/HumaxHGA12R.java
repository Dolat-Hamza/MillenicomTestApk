package com.example.millenicomtestapk.ap.humax;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

//import timber.log.Timber;


public class HumaxHGA12R extends AccessPoint {

    //private static final String TAG = net.ambeent.app.android.ap.humax.HumaxHGA12R.class.getSimpleName();
    private static final String TAG = com.example.millenicomtestapk.ap.humax.HumaxHGA12R.class.getSimpleName();
    private static final String DEFAULT_USERNAME = "NET_64416E";
    private static final String DEFAULT_PASSWORD = "942CB364416E";


    private State state = State.LOGIN_STATE;

    public HumaxHGA12R(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, "State NET_64416E: " + state);
        Log.d(TAG, url);
        if (state == State.LOGIN_STATE) {
            //Timber.e("çalıştı" + getUsername());
            state = State.CREDENTIAL_CHECK_STATE;
            String str = "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function () {" +
                    "document.getElementById('login').value = '" + getUsername() + "';" +
                    "document.getElementById('senha').value = '" + getPassword() + "';" +
                    "document.getElementsByClassName('holder-icon')[0].click();" +
                    "setTimeout(function () {" +
                    "if (document.getElementsByClassName('f-row')[0].textContent == 'Login incorreto'){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "}, 4000); " +
                    "}, 3000); " +
                    "}";
            return str;
        } else {
            if (getOptimalChannel() < 14) {
                if (state == State.CREDENTIAL_CHECK_STATE) {
                    state = State.CHANNEL_CHANGE_STATE;
                    //Timber.e("KANAL DURUMU 2.4G = " + getOptimalChannel());
                    String a = "javascript:{" +
                            initAmbeent()+
                            "setTimeout(function () {" +
                            "window.scrollTo(0,0);" +
                            "document.getElementsByClassName('next config')[0].click();" +

                            "setTimeout(function () {" +
                            "document.getElementsByClassName('main-menu-button')[0].click();" +
                            "document.getElementById('menu-wi-fi').click();" +
                            "document.getElementsByClassName('item-menu')[32].children[0].click();" +

                            "setTimeout(function () {" +
                            "document.getElementById('channel').value = " + getOptimalChannel() + ";" +
                            "location = location;" +
                            //"document.getElementById('channel').selectedIndex= " + getOptimalChannel() + ";" +
                            "setTimeout(function () {" +
                            "document.getElementsByClassName('button icon')[0].click();" +
                            "setTimeout(function() {" +
                            ambeent(String.valueOf(getOptimalChannel())) +
                            "},2000);" +
                            "}, 3000); " +
                            "}, 3000); " +
                            "}, 3000); " +
                            "}, 3000); " +
                            "}";
                    return a;
                } else {
                    return null;
                }
            } else {
                if (state == State.CREDENTIAL_CHECK_STATE) {
                    state = State.CHANNEL_CHANGE_STATE;
                    //Timber.e("KANAL DURUMU 5G = " + getOptimalChannel());
                    String kullan = "javascript:{" +
                            initAmbeent()+
                            "setTimeout(function () {" +
                            "window.scrollTo(0,0);" +
                            "document.getElementsByClassName('next config')[0].click();" +
                            "setTimeout(function () {" +
                            "document.getElementsByClassName('main-menu-button')[0].click();" +
                            "document.getElementById('menu-wi-fi').click();" +
                            "document.getElementsByClassName('item-menu')[32].children[0].click();" +
                            "setTimeout(function () {" +
                            "document.getElementById('radio5g').click();" +
                            "setTimeout(function () {" +
                            //"document.getElementById('channel').value = " + getOptimalChannel() + ";" +
                            //"document.getElementById('channel').selectedIndex= " + getOptimalChannel() + ";" +
                            "var wcg = document.getElementById('channel');" +
                            "wcg.value =  '" + getOptimalChannel() + "';" +
                            "var eventt = new Event('change');" +
                            "wcg.dispatchEvent(eventt);" +
                            //"location = location;" +
                            "setTimeout(function () {" +
                            "document.getElementsByClassName('button icon')[0].click();" +
                            "setTimeout(function() {" +
                            ambeent(String.valueOf(getOptimalChannel())) +
                            "},2000);" +
                            "}, 3000); " +
                            "}, 4000); " +
                            "}, 3000); " +
                            "}, 3000); " +
                            "}, 3000); " +
                            "}";
                    return kullan;
                } else {
                    return null;
                }
                //ELSE BİTTİ
            }
        }
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
                        149,
                        153,
                        157,
                        161
                },
                        {36, 40, 44, 48, 149, 153, 157, 161},
                        {36, 40, 44, 48, 149, 153, 157, 161},
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

//document.getElementById("radio5g").click();