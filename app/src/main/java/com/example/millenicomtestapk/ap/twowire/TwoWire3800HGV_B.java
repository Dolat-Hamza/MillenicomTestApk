package com.example.millenicomtestapk.ap.twowire;

import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class TwoWire3800HGV_B extends AccessPoint {

    public static final String TAG = TwoWire3800HGV_B.class.getSimpleName();

    public static final String LOGIN_SUFFIX = "/xslt?PAGE=C05";
    public static final String MAIN_SUFFIX = "/xslt";

    public TwoWire3800HGV_B(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);

        if (url.equals(getFormattedGateway() + "/"))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + LOGIN_SUFFIX + "';" +
                    "};";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if(document.getElementsByName('channel').length==1){" +
                    "document.getElementsByName('channel')[0].selectedIndex = " + getOptimalChannel() + ";" +
                    "setTimeout( function() {" +
                    "var button = document.querySelectorAll('[type=\"button\"]');" +
                    "button[3].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    " }, 1000);" +
                    "} else if (document.getElementsByClassName('fieldlabel').length > 0){ " +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "};";

        else if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "document.getElementsByName('PASSWORD')[0].value = " + getPassword() + ";" +
                    "setTimeout(() => { " +
                    "document.getElementsByClassName('buttontext')[1].click();" +
                    "}, 1000);" +
                    "};";

        return null;
    }
}
