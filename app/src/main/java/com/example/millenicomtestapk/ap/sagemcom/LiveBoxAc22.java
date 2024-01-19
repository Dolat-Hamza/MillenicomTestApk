package com.example.millenicomtestapk.ap.sagemcom;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class LiveBoxAc22 extends AccessPoint {

    private static final String TAG = LiveBoxAc22.class.getSimpleName();
    private static final String LOGIN_SUFFIX = "/4.63.50/gui/";
    private static final String LOGIN_SUFFIX2 = "/4.63.50/gui/#";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "9433C4EA";

    public LiveBoxAc22(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {

        if ((!url.startsWith(getFormattedGateway() + LOGIN_SUFFIX)) || (!url.startsWith(getFormattedGateway() + LOGIN_SUFFIX2))) {
            String a = "javascript:{" +
                    initAmbeent() +
                    "var repeatInterval = setInterval(function(){" +
                    "if(document.getElementById('configuration_title')){" +
                    "document.getElementById('configuration_configinterface_hyperlink').click();" +
                    "}" +
                    "document.getElementById('authentification_login_textbox').click();" +
                    "document.getElementById('authentification_login_textbox').value = '" + getUsername() + "';" +
                    "document.getElementById('authentification_password_passwordTextbox').click();" +
                    "document.getElementById('authentification_password_passwordTextbox').value = '" + getPassword() + "';" +
                    // Click actions
                    "var loginButton = document.getElementById('authentification_save_button');" +
                    "document.getElementById('authentification_save_button').removeAttribute('class');" +
                    "document.getElementById('authentification_save_button').removeAttribute('disabled');" +
                    "loginButton.click();" +
                    "clearInterval(repeatInterval);" +
                    "}, 1300);" +

                    "setTimeout(function () {" +
                    "if(document.getElementsByClassName('gwt-TextBox GIGV-KQCOP-fr-orange-livebox-client-ui-css-CommonsCss-redSection')[0] != null) {" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "} else {" +

                    "setTimeout(function () {" +
                    "document.getElementsByClassName('gwt-Button')[0].click();" +

                    "setTimeout(function () {" +
                    "document.getElementById('subMenu_menuNetwork.wifi_hyperlink').click();" +
                    "setTimeout(function () {";
            //"window.location.href = '" + self.getFormattedUrl() + self.wifiSuffix  + "';" +
            if (getOptimalChannel() < 14) {
                a += "document.getElementById('network_wifi_wifi2.4_foldable_imageButton').click();" +
                        "document.getElementById('network_wifi_wifi2.4_technicalSettings_channel_combobox').value = " + getOptimalChannel() + ";";

            } else {
                a += "document.getElementById('network_wifi_activation_difference_enable_radioButton').click();" +
                        "document.getElementsByClassName('GIGV-KQCKM-fr-orange-livebox-client-ui-css-CommonsCss-fpsButton')[1].click();" +
                        "document.getElementById('network_wifi_wifi5_technicalSettings_channel_combobox').value = " + getOptimalChannel() + ";";


            }
            a +=    initAmbeent() +
                    "var saveButton = document.getElementById('save_form_button');" +
                    "document.getElementById('save_form_button').removeAttribute('class');" +
                    "document.getElementById('save_form_button').removeAttribute('disabled');" +
                    "saveButton.click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +


                    "setTimeout(function(){" +
                    "var saveButton = document.getElementById('save_form_button');" +
                    "document.getElementById('save_form_button').removeAttribute('class');" +
                    "document.getElementById('save_form_button').removeAttribute('disabled');" +
                    "saveButton.click();" +
                    "setTimeout(function() {" +
                    ambeent(String.valueOf(getOptimalChannel())) +
                    "},2000);" +
                    "}, 20000);" +
                    "}, 8000);" +
                    "}, 8000);" +
                    "}, 10000);" +
                    "}" + // end of else
                    "},8000);}";
            return a;
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
                        48
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