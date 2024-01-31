// MainActivity.java
package com.example.millenicomtestapk

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.millenicomtestapk.Constants.APP_CALLBACK_PORT
import com.google.android.material.snackbar.Snackbar
import com.wireless.ambeentutil.Ambeent.getUpnpInfo
import com.wireless.ambeentutil.sampledata.hosts.MyWebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

//new imports
import java.util.Locale
import com.example.millenicomtestapk.Constants.ACTIONTEC
import com.example.millenicomtestapk.Constants.AIRTIES
import com.example.millenicomtestapk.Constants.ARRIS
import com.example.millenicomtestapk.Constants.ASUS
import com.example.millenicomtestapk.Constants.BILLION
import com.example.millenicomtestapk.Constants.CASTLENET
import com.example.millenicomtestapk.Constants.COMPAL
import com.example.millenicomtestapk.Constants.EDGECORE
import com.example.millenicomtestapk.Constants.EFM
import com.example.millenicomtestapk.Constants.HUAWEI
import com.example.millenicomtestapk.Constants.LINKSYS
import com.example.millenicomtestapk.Constants.MOTOROLA
import com.example.millenicomtestapk.Constants.NETGEAR
import com.example.millenicomtestapk.Constants.REALTEK
import com.example.millenicomtestapk.Constants.ROUTERBOARD
import com.example.millenicomtestapk.Constants.SERCOMM
import com.example.millenicomtestapk.Constants.TPLINK
import com.example.millenicomtestapk.Constants.TWOWIRE
import com.example.millenicomtestapk.Constants.UNKNOWN
import com.example.millenicomtestapk.Constants.VERIZON
import com.example.millenicomtestapk.Constants.ZTE
import com.example.millenicomtestapk.Constants.ZYXEL


const val PERMISSION_LOCATION_SERVICES = 100

@RequiresApi(34)
class MainActivity : AppCompatActivity() {
//    private val PUBLIC_TESTING_COMPANY_ID = "3c7b06b3-6acc-4245-bcd2-d0b0dd7baf51"
//    private val PUBLIC_TESTING_CUSTOMER_ID = "Milleni@v0.2.22"
    var accesspoint : AccessPoint? = null
    private var mAccessPoint: AccessPoint? = accesspoint

    private var gatewayIpAddress: Int = 0 // Property to store the gateway IP address
    private lateinit var applicationContext: Context
    private lateinit var wifiManager: WifiManager
//    private lateinit var modemMac: String
//    private lateinit var builder: AmbBuilder
    lateinit var snackLayout: CoordinatorLayout
    lateinit var macAddress: String
    lateinit var routerInfo0: String
    lateinit var routerInfo1: String
    var selectedBrand : String = "brand"
    var selectedRouter : String = "router"
    lateinit var extractedBrand : TextView
    lateinit var extractedRouter : TextView
    var webViewButton : Button? = null
    var finalBrand : String = ""
    var finalRouter : String = ""
    var finalGateway : String = ""
    lateinit var  webView : WebView
    var setupUsername : String = ""
    var setupPassword : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the "Optimize Router" and "Web view" button click listener
        //val optimizeRouterButton = findViewById<Button>(R.id.btnOptimizeRouter)
        webViewButton = findViewById<Button>(R.id.btnWebViewOpen)
        webView = findViewById<WebView>(R.id.webView)

        val intent = intent
        setupUsername = intent.getStringExtra("setupUsername").toString()
        setupPassword = intent.getStringExtra("setupPassword").toString()
        Toast.makeText(this, setupUsername + " " + setupPassword, Toast.LENGTH_SHORT).show()



        applicationContext = getApplicationContext()


        //displaySupportedBrands()
        // First, call getWiFiInfo() to initialize wifiManager
        getWiFiInfo()
        wifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager


