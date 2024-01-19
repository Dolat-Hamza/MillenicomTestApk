package com.example.millenicomtestapk.ap.tplink;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class TPLinkArcherC9 extends AccessPoint {

    public static final String TAG = TPLinkArcherC9.class.getSimpleName();
    private static final String DEFAULT_SUFFIX = "/cgi-bin/luci/;stok=";
    private static final String LOGIN_SUFFIX = "/login?form=login";
    private static final String SETTINGS_SUFFIX_2G = "/admin/wireless?form=wireless_2g";
    private static final String SETTINGS_SUFFIX_5G = "/admin/wireless?form=wireless_5g";
    private static final String DEFAULT_PASSWORD = "admin";

    public TPLinkArcherC9(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        Log.d(TAG, getFormattedGateway());

        if (!url.startsWith(getFormattedGateway() + "/webpages/login.html"))
            return "";

        String a = "javascript:{" +
                initAmbeent()+
                // Get RSA key
                "generateRequest('" + getFormattedGateway() + DEFAULT_SUFFIX + LOGIN_SUFFIX + "', 'operation=read&undefined=')" +
                ".then(function(result) {" +
                "console.log('Get RSA key result: ' + JSON.stringify(result, null, 2));" +
                // Login promise
                "return generateRequest('" + getFormattedGateway() + DEFAULT_SUFFIX + LOGIN_SUFFIX + "', " +
                "'operation=login&password=' + $.su.encrypt('" + getPassword() + "', result.data.password));" +
                "})" +
                ".then(function(result) {" +
                "console.log('Login result: ' + JSON.stringify(result, null, 2));" +
                // Change channel promise
                "return generateRequest('" + getFormattedGateway() + DEFAULT_SUFFIX + "' + result.data.stok + (" + getOptimalChannel() + " > 13 ? '" + SETTINGS_SUFFIX_5G + "' : '" + SETTINGS_SUFFIX_2G + "')," +
                "'operation=write&htmode=20&channel=" + getOptimalChannel() + "');" +
                "})" +
                // Success
                ".then(function(result) {" +
                "console.log('Success result: ' + JSON.stringify(result, null, 2));" +
                "setTimeout(function() {" +
                ambeent(String.valueOf(getOptimalChannel())) +
                "},2000);" +
                "})" +
                // Error
                initAmbeent()+
                ".catch(function(error) {" +
                "console.log('Error: ' + error);" +
                "setTimeout(function() {" +
                ambeent("failure") +
                "},2000);" +
                "});" +

                // Generate request promise function
                "function generateRequest(url, data) {" +
                "return new Promise(function(resolve, reject) {" +
                "var xhr = new XMLHttpRequest();" +
                "xhr.withCredentials = true;" +

                "xhr.addEventListener('readystatechange', function() {" +
                "if (this.readyState === 4) {" +
                "var response = JSON.parse(this.responseText);" +
                "console.log('Response: ' + JSON.stringify(response, null, 2));" +
                "if (response.success) return resolve(response);" +
                "else return reject();" +
                "}" +
                "});" +

                "xhr.open('POST', url);" +
                "xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');" +
                "xhr.setRequestHeader('cache-control', 'no-cache');" +
                "xhr.send(data);" +

                "});" +
                "}" +

                "}";
        return a;
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
                        36, 40, 44, 48
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
