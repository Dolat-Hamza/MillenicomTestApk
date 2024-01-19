package com.example.millenicomtestapk

import android.os.AsyncTask
import com.wireless.ambeentutil.HttpHelper
import java.io.IOException

abstract class AccessPoint {
    var username: String? = null
    var password: String? = null
    var gateway: String? = null
    var formattedGateway: String? = null
        private set
    var callbackPort: Int = 0
    var optimalChannel: Int = 0

    constructor() {}
    constructor(
        gateway: String,
        username: String?,
        password: String?,
        callbackPort: Int
    ) : this(gateway, callbackPort) {
        this.username = username
        this.password = password
    }

    constructor(gateway: String, password: String?, callbackPort: Int) : this(
        gateway,
        callbackPort
    ) {
        this.password = password
    }

    constructor(gateway: String, callbackPort: Int) {
        this.gateway = gateway
        this.callbackPort = callbackPort
        setFormattedGateway(gateway)
    }

    open fun forPopUp(): String {
        return ""
    }

    open fun getSendCode(): String {
        return "if (window.amb_sendKeyEventsAsync === undefined){" +
                "window.amb_sendKeyEventsAsync = function (elmnt, str)" +
                "{" +
                "return new Promise(function(resolve){" +
                "if (str.length==0){" +
                "resolve();" +
                "} else {" +
                "setTimeout(function() {" +
                "if (str[0] != ''){" +
                "var kei={ key:'a',code:'KeyA', bubbles: true, composed: true, cancelable: true, isComposing: false, view:document.defaultView};" +
                "var kev = new KeyboardEvent('keydown',kei );" +
                "elmnt.dispatchEvent(kev);" +
                "elmnt.value +=str[0];" +
                "var kpr = new KeyboardEvent('keypress',kei );" +
                "elmnt.dispatchEvent(kpr);" +
                "var iei={ inputType:'insertText', data:elmnt.value, bubbles: true};" +
                "var iev= new InputEvent('input',iei);" +
                "elmnt.dispatchEvent(iev);" +
                "var keu = new KeyboardEvent('keyup',kei);" +
                "elmnt.dispatchEvent(keu);" +
                "}" +
                "resolve(amb_sendKeyEventsAsync(elmnt,str.substring(1,str.length)));" +
                "},100);" +
                "}" +
                "});" +
                "}" +
                "}" +

                "if (window.amb_sendcode === undefined){" +
                "window.amb_sendcode= function(elementId,str){" +
                "return new Promise (function(resolve){" +
                "(window.amb_sendKeyEventsAsync(elementId,str))" +
                ".then(function(){ resolve(); });" +
                "});" +
                "}" +
                "}"
    }

    open fun ambeent(value: String): String {
        return  "port.postMessage('" + value + "');" +
                "port.close();"
    }

    open fun initAmbeent(): String {
        return " var port;" +
                " onmessage = function (e) {" +
                " port = e.ports[0];" +
                " };"
    }

    abstract fun getJSCodeForUrl(url: String?): String?

    //Just adds "http://" to gateway.
    private fun setFormattedGateway(gateway: String) {
        formattedGateway = "http://$gateway"
    }

    open fun channelIsValid(
        channelType: Int,
        scanResultChannelWith: Int,
        channel: Int
    ): Boolean {
        if (channelType < 0 || scanResultChannelWith < 0 || channel < 0) return false
        when (channelType) {
            0 -> {
                val channelList = arrayOf(
                    intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
                    intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
                )
                if (scanResultChannelWith > 1) return false
                return channelList[scanResultChannelWith].contains(channel)
            }
            1 -> {
                val channelList = arrayOf(
                    intArrayOf(
                        32,
                        36,
                        40,
                        44,
                        48,
                        52,
                        56,
                        60,
                        64,
                        68,
                        96,
                        100,
                        104,
                        108,
                        112,
                        116,
                        120,
                        124,
                        128,
                        132,
                        136,
                        140,
                        144,
                        149,
                        153,
                        157,
                        161,
                        165,
                        169,
                        173
                    ),
                    intArrayOf(36, 44, 52, 60, 100, 108, 116, 124, 132, 140, 149, 157),
                    intArrayOf(36, 52, 100, 116, 132, 149),
                    intArrayOf(36, 100)
                )

                if (scanResultChannelWith > 3) return false
                return channelList[scanResultChannelWith].contains(channel)
            }
            else ->
                return false
        }

    }

    val localHostCaptchaScript: String
        get() = ("var HttpRequest = new XMLHttpRequest();" +
                "var url = 'http://localhost:" + callbackPort + "/captcha';" +
                "HttpRequest.open('GET', url);" +
                "HttpRequest.send();")

    val localHostAskScript: String
        get() {
            return ("var HttpRequest = new XMLHttpRequest();" +
                    "var url = 'http://localhost:" + callbackPort + "/ask';" +
                    "HttpRequest.open('GET', url);" +
                    "HttpRequest.send();")
        }

    val localHostLoginScript: String
        get() {
            // todo: review & remove AppViewModel.mModelVerify.postValue(true)
            return ("var HttpRequest = new XMLHttpRequest();" +
                    "var url = 'http://localhost:" + callbackPort + "/loggedIn';" +
                    "HttpRequest.open('GET', url);" +
                    "HttpRequest.send();")
        }

    val localHostSuccessScript: String
        get() {
            return ("var HttpRequest = new XMLHttpRequest();" +
                    "var url = 'http://localhost:" + callbackPort + "/optSuccess/" + optimalChannel + "';" +
                    "HttpRequest.open('GET', url);" +
                    "HttpRequest.send();")
        }

    val localHostRestartModemScript: String
        get() {
            return ("var HttpRequest = new XMLHttpRequest();" +
                    "var url = 'http://localhost:" + callbackPort + "/restart';" +
                    "HttpRequest.open('GET', url);" +
                    "HttpRequest.send();")
        }

    val localHostReOptimizeScript: String
        get() {
            return ("var HttpRequest = new XMLHttpRequest();" +
                    "var url = 'http://localhost:" + callbackPort + "/reoptimize';" +
                    "HttpRequest.open('GET', url);" +
                    "HttpRequest.send();")
        }

    inner class NotifyLogin() :
        AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                HttpHelper.downloadUrl("http://localhost:$callbackPort/loggedIn")
            } catch (e: IOException) {
            }
            return null
        }
    }

    inner class NotifySuccess() :
        AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                HttpHelper.downloadUrl("http://localhost:$callbackPort/optSuccess/$optimalChannel")
            } catch (e: IOException) {
            }
            return null
        }
    }

    companion object {
        private val TAG: String = AccessPoint::class.java.simpleName
    }
}