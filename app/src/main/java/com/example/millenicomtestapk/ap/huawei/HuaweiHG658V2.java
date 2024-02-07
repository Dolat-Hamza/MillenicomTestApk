package com.example.millenicomtestapk.ap.huawei;

import android.text.TextUtils;

import com.example.millenicomtestapk.AccessPoint;

public class HuaweiHG658V2 extends AccessPoint {

    private static final String TAG = HuaweiHG658V2.class.getSimpleName();

    private static final String LOGIN_SUFFIX = "/";
    private static final String MAIN_SUFFIX = "/html/wizard/wizard.html";
    private static final String SETTINGS_SUFFIX = "/html/advance.html#internet";

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "superonline";

    private boolean isLoggedIn;

    public HuaweiHG658V2(String gateway, String username, String password, String setupUsername, String setupPassword, int callbackPort) {
        super(gateway, username, password, setupUsername, setupPassword, callbackPort);
        if (TextUtils.isEmpty(username)) setUsername(DEFAULT_USERNAME);
        if (TextUtils.isEmpty(password)) setPassword(DEFAULT_PASSWORD);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        if (!isLoggedIn && url.equals(getFormattedGateway() + LOGIN_SUFFIX))
            return "javascript:{" +
                    // Writing username.
                    "document.getElementById('index_username').value= '" + getUsername() + "';" +
                    // Writing password.
                    "document.getElementById('password').value = '" + getPassword() + "';" +
                    // Login click.
                    "document.getElementById('loginbtn').click();" +
                    "setTimeout(function () { " +
                    // Check if there is an error message. If there is, username or pass is wrong
                    "if(document.getElementById('errorCategory').innerHTML.length > 0){ " +
                    // Ask for credentials
                    "setTimeout(function() {" +
                    "console.log('AmbeentFailure');" +
                    "},2000);" +
                    //getLocalHostAskScript() +
                    "}" +
                    "}, 1000);" +
                    // If the default password is not changed, a dialog box that warns you about changing password appears. So we need an interval to be able to click it.
                    "var repeatInterval = setInterval(function() {" +
                    "if(document.getElementById('setfirstbutton') != null){" +
                    // Click the dialog box button to login.
                    "document.getElementById('setfirstbutton').click();" +
                    "clearInterval(repeatInterval);" +
                    // Closing if braces
                    "}" +
                    // End of setInterval function.
                    "}, 1200) " +
                    // Javascript braces.
                    "};";

        else if (url.equals(getFormattedGateway() + MAIN_SUFFIX)) {
            isLoggedIn = true;
            return "javascript:{" +
                    "window.location.href = '" + getFormattedGateway() + SETTINGS_SUFFIX + "';" +
                    "};";
        } else if (url.equals(getFormattedGateway() + SETTINGS_SUFFIX))
            return "javascript:{" +
                    "setTimeout(function() {" +
                    "document.getElementById('wan_setup_InternetGatewayDevice_WANDevice_1_WANConnectionDevice_1_WANPPPConnection_1__change').click();" +
                    "},3000);" +

                    "var repeatInterval = setInterval(function(){" +
                    "if(document.getElementById('wan_internal_edit_view_wan_Username_ctrl') !== undefined && document.getElementById('wan_internal_edit_view_wan_Username_ctrl') !== null){" +
                    "clearInterval(repeatInterval);" +
                    "Ember.View.views['wan_internal_edit_view'].content.Username = '" + getSetupUsername() + "';" +
                    "Ember.View.views['wan_internal_edit_view'].content.Password = '" + getSetupPassword() + "';" +

                    "setTimeout(function() {" +
                    "document.getElementById('wan_submit_item').click();" +

                    "setTimeout(function() {" +
                    "document.getElementById('wan_setup_InternetGatewayDevice_WANDevice_2_WANConnectionDevice_1_WANPPPConnection_1__change').click();" +

                    "var repeatInterval2 = setInterval(function(){" +
                    "if(document.getElementById('wan_internal_edit_view_wan_Username_ctrl') !== undefined && document.getElementById('wan_internal_edit_view_wan_Username_ctrl') !== null){" +
                    "clearInterval(repeatInterval2);" +
                    "Ember.View.views['wan_internal_edit_view'].content.Username = '" + getSetupUsername() + "';" +
                    "Ember.View.views['wan_internal_edit_view'].content.Password = '" + getSetupPassword() + "';" +
                    "document.getElementById('wan_submit_item').click();" +


                    "setTimeout(function() {" +
                    "document.getElementById('signout_ctrl').click();" +
                    "console.log('AmbeentSuccess');" +
                    "},4000);" +
                    "}" +

                    "},2000);" +

                    "},3000);" +

                    "},3000);" +
                    "}" +
                    "},2000);" +
                    "};";

        return null;
    }
}