        snackLayout = findViewById(R.id.layout)
        extractedBrand = findViewById<TextView>(R.id.brandExtracted)
        extractedRouter = findViewById<TextView>(R.id.routerExtracted)
        // optimizeRouter()
        //getRouterInfo(this)
        extractGatewayAndRouterInfo(this)


        if (webViewButton != null){
                //val webViewButton = findViewById<Button>(R.id.btnWebViewOpen)
                //webViewButton!!.visibility = View.VISIBLE
                webViewButton!!.setOnClickListener(){
                        openWebView()
                }

            }


    }

    private fun getWiFiInfo() {
        wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
        Log.d("wifi", wifiManager.toString())
//        modemMac = Ambeent.getConnectionBSSID(wifiManager)
//        Log.d("modem", modemMac)
        if (wifiManager != null && wifiManager.isWifiEnabled) {
            val wifiInfo = wifiManager.connectionInfo
            val ssid = wifiInfo.ssid // Get the SSID (name) of the connected Wi-Fi network

            // Get the DHCP information
            val dhcpInfo = wifiManager.dhcpInfo
            gatewayIpAddress = dhcpInfo.gateway // Store the gateway IP address

            // Display the Wi-Fi and gateway information
            val wifiInfoTextView = findViewById<TextView>(R.id.wifiInfoTextView)
            wifiInfoTextView.text = """
                     Gateway IP Address: ${formatIpAddress(gatewayIpAddress)}
                     """.trimIndent()
        } else {
            // Wi-Fi is not enabled
            val wifiInfoTextView = findViewById<TextView>(R.id.wifiInfoTextView)
            wifiInfoTextView.text = "Wi-Fi is not enabled"
        }
    }

    private fun formatIpAddress(ipAddress: Int): String {
        return (ipAddress and 0xFF).toString() + "." +
                (ipAddress shr 8 and 0xFF) + "." +
                (ipAddress shr 16 and 0xFF) + "." +
                (ipAddress shr 24 and 0xFF)
    }

    private fun optimizeRouter() {
        // You can now use the stored gateway IP address wherever needed
        // getWiFiInfo()

        // Create a WebView within the context of your activity
        val webView = WebView(this)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Replace "http://$gatewayIpAddress" with the actual IP address of the router
        webView.loadUrl("http://$gatewayIpAddress")


    }



    fun displaySnackbar(id: View, message: String) {
        val snack: Snackbar = Snackbar.make(id, message, Snackbar.LENGTH_LONG)
        snack.show()

    }

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }


    fun displaySupportedBrands(){
        //Settting spinner
        val brandLayout : LinearLayout = findViewById(R.id.brandLinear)
        var brandSpinner : Spinner = findViewById(R.id.brandSpinner)
        brandLayout.visibility = View.VISIBLE
        brandSpinner.visibility = View.VISIBLE
        brandSpinner.setPrompt("Select Brand")
        val supportedBrands : List<String> = AccessPointFactory.supportedRouters.keys.toList()
        Log.d("SupportedList", AccessPointFactory.supportedRouters.keys.toList().toString())
        val dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, supportedBrands)
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        brandSpinner.setAdapter(dataAdapter)
        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                selectedBrand = supportedBrands[position]
                dataAdapter.notifyDataSetChanged()
                // Do something with the selected item
                //println("Selected item: $selectedItem")
                Log.d("Selected Brand:", selectedBrand)
                extractedBrand.setText(selectedBrand)
                displaySupportedRouters(selectedBrand)
               // Toast.makeText(this@MainActivity, "Selected: $selectedBrand", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle the case when nothing is selected
                extractedBrand.setText("")
            }
        }
    }

    fun displaySupportedRouters(brand:String){
        val routerLayout : LinearLayout = findViewById(R.id.routerLinear)
        var routerSpinner: Spinner = findViewById(R.id.routerSpinner)
        routerLayout.visibility = View.VISIBLE
        routerSpinner.visibility = View.VISIBLE
        val supportedRouters : Array<String> = AccessPointFactory.supportedRouters[brand]!!
        //Log.d("Routers", "Array for key '$brand': ${supportedRouters.joinToString(", ")}")
        val dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, supportedRouters)
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        routerSpinner.setAdapter(dataAdapter)
        routerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                selectedRouter = supportedRouters[position]
                dataAdapter.notifyDataSetChanged()
                extractedRouter.setText(selectedRouter)
                // New Lines added
                val dhcpInfo = wifiManager.dhcpInfo
                val gatewayAddress = InetAddress.getByAddress(intToByteArray(dhcpInfo.gateway)).hostAddress
                val myRouter = RouterEntity(brand,selectedRouter, "", "", setupUsername,setupPassword)
                AccessPointFactory.getAccessPoint(
                    router = myRouter,
                    gateway = gatewayAddress.toString(),
                    callbackPort = APP_CALLBACK_PORT
                )
                accesspoint = AccessPointFactory.getAccessPoint(
                    router = myRouter,
                    gateway = gatewayAddress.toString(),
                    callbackPort = APP_CALLBACK_PORT
                )
                finalBrand = brand
                finalRouter = selectedRouter
                finalGateway = gatewayAddress.toString()
                mAccessPoint = accesspoint

                Log.d("Selected Router1:", selectedRouter)
                // Do something with the selected item
                //Toast.makeText(this@MainActivity, "Selected: $selectedBrand", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle the case when nothing is selected
                extractedRouter.setText("")
            }
        }
    }

    @SuppressLint("ServiceCast")
    fun extractGatewayAndRouterInfo(context: Context): Pair<String?, Array<String?>?> {
        var gatewayAddress: String? = null
        var routerInfo: Array<String?>? = null
        val progress : ProgressBar = findViewById(R.id.progressbar)

        GlobalScope.launch(Dispatchers.IO) {
            try {

                runOnUiThread(){
                    progress.visibility = View.VISIBLE
                }
                // Attempt 1: Get gateway from ConnectivityManager (API 29+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Log.d("Step1", "In attempt 1")
                    val connectivityManager =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork = connectivityManager.activeNetwork
                    val linkProperties = connectivityManager.getLinkProperties(activeNetwork)
                    val routes = linkProperties?.routes ?: emptyList()
                    for (route in routes) {
                        if (route.isDefaultRoute() && !(route.getGateway() is Inet6Address)) {
                            gatewayAddress = route.gateway?.hostAddress
                            Log.d("GatewayAddress", "In Step 1 "+ gatewayAddress.toString())
                            // Attempt to extract router info using UPnP
                            routerInfo = getUpnpInfo()
                            Log.d("RouterInfoDetails", "Router Info: ${routerInfo?.joinToString(", ")}")

                            if (routerInfo?.get(0).isNullOrEmpty() && routerInfo?.get(1).isNullOrEmpty()){
                                Log.d("NoBrand", "No Brand")
                                runOnUiThread(){
                                    displaySupportedBrands()
                                }
                            }

                            else if (routerInfo?.get(1).isNullOrEmpty()){
                                Log.d("NoRouter", "I am here")
                                runOnUiThread(){
                                    val brandE = routerInfo?.get(0)
                                    if (brandE != null) {
                                        displaySupportedRouters(brandE)
                                    }
                                }
                            }

                            else{
                                runOnUiThread {
                                    extractedBrand.setText(getBrandShortName(routerInfo?.get(0)))
                                    extractedRouter.setText(routerInfo?.get(1))
                                }
                                val myRouter = RouterEntity(getBrandShortName(routerInfo?.get(0)),routerInfo?.get(1), "", "", setupUsername, setupPassword)
                                AccessPointFactory.getAccessPoint(
                                    router = myRouter,
                                    gateway = gatewayAddress.toString(),
                                    callbackPort = APP_CALLBACK_PORT
                                )
                                //New Lines added
                                accesspoint = AccessPointFactory.getAccessPoint(
                                    router = myRouter,
                                    gateway = gatewayAddress.toString(),
                                    callbackPort = APP_CALLBACK_PORT
                                )
                                finalBrand = getBrandShortName(routerInfo?.get(0))
                                finalRouter = routerInfo?.get(1)!!
                                finalGateway = gatewayAddress.toString()
                                mAccessPoint = accesspoint
                                Log.d("checkAccess", mAccessPoint.toString())
                                Log.d("InfoStep1", "Gateway Address is : " + gatewayAddress.toString() + " Router Info is : " + routerInfo.toString())
                                break
                            }

                        }
                    }
                    if (gatewayAddress == null || routerInfo == null) {
                        displaySnackbar(snackLayout, "Router is not supported")
                    }
                }

                //Checking for lower android versions
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val wifiInfo = wifiManager.connectionInfo

                    // Extract gateway address from DHCP info
                    val dhcpInfo = wifiManager.dhcpInfo
                    gatewayAddress = InetAddress.getByAddress(intToByteArray(dhcpInfo.gateway)).hostAddress

                    // Attempt to extract router info using UPnP or other methods
                    routerInfo = getUpnpInfo()
                    // ... rest of your code ...
                    Log.d("RouterInfoDetails", "Router Info: ${routerInfo?.joinToString(", ")}")

                    if (routerInfo?.get(0).isNullOrEmpty() && routerInfo?.get(1).isNullOrEmpty() ){
                        runOnUiThread(){
                            displaySupportedBrands()
                        }
                    }

                    else if (routerInfo?.get(1).isNullOrEmpty()){
                        val brandE = routerInfo?.get(0)
                        runOnUiThread(){
                            if (brandE != null) {
                                displaySupportedRouters(brandE)
                            }
                        }
                    }
                    else{
                        runOnUiThread {
                            extractedBrand.setText(getBrandShortName(routerInfo?.get(0)))
                            extractedRouter.setText(routerInfo?.get(1))
                        }
                        val myRouter = RouterEntity(getBrandShortName(routerInfo?.get(0)),routerInfo?.get(1), "", "", setupUsername, setupPassword)
                        Log.d("myRouter", myRouter.toString())
                        AccessPointFactory.getAccessPoint(
                            router = myRouter,
                            gateway = gatewayAddress.toString(),
                            callbackPort = APP_CALLBACK_PORT
                        )
                        //New Lines added
                        accesspoint = AccessPointFactory.getAccessPoint(
                            router = myRouter,
                            gateway = gatewayAddress.toString(),
                            callbackPort = APP_CALLBACK_PORT
                        )
                        finalBrand = getBrandShortName(routerInfo?.get(0))
                        finalRouter = routerInfo?.get(1)!!
                        finalGateway = gatewayAddress.toString()
                        mAccessPoint = accesspoint
                        Log.d("checkAccess", mAccessPoint.toString())
                        Log.d("LowerAndroidVersions", "Gateway Address is : " + gatewayAddress.toString() + " Router Info is : " + routerInfo.toString())
                    }
                    if (gatewayAddress == null || routerInfo == null) {
                        displaySnackbar(snackLayout, "Router is not supported")
                    }
                }

                // Attempt 2: Get gateway from DhcpInfo (for older APIs)
                if (gatewayAddress == null) {
                    Log.d("Step1", "In attempt 2")
                    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val dhcpInfo = wifiManager.dhcpInfo
                    val gateway = InetAddress.getByAddress(
                        byteArrayOf(
                            dhcpInfo.gateway.toByte(),
                            dhcpInfo.gateway.toByte(),
                            dhcpInfo.gateway.toByte(),
                            1
                        )
                    )
                    gatewayAddress = gateway.hostAddress
                    Log.d("GatewayAddress", "In Step 2 " + gatewayAddress.toString())
                }

                // Attempt 3: Iterate through network interfaces (fallback)
                if (gatewayAddress == null) {
                    Log.d("Step1", "In attempt 3")
                    val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
                    for (intf in interfaces) {
                        if (intf.isUp && !intf.isLoopback) {
                            val addresses = Collections.list(intf.inetAddresses)
                            for (addr in addresses) {
                                if (addr is Inet4Address && !addr.isLoopbackAddress) {
                                    val gateway = InetAddress.getByName("192.168.1.1") // Common default gateway
                                    gatewayAddress = gateway.hostAddress
                                    Log.d("GatewayAddress","In Step 3 "+ gatewayAddress.toString())
//                                    if (intf.isReachable(gateway, 100)) {
//                                        gatewayAddress = gateway.hostAddress
//                                        break
//                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("GatewayInfo", "Error extracting gateway and router information: ${e.message}", e)
            }
            finally {
                runOnUiThread {
                    progress.visibility = View.GONE
                }
            }
        }

        return Pair(gatewayAddress, routerInfo)

    }
    // Helper function to convert integer to byte array
    private fun intToByteArray(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xFF).toByte(),
            ((value shr 8) and 0xFF).toByte(),
            ((value shr 16) and 0xFF).toByte(),
            ((value shr 24) and 0xFF).toByte()
        )
    }

    private fun showCredentialsPopup() {
        webView.visibility = View.GONE
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Authentication Required")
        builder.setMessage("Please enter your username and password.")

        val inputUsername = EditText(this)
        inputUsername.hint = "Username"
        val inputPassword = EditText(this)
        inputPassword.hint = "Password"
        inputPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        builder.setView(
            LinearLayoutCompat(this).apply {
                orientation = LinearLayoutCompat.VERTICAL
                layoutParams = LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                addView(inputUsername)
                addView(inputPassword)
            }
        )

        builder.setPositiveButton("OK") { _, _ ->
            val username = inputUsername.text.toString()
            val password = inputPassword.text.toString()

            // Process the entered username and password as needed
            // You may want to handle the authentication logic here

            // Continue with your WebView logic...
            val myRouter = RouterEntity(finalBrand,finalRouter, username, password, setupUsername, setupPassword)
            AccessPointFactory.getAccessPoint(
                router = myRouter,
                gateway = finalGateway,
                callbackPort = APP_CALLBACK_PORT
            )
            //New Lines added
            accesspoint = AccessPointFactory.getAccessPoint(
                router = myRouter,
                gateway = finalGateway,
                callbackPort = APP_CALLBACK_PORT
            )
            mAccessPoint = accesspoint

            if (mAccessPoint != null){
                openWebView()
            }
            else{
                displaySnackbar(snackLayout, "Access point not found.")
            }

        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()

            // Handle the cancelation or provide a fallback mechanism
        }

        builder.show()
    }

    fun getBrandShortName(brand: String?): String {
        return when {
            brand.isNullOrBlank() -> UNKNOWN
            brand.toLowerCase(Locale.ENGLISH).contains("actiontec") -> ACTIONTEC
            brand.toLowerCase(Locale.ENGLISH).contains("airties") -> AIRTIES
            brand.toLowerCase(Locale.ENGLISH).contains("arris") -> ARRIS
            brand.toLowerCase(Locale.ENGLISH).contains("asus") -> ASUS
            brand.toLowerCase(Locale.ENGLISH).contains("billion") -> BILLION
            brand.toLowerCase(Locale.ENGLISH).contains("compal") -> COMPAL
            brand.toLowerCase(Locale.ENGLISH).contains("zyxel") -> ZYXEL
            brand.toLowerCase(Locale.ENGLISH).contains("tp-link") -> TPLINK
            brand.toLowerCase(Locale.ENGLISH).contains("huawei") -> HUAWEI
            brand.toLowerCase(Locale.ENGLISH).contains("mtrlc") -> MOTOROLA
            brand.toLowerCase(Locale.ENGLISH).contains("motorola") -> MOTOROLA
            brand.toLowerCase(Locale.ENGLISH).contains("routerboard") -> ROUTERBOARD
            brand.toLowerCase(Locale.ENGLISH).contains("verizon") -> VERIZON
            brand.toLowerCase(Locale.ENGLISH).contains("zte") -> ZTE
            brand.toLowerCase(Locale.ENGLISH).contains("2wire") -> TWOWIRE
            brand.toLowerCase(Locale.ENGLISH).contains("efm") -> EFM
            brand.toLowerCase(Locale.ENGLISH).contains("netgear") -> NETGEAR
            brand.toLowerCase(Locale.ENGLISH).contains("realtek") -> REALTEK
            brand.toLowerCase(Locale.ENGLISH).contains("linksys") -> LINKSYS
            brand.toLowerCase(Locale.ENGLISH).contains("vodafone") -> CASTLENET
            brand.toLowerCase(Locale.ENGLISH).contains("dwnet") -> SERCOMM
            brand.toLowerCase(Locale.ENGLISH).contains("broadcom") -> ZTE
            brand.toLowerCase(Locale.ENGLISH).contains("castlenet") -> CASTLENET
            brand.toLowerCase(Locale.ENGLISH).contains("edgecore") -> EDGECORE
            else -> brand
        }
    }

    fun openWebView (){
        val progressBar : ProgressBar = findViewById(R.id.progressbar)
        progressBar.visibility = View.VISIBLE

        //webView.visibility = View.VISIBLE
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webViewButton!!.visibility = View.GONE
        //webView.visibility = View.VISIBLE
        webView.webViewClient = WebViewClient()

        // new lines addition
        WebView.setWebContentsDebuggingEnabled(true)
        webView.settings.domStorageEnabled = true

        val formattedGateway = formatIpAddress(gatewayIpAddress)
        // Replace "http://$gatewayIpAddress" with the actual IP address of the router
        webView.loadUrl("http://$formattedGateway")
        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d("WebViewConsole", "${consoleMessage?.message()}")
                if (consoleMessage?.message().toString() == "AmbeentFailure"){
                    progressBar.visibility = View.GONE
                    displaySnackbar(snackLayout, "Invalid Credentials")
                    showCredentialsPopup()
                }
                else if (consoleMessage?.message().toString() == "AmbeentSuccess"){
                    webView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    displaySnackbar(snackLayout, "Your router setup is successfull.")
                }
                return super.onConsoleMessage(consoleMessage)
            }
        }
        // New code addition for Error
        webView.webViewClient = object :WebViewClient(){
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest?,
                error: WebResourceError
            ) {
                val errorDescription = error.description.toString()
                //Timber.e("$TAG - Error: $errorDescription")

                // Show a popup to take username and password
                showCredentialsPopup()

                // Rest of your code...
            }
        }

        //New addition for JS injection
        //webView.webViewClient.onPageFinished(webView, "http://$formattedGateway")
//                val cookie = CookieManager.getInstance().getCookie(formattedGateway)
        val cookie = CookieManager.getInstance().getCookie(formattedGateway)
        var url = "http://$formattedGateway"

        Log.d("URLCheck", "url: $url")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val jsCode = mAccessPoint?.getJSCodeForUrl(url)
                Log.d("mAccessPoint", mAccessPoint.toString())
                Log.d("JSCode", jsCode.toString())
                super.onPageFinished(view, url)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && jsCode != null) {
                    webView.evaluateJavascript(jsCode) { value ->
                        Log.d("Check", value!!)
                    }
                }
            }
        }

        //webView.loadUrl("http://$formattedGateway")

        //new Lines added

//
//                mAccessPoint?.let { ap ->
//                    val jsCode: String? = formattedGateway.let(ap::getJSCodeForUrl)
//                    Log.d("jsCode" , jsCode.toString())
//
//                    jsCode?.let {
//                        webView.evaluateJavascript(
//                            it
//                        ) { value ->
//                            Log.d("New Value", value.toString())
//                        }
//                    }
//                }
        Log.d("GatewayAddress" ,"https://$formattedGateway")
    }

}
