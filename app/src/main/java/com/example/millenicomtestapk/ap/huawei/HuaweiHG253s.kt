package com.example.millenicomtestapk.ap.huawei

import com.example.millenicomtestapk.AccessPoint

class HuaweiHG253s(
    gateway: String?,
    username: String? = DEFAULT_USERNAME,
    password: String? = DEFAULT_PASSWORD,
    callbackPort: Int
) : AccessPoint(gateway!!, username, password, callbackPort) {
    override fun getJSCodeForUrl(url: String?): String? {
        when (url) {
            formattedGateway + LOGIN_SUFFIX -> return "javascript:{" +
                    // Writing username.
                    "document.getElementById('txt_Username').value= '" + username + "';" +
                    // Writing password.
                    "document.getElementById('txt_Password').value= '" + password + "';" +
                    "setTimeout(function() { " +
                    // Login click.
                    "document.getElementById('btnLogin').click(); " +
                    "document.getElementById('btnLogin').onclick()" +
                    "}, 2000);" +
                    "};"
            formattedGateway + LOGIN_ERROR_SUFFIX -> return "javascript:{" +
                    initAmbeent()+
                    "if(document.getElementById('err_info').innerHTML.length > 0){" +
                    "setTimeout(function() {" +
                    ambeent("failure") +
                    "},2000);" +
                    "}" +
                    "};"
            formattedGateway + DEFAULT_PASSWORD_SUFFIX -> return "javascript:{" +
                    "parent.frames['contentfrm'].document.getElementsByName('button2')[0].click();" +
                    "};"
            formattedGateway + MAIN_SUFFIX -> return "javascript:{" +
                    localHostLoginScript +
                    "window.location.href = '" + formattedGateway + SETTINGS_SUFFIX + "';" +
                    "};"
            formattedGateway + SETTINGS_SUFFIX -> return "javascript:{" +
                    initAmbeent()+
                    "setTimeout(function() { " +
                    // Select 2.4 ghz channel
                    "document.getElementsByName('wlChannel')[0][" + optimalChannel + "].selected = true;" +
                    // Save the channel
                    "document.getElementsByName('btnApply')[0].click();" +
                    "setTimeout(function() {" +
                    ambeent(optimalChannel.toString()) +
                    "},2000);" +
                    "}, 1000);" +
                    "};"
            else -> return null
        }
    }

    companion object {
        private val TAG = HuaweiHG253s::class.java.simpleName
        private const val LOGIN_SUFFIX = "/"
        private const val LOGIN_ERROR_SUFFIX = "/index/login.cgi"
        private const val DEFAULT_PASSWORD_SUFFIX = "/html/management/admin_content.asp"
        private const val MAIN_SUFFIX = "/html/content.asp"
        private const val SETTINGS_SUFFIX = "/html/wizard/quickwlan.asp"
        private const val DEFAULT_USERNAME = "admin"
        private const val DEFAULT_PASSWORD = "superonline"
    }
}