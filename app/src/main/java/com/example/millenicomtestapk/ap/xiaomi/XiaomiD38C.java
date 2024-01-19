package com.example.millenicomtestapk.ap.xiaomi;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class XiaomiD38C extends AccessPoint {

    public static final String TAG = XiaomiD38C.class.getSimpleName();

    public static final String LOGIN_SUFFIX = "/cgi-bin/luci/web";

    private static final String DEFAULT_PASSWORD = "00000000";


    public XiaomiD38C(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);

    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);

        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            return "javascript:{" +
                    initAmbeent() +
                    "setTimeout(function(){" +
                    "document.getElementById('password').value = '" + getPassword() + "';" +
                    "document.getElementById('btnRtSubmit').click();" +
                    "}, 4000);" +
                    "setTimeout(function(){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}, 8000);" +
                    "};";
        } else if (url.endsWith("/home#router")) {
            return "javascript:{" +
                    "setTimeout(function(){" +
                    "window.location.href = window.location.href.slice(0,-12) + '/setting/wifi';" +
                    "}, 1000);" +
                    "};";
        } else if (url.endsWith("/setting/wifi")) {

            if (getOptimalChannel() > 13) {
                String channel_suffix = "";
                switch (getOptimalChannel()) {
                    case 36:
                        channel_suffix = "1";
                        break;
                    case 40:
                        channel_suffix = "2";
                        break;
                    case 44:
                        channel_suffix = "3";
                        break;
                    case 48:
                        channel_suffix = "4";
                        break;
                    case 149:
                        channel_suffix = "5";
                        break;
                    case 153:
                        channel_suffix = "6";
                        break;
                    case 157:
                        channel_suffix = "7";
                        break;
                    case 161:
                        channel_suffix = "8";
                        break;
                }
                return "javascript:{" +
                        initAmbeent() +
                        "var repeatInterval1 = setInterval(function(){" +
                        "if(document.getElementsByClassName('ipt-text dummy')[5] != null) {" +
                        "clearInterval(repeatInterval1);" +
                        "document.getElementsByClassName('ipt-text dummy')[5].click()" +
                        "}" +
                        "console.log('console 1'); " +
                        "}, 1000);" +
                        "var repeatInterval2 = setInterval(function(){" +
                        "if(document.getElementsByClassName('nowrap')['" + channel_suffix + "'] != null) {" +
                        "clearInterval(repeatInterval2);" +
                        "document.getElementsByClassName('nowrap')['" + channel_suffix + "'].click();" +
                        "}" +
                        "console.log('console 2'); " +
                        "}, 1000);" +
                        "var repeatInterval3 = setInterval(function(){" +
                        "if(document.getElementsByClassName('btn btn-primary btn-l')[1] != null) {" +
                        "clearInterval(repeatInterval3);" +
                        "document.getElementsByClassName('btn btn-primary btn-l')[1].click();" +
                        "}" +
                        "console.log('console 3'); " +
                        "}, 1000);" +
                        "setTimeout(function() { " +
                        "document.getElementsByClassName('btn btn-primary')[0].click();" +
                        "console.log('console 4'); " +
                        "}, 8000);" +
                        "setTimeout(function() { " +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "console.log('console 5'); " +
                        "}, 10000);" +
                        "};";

            } else {
                return "javascript:{" +
                        initAmbeent() +
                        "var repeatInterval1 = setInterval(function(){" +
                        "if(document.getElementsByClassName('ipt-text dummy')[1] != null) {" +
                        "clearInterval(repeatInterval1);" +
                        "document.getElementsByClassName('ipt-text dummy')[1].click();" +
                        "}" +
                        "console.log('console 1'); " +
                        "}, 1000);" +

                        "var repeatInterval2 = setInterval(function(){" +
                        "if(document.getElementsByClassName('nowrap')['" + getOptimalChannel() + "'] != null) {" +
                        "clearInterval(repeatInterval2);" +
                        "document.getElementsByClassName('nowrap')['" + getOptimalChannel() + "'].click();" +
                        "document.getElementsByClassName('ipt-text dummy')[2].click();" +
                        "document.getElementsByClassName('nowrap')['1'].click()" +
                        "}" +
                        "console.log('console 2'); " +
                        "}, 1000);" +


                        "var repeatInterval3 = setInterval(function(){" +
                        "if(document.getElementsByClassName('btn btn-primary btn-l')[0] != null) {" +
                        "clearInterval(repeatInterval3);" +
                        "document.getElementsByClassName('btn btn-primary btn-l')[0].click();" +
                        "}" +
                        "console.log('console 3'); " +
                        "}, 1000);" +


                        "setTimeout(function() { " +
                        "document.getElementsByClassName('btn btn-primary')[0].click();" +
                        "console.log('console 4'); " +
                        "}, 8000);" +

                        "setTimeout(function() { " +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "console.log('console 5'); " +
                        "}, 10000);" +
                        "};";

            }
        }

        return null;
    }

}
