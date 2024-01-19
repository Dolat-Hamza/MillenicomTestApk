package com.example.millenicomtestapk.ap.twowire;

import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TwoWire5031NV_030 extends AccessPoint {
    public static final String TAG = TwoWire5031NV_030.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/xslt?PAGE=C_2_1_POST&NEXTPAGE=C_2_1_POST";
    private static final String MAIN_SUFFIX = "/";
    private static final String ERROR_SUFFIX = "/xslt?PAGE=login_post";
    private static final String SETTINGS_SUFFIX = "/xslt?PAGE=C_2_1";


    public TwoWire5031NV_030(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "document.getElementById('CHANNEL').value = " + getOptimalChannel() + ";" +
                    "document.getElementsByName('SAVE')[0].click();" +
                    "setTimeout(function() {" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1000);" +
                    "};";
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent() +
                    "document.getElementById('ADM_PASSWORD').value = '" + getPassword() + "';" +
                    "document.getElementsByClassName('button')[0].click();" +
                    "setTimeout(function() {" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + ERROR_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementById('ADM_PASSWORD') != null) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "};";
        return null;
    }
}