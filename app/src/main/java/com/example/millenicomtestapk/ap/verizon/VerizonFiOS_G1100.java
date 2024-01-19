package com.example.millenicomtestapk.ap.verizon;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerizonFiOS_G1100 extends AccessPoint {
    public static final String TAG = VerizonFiOS_G1100.class.getSimpleName();


    private static final String LOGIN_SUFFIX = "/#/login";
    private static final String MAIN_SUFFIX = "/#/main";
    private static final String SETUP_SUFFIX = "/#/setup";
    private static final String SETTINGS_SUFFIX = "/#/wireless/basic";

    private static final String DEFAULT_PASSWORD = "password";

    private boolean setupAttempted = false;
    private boolean loginAttempted = false;
    private boolean loginSuccessful = false;


    public VerizonFiOS_G1100(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (!setupAttempted && url.equals(getFormattedGateway() + SETUP_SUFFIX)) {
            setupAttempted = true;
            return "javascript:{" +
                    "var repeatInterval = setInterval(function() {" +
                    "if (angular.element(document.getElementById('step1InputPasswordMask')).scope() !== undefined) {" +
                    "clearInterval(repeatInterval);" +
                    "angular.element(document.getElementById('step1InputPasswordMask')).scope().password = '" + getPassword() + "';" +
                    "document.getElementById('step1BtnNext').click();" +
                    "setTimeout(function() { document.getElementById('step2BtnNext').click(); }, 500);" +
                    "setTimeout(function() { document.getElementById('step3BtnNext').click(); }, 1000);" +
                    "setTimeout(function() { window.location.href = '" + getFormattedGateway() + "' }, 1500);" +
                    "}" +
                    "}, 2000);" +
                    "};";
        } else if (setupAttempted && url.equals(getFormattedGateway() + SETUP_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (!setupAttempted && !loginAttempted && url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            loginAttempted = true;
            return "javascript:{" +
                    "var repeatInterval = setInterval(function() {" +
                    "if (angular.element(document.getElementById('login-table')).scope() !== undefined) {" +
                    "clearInterval(repeatInterval);" +
                    "angular.element(document.getElementById('login-table')).scope().password = '" + getPassword() + "';" +
                    "angular.element(document.getElementById('login-table')).scope().login();" +
                    "setTimeout(function() { window.location.href = '" + getFormattedGateway() + "' }, 500);" +
                    "}" +
                    "}, 1500);" +
                    "};";
        } else if (!setupAttempted && !loginSuccessful && loginAttempted && url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() { " +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    " }, 4000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX)) {
            loginSuccessful = true;
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "var repeatInterval = setInterval(function() {" +
                    "if (angular.element(document.getElementsByClassName('colInput')[0]).scope().wirelessA !== undefined) {" +
                    "clearInterval(repeatInterval);" +
                    ((getOptimalChannel() <= 13) ?
                            "angular.element(document.getElementsByClassName('colInput')[0]).scope().wirelessA.channelDesired = " + getOptimalChannel() + ";" :
                            "angular.element(document.getElementsByClassName('colInput')[0]).scope().wirelessB.channelDesired = " + getOptimalChannel() + ";") +
                    "angular.element(document.getElementsByClassName('colInput')[0]).scope().save();" +
                    "setTimeout(function() { document.getElementsByClassName('modal-footer-setup floatMeLeft')[0].children[0].click(); }, 1000);" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}" +
                    "}, 1500);" +
                    "};";
        return null;
    }

    @Override
    public boolean channelIsValid(int channelType, int scanResultChannelWith, int channel) {
        switch (channelType) {
            case 0: {
                int[][] channelList = {{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13},
                        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}};
                if (scanResultChannelWith > 1) return false;
                return Arrays.asList(channelList[scanResultChannelWith]).contains(channel);
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
                        161,
                        165
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