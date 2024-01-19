package com.example.millenicomtestapk.ap.zyxel;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ZyxelP_660W_T1v3 extends AccessPoint {

    private static final String TAG = ZyxelP_660W_T1v3.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/";
    private static final String ERROR_SUFFIX = "/rpAuth.html";
    private static final String CREDENTIAL_CHECK_SUFFIX = "/passWarning.html";
    private static final String OPTIONS_SUFFIX = "/Act_option.html";
    private static final String MAIN_SUFFIX = "/rpSys.html";

    private static final String DEFAULT_PASSWORD = "ttnet2";


    public ZyxelP_660W_T1v3(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);


    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    "document.getElementsByName('LoginPassword')[0].value= '" + getPassword() + "';" +
                    "document.getElementsByName('Prestige_Login')[0].click();" +
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
                    "window.location.href = '" + getFormattedGateway() + MAIN_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + OPTIONS_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + MAIN_SUFFIX + "';" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    initAmbeent()+
                    "if (document.getElementsByName('LoginPassword')[0] !=null){" +
                    "document.getElementsByName('LoginPassword')[0].value='" + getPassword() + "';" +
                    "document.getElementsByName('Prestige_Login')[0].click();" +
                    "} else {" +
                    "setTimeout(function(){" +
                    "parent.frames[`panel`].document.getElementsByClassName(`sub1`)[2].click();" +
                    "setTimeout(function(){" +
                    "parent.frames[`main`].document.getElementsByName('Channel_ID')[0][" + (getOptimalChannel() - 1) + "].selected = true;" +
                    "setTimeout(function(){" +
                    "parent.frames[`main`].document.getElementsByName('sysSubmit')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 2000); " +
                    "}, 2000); " +
                    "}, 2000); " +
                    "}" +
                    "};";

        return null;
    }
}
