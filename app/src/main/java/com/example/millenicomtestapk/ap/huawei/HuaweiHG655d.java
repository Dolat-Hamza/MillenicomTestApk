package com.example.millenicomtestapk.ap.huawei;

import android.text.TextUtils;
import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class HuaweiHG655d extends AccessPoint {

    public static final String TAG = HuaweiHG655d.class.getSimpleName();
    public static final String LOGIN_SUFFIX = "/html/index.asp";
    public static final String DEFAULT_PASSWORD_SUFFIX = "/html/management/ttnet_content.asp";
    public static final String MAIN_SUFFIX = "/html/content.asp";
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "admin";
    private State state = State.LOGIN_STATE;

    public HuaweiHG655d(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);
        if (url.equals(getFormattedGateway() + LOGIN_SUFFIX)) {
            if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE;
                return "javascript:{" +
                        "document.getElementById('txt_Username').value = '" + getUsername() + "';" +
                        "document.getElementById('txt_Password').value = '" + getPassword() + "';" +
                        "setTimeout(() => {" +
                        "document.getElementById('btnLogin').click();" +
                        "}, 400);" +
                        "};";
            } else {
                return "javascript:{" +
                        "  if (document.getElementById('erroinfoId').firstElementChild.innerHTML.length > 50) {" +
                        "  }" +
                        "};";
            }
        } else if (url.equals(getFormattedGateway() + DEFAULT_PASSWORD_SUFFIX))
            return "javascript:{" +
                    "var frame = document.getElementById('cofrm').contentDocument || document.getElementById('cofrm').contentWindow.document;" +
                    "frame.getElementsByName('button2')[0].click();" +
                    "};";
        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX))
            return "javascript:{" +
                   initAmbeent()+
                    "var isBasicClicked = false;" +
                    "var isWLANClicked = false;" +
                    "var repeat = setInterval(() => {" +
                    "  if (!isBasicClicked) {" +
                    "    var menuFrame = document.getElementById('listfrm').contentDocument || document.getElementById('listfrm').contentWindow.document;" +
                    "    menuFrame.getElementById('link_Admin_1').click();" +
                    "    isBasicClicked = true;" +
                    "  } else if (!isWLANClicked) {" +
                    "    var menuFrame = document.getElementById('listfrm').contentDocument || document.getElementById('listfrm').contentWindow.document;" +
                    "    menuFrame.getElementById('link_Admin_1_6').click();" +
                    "    isWLANClicked = true;" +
                    "  } else {" +
                    "    clearInterval(repeat);" +
                    "    var innerFrame = document.getElementById('contentfrm').contentDocument || document.getElementById('contentfrm').contentWindow.document;" +
                    "    var sel = innerFrame.getElementsByName('wlChannel')[0];" +
                    "    sel.selectedIndex = " + getOptimalChannel() + ";" +
                    "    innerFrame.getElementsByName('btnApply')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}}, 2000);" +
                    "};";


        return null;
    }

    enum State {
        LOGIN_STATE,
        CREDENTIAL_CHECK_STATE
    }

}
