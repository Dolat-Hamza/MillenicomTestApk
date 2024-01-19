package com.example.millenicomtestapk.ap.arris;

import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class Arris5268AC_FXN extends AccessPoint {

    private static final String TAG = Arris5268AC_FXN.class.getSimpleName();
    private static final String LOGIN_SUFFIX = "/";
    private static final String MAIN_SUFFIX = "/xslt?PAGE=A_2_1";
    private static final String SETTINGS_SUFFIX = "/xslt?PAGE=C_2_1";
    private static final String SUCCESS_SUFFIX = "/xslt?PAGE=login_post";
    private State state = State.LOGIN_STATE;

    public Arris5268AC_FXN(String gateway, String password, int callbackPort) {
        super(gateway, password, callbackPort);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX) || url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";

        else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX) || url.equals(getFormattedGateway() + SUCCESS_SUFFIX))
            if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE;
                return "javascript:{" +
                        "document.getElementById('ADM_PASSWORD').value = '" + getPassword() + "';" +
                        "document.getElementsByClassName('button')[0].click();" +
                        "};";
            } else {
                return "javascript:{" +
                        initAmbeent() +
                        "if (document.getElementById('FREQUENCY') == null){" +
                        "setTimeout(function() {" +
                        ambeent("failure") +
                        "},2000);" +
                        "}" +
                        "else {" +
                        initAmbeent() +
                        "setInterval(() => {" +
                        "document.getElementById('FREQUENCY').value = 1; " +
                        "document.getElementById('CHANNEL').value= " + getOptimalChannel() + ";" +
                        "document.getElementsByName('SAVE')[0].click();" +
                        "setTimeout(function() {" +
                        ambeent(String.valueOf(getOptimalChannel())) +
                        "},2000);" +
                        "}, 1300);" +
                        "}" +
                        "};";

            }
        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE,

    }
}
