package com.example.millenicomtestapk.ap.zyxel;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class ZyxelNBG6604 extends AccessPoint {


    private static final String TAG = ZyxelNBG6604.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/cgi-bin/luci";
    private static final String ERROR_SUFFIX = "/cgi-bin/luci?login_error=1";
    private static final String CREDENTIAL_CHECK_SUFFIX = "/cgi-bin/luci/easy/passWarning";
    private static final String OPTIONS_SUFFIX = "/easy/eaZy123";
    private static final String MAIN_SUFFIX = "/expert/frame_expert";

    private static final String DEFAULT_PASSWORD = "password";


    public ZyxelNBG6604(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "document.getElementById('password').value = '" + getPassword() + "';" +
                    "submit_data();" +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + CREDENTIAL_CHECK_SUFFIX))
            return "javascript:{" +
                    "document.getElementById('NEWpassword').value = 'password';" +
                    "document.getElementById('CONFIRMpassword').value = 'password';" +
                    "document.getElementById('apply').click();" +
                    "};";
        else if (url.contains(OPTIONS_SUFFIX))
            return "javascript:{" +
                    "document.querySelectorAll('input')[1].click();" +
                    "};";
        else if (url.contains(MAIN_SUFFIX))
            if (getOptimalChannel() < 14) {
                return "javascript:{" +
                        initAmbeent() +
                        "parent.frames['bottom'].document.getElementById('bar_3').click();" +
                        "var repeatInterval = setInterval(function () {" +
                        "var Frame = window.frames['mainFrame'].window.frames['data'];" +
                        "if (Frame.document.getElementsByName('band')[0].value != 24) {" +
                        "Frame.document.getElementsByName('band')[0].value = 24;" +
                        "Frame.document.getElementsByName('band')[0].onchange();" +
                        "} else {" +
                        "Frame.document.getElementById('Channel_ID').value ='" + getOptimalChannel() + "';" +
                        "Frame.document.getElementById('Channel_ID').onchange();" +
                        "Frame.document.getElementsByName('sysSubmit')[0].click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "}, 4000);" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent() +
                        "parent.frames['bottom'].document.getElementById('bar_3').click();" +
                        "var repeatInterval = setInterval(function () {" +
                        "var Frame = window.frames['mainFrame'].window.frames['data'];" +
                        "if (Frame.document.getElementsByName('band')[0].value != 5) {" +
                        "Frame.document.getElementsByName('band')[0].value = 5;" +
                        "Frame.document.getElementsByName('band')[0].onchange();" +
                        "} else {" +
                        "Frame.document.getElementById('Channel_ID').value ='" + getOptimalChannel() + "';" +
                        "Frame.document.getElementById('Channel_ID').onchange();" +
                        "Frame.document.getElementsByName('sysSubmit')[0].click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}" +
                        "}, 4000);" +
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
                        140,
                },
                        {},
                        {},
                        {36,40,44,52,60,64,100,104,108,112,116,120,124,128,132,136,140,144,149,153,157,161}};
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
