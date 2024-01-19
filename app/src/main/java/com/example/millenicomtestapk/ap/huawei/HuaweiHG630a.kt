package com.example.millenicomtestapk.ap.huawei

import com.example.millenicomtestapk.AccessPoint
//import timber.log.Timber

class HuaweiHG630a(
    gateway: String?,
    username: String? = DEFAULT_USERNAME,
    password: String? = DEFAULT_PASSWORD,
    callbackPort: Int
) : AccessPoint(gateway!!, username, password, callbackPort) {
    internal enum class State {
        LOGIN_STATE, CREDENTIAL_CHECK_STATE
    }

    private var state =
        State.LOGIN_STATE

    override fun getJSCodeForUrl(url: String?): String? {
       // Timber.e("$TAG, url: $url")
        if (url == formattedGateway + LOGIN_SUFFIX) {
            return if (state == State.LOGIN_STATE) {
                state = State.CREDENTIAL_CHECK_STATE
                "javascript:{" +
                        "document.getElementById('txt_Username').value = '" + username + "';" +
                        "document.getElementById('txt_Password').value = '" + password + "';" +
                        "setTimeout(() => {" +
                        "document.getElementById('btnLogin').click();" +
                        "}, 300);" +
                        "};"
            } else {
                "javascript:{" +
                        "if(document.getElementById('erroinfoId').innerHTML.length > 75){" +
                        localHostAskScript +
                        "}" +
                        "};"
            }
        } else if (url == formattedGateway + DEFAULT_PASSWORD_SUFFIX) return "javascript:{" +
                "window.location.href = '" + formattedGateway + MAIN_SUFFIX + "';" +
                "};" else if (url == formattedGateway + MAIN_SUFFIX) return "javascript:{" +
                localHostLoginScript +
                "var isSettingsClicked = false;" +
                "var isWLANClicked = false;" +
                "var repeat = setInterval(() => {" +
                "if (!isSettingsClicked) {" +
                "var menuFrame = document.getElementById('listfrm').contentDocument || document.getElementById('listfrm').contentWindow.document;" +
                "menuFrame.getElementById('link_Admin_1').click();" +
                "isSettingsClicked = true;" +
                "} else if (!isWLANClicked) {" +
                "var menuFrame = document.getElementById('listfrm').contentDocument || document.getElementById('listfrm').contentWindow.document;" +
                "menuFrame.getElementById('link_Admin_1_3').click();" +
                "isWLANClicked = true;" +
                "} else {" +
                "clearInterval(repeat);" +
                "var mainFrame = document.getElementById('contentfrm').contentDocument || document.getElementById('contentfrm').contentWindow.document;" +
                "var bandWidth = mainFrame.getElementsByName('bwControl')[0];" +
                "var wlChannel = mainFrame.getElementsByName('wlChannel')[0];" +
                "bandWidth.selectedIndex = 0;" +
                "wlChannel.selectedIndex = " + optimalChannel + ";" +
                "mainFrame.getElementsByName('btnApply')[0].click();" +
                localHostSuccessScript +
                "}}, 1300);" +
                "};"
        return null
    }

    companion object {
        val TAG = HuaweiHG630a::class.java.simpleName
        const val LOGIN_SUFFIX = "/html/index.asp"
        const val DEFAULT_PASSWORD_SUFFIX = "/html/management/ttnet_content.asp"
        const val MAIN_SUFFIX = "/html/content.asp"
        const val DEFAULT_USERNAME = "admin"
        const val DEFAULT_PASSWORD = "admin"
    }
}