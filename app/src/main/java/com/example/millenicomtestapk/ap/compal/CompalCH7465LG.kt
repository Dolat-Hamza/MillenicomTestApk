package com.example.millenicomtestapk.ap.compal

import com.example.millenicomtestapk.AccessPoint
//import timber.log.Timber

class CompalCH7465LG(
    gateway: String?,
    password: String? = DEFAULT_PASSWORD,
    callbackPort: Int
) : AccessPoint(gateway!!, password, callbackPort) {
    override fun getJSCodeForUrl(url: String?): String? {
        //Timber.e("$TAG, $url")
        if (url?.contains("login", true) == true) {
            return "javascript:{" +
                    "function sleep(ms) {" +
                    "return new Promise(resolve => setTimeout(resolve, ms));" +
                    "}" +
                    "async function login() {" +
                    "document.getElementById('loginPassword').value = '$password';" +
                    "await sleep(800);" +
                    "document.getElementById('c_36').disabled = false;" +
                    "await sleep(1000);" +
                    "document.getElementById('c_36').click();" +
                    "}" +
                    "login();" +
                    "}"
        } else if (url?.contains("index", true) == true) {
            return "javascript:{" +
                    "function sleep(ms) {" +
                    "return new Promise(resolve => setTimeout(resolve, ms));" +
                    "}" +
                    "async function setChannel() {" +
                    "await sleep(3000);" +
                    "document.getElementById('c_mu07').click();" +
                    "await sleep(2000);" +
                    "document.getElementById('ichannelRadio2G1').click();" +
                    "await sleep(1000);" +
                    "document.getElementById('wlChannelNum').selectedIndex = ${optimalChannel.minus(
                        1
                    )};" +
                    "await sleep(1000);" +
                    "document.getElementById('c_02').click();" +
                    "}" +
                    "setChannel();" +
                    "}"
        }
        return null
    }

    companion object {
        private val TAG = CompalCH7465LG::class.java.simpleName
        private const val DEFAULT_PASSWORD = ""
    }

}