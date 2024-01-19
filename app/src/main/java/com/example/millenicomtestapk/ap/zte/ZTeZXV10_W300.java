package com.example.millenicomtestapk.ap.zte;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class ZTeZXV10_W300 extends AccessPoint {

    public static final String TAG = ZTeZXV10_W300.class.getSimpleName();
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "ttnet";
    private static final String LOGIN_SUFFIX = "/";
    private static final String MAIN_SUFFIX = "/rpSys.html";
    private State state = State.LOGIN_STATE;

    public ZTeZXV10_W300(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (state == State.LOGIN_STATE) {
            if (url.equals(getFormattedGateway() + LOGIN_SUFFIX) || (url.equals(getFormattedGateway() + MAIN_SUFFIX)))
                return "javascript:{" +
                        "var pure = '" + getFormattedGateway() + "';" +
                        "var link = pure.slice(0, 7) + '" + getUsername() + ":" + getPassword() + "@' + pure.slice(7) + '/basic/home_wlan.htm';" +
                        "window.location.href = link;" +
                        "};";
            else
                state = State.CHECK_STATE;
            return "javascript:{" +
                    initAmbeent()+
                    "if(document.getElementsByName('SaveBtn')[0] == null){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "else{" +
                    initAmbeent() +
                    "setInterval(() => {" +
                    "document.getElementsByName('Channel_ID')[0].selectedIndex= " + getOptimalChannel() + ";" +
                    "document.getElementsByName('SaveBtn')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 1300);" +
                    "}" + "};";
        }

        return null;
    }

    enum State {
        LOGIN_STATE,
        CHECK_STATE
    }
}
