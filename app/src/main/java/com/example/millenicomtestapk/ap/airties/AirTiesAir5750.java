package com.example.millenicomtestapk.ap.airties;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

public class AirTiesAir5750 extends AccessPoint {

    private static final String TAG = AirTiesAir5750.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/login.html";
    private static final String LOGIN_ERROR_SUFFIX = "/login.html?ErrorCode=6&redirect=&self=1";
    private static final String MAIN_SUFFIX = "/main.html";
    private static final String SETTINGS_SUFFIX = "/wireless/settings/settings_new.html";

    private static final String DEFAULT_PASSWORD = "password";

    public AirTiesAir5750(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {

        if (getOptimalChannel() > 140) setOptimalChannel(140);

        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            // Try login
            return "javascript:{" +
                    "try{" +
                    // Getting current iframe document.
                    "var iframeDocumentLogin = document.getElementById('loginmain_frame').contentDocument;" +
                    // Writing password.
                    "iframeDocumentLogin.getElementById('uiPostPassword').value = '" + getPassword() + "';" +
                    "iframeDocumentLogin.getElementById('uiPostForm').submit();" +
                    "} catch(err){" +
                    "}" +
                    "};";
        else if (url.equals(getFormattedGateway() + LOGIN_ERROR_SUFFIX))
            // Login failed
            return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            //Remote access page
            return "javascript:{" +
                    getLocalHostLoginScript() +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return getOptimalChannel() < 14 ?
                    // 2.4 GHz
                    "javascript:{" +
                            initAmbeent()+
                            "var repeatInterval = setInterval(function(){" +
                            "if (document.getElementById('cmb_channel') != null){" +
                            "document.getElementById('cmb_channel')[" + getOptimalChannel() + "].selected = true;" +
                            "document.getElementById('__ML_save').click();" +
                            "clearInterval(repeatInterval);" +
                            "setTimeout(function() {" +
                            ambeent(String.valueOf(getOptimalChannel())) +
                            "},2000);" +
                            "}" +
                            "}, 3000);" +
                            "};" :
                    // 5.0 GHz
                    "javascript:{" +
                            initAmbeent()+
                            "var repeatInterval = setInterval(function(){" +
                            "$(\"#tabs\").tabs({active: 1});" +
                            // Not on 5 GHz tab
                            "if (document.getElementById('cmb_channel_1') != null){" +
                            // Select 20 MHz from bandwidth options
                            "document.getElementById('cmb_chanbw_1')[0].selected = true;" +
                            // Apply changes of bandwidth selection to populate channel list
                            "document.getElementById('cmb_chanbw_1').onchange();" +
                            // Select the channel
                            "document.getElementById('opt_channel_1_" + getOptimalChannel() + "').selected = true;" +
                            "document.getElementById('__ML_save').click();" +
                            "clearInterval(repeatInterval);" +
                            "setTimeout(function() {" +
                            ambeent(String.valueOf(getOptimalChannel())) +
                            "},2000);" +
                            "}" +
                            "}, 3000);" +
                            "};";

        return null;
    }
}
