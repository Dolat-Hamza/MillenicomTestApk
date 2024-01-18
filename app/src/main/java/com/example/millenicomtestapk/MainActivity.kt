// MainActivity.java
package com.example.millenicomtestapk

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import com.example.millenicomtestapk.AmbeentApplication.Companion.ambeentSdk
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.wireless.ambeentutil.Ambeent
import com.wireless.ambeentutil.AmbeentDeviceId
import com.wireless.ambeentutil.AmbeentExceptions
import com.wireless.ambeentutil.ambpkg.AmbBuilder
import com.wireless.ambeentutil.ambpkg.responseType.Failure
import com.wireless.ambeentutil.ambpkg.responseType.InProgress
import com.wireless.ambeentutil.ambpkg.responseType.MessageType
import com.wireless.ambeentutil.ambpkg.responseType.Success
import com.wireless.ambeentutil.sampledata.hosts.MyWebViewClient
import org.json.JSONException
import org.json.JSONObject


const val PERMISSION_LOCATION_SERVICES = 100

@RequiresApi(34)
class MainActivity : AppCompatActivity() {
    private val PUBLIC_TESTING_COMPANY_ID = "3c7b06b3-6acc-4245-bcd2-d0b0dd7baf51"
    private val PUBLIC_TESTING_CUSTOMER_ID = "Milleni@v0.2.22"

    private var gatewayIpAddress: Int = 0 // Property to store the gateway IP address
    private lateinit var applicationContext: Context
    private lateinit var telephony: TelephonyManager
    private lateinit var geocoder: Geocoder
    private lateinit var wifiManager: WifiManager
    private lateinit var modemMac: String
    private lateinit var builder: AmbBuilder
    lateinit var snackLayout: CoordinatorLayout
    lateinit var macAddress: String
    lateinit var routerInfo0: String
    lateinit var routerInfo1: String
    var selectedBrand : String = "brand"
    var selectedRouter : String = "router"
    lateinit var extractedBrand : TextView
    lateinit var extractedRouter : TextView
    var webViewButton : Button? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the "Optimize Router" and "Web view" button click listener
        val optimizeRouterButton = findViewById<Button>(R.id.btnOptimizeRouter)
        webViewButton = findViewById<Button>(R.id.btnWebViewOpen)

        applicationContext = getApplicationContext()
        geocoder = Geocoder(applicationContext)
        Log.d("Geocoder", geocoder.toString())
        telephony =
            applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager


        ambeentSdk.registerDevice(getDeviceId(applicationContext),
            PUBLIC_TESTING_COMPANY_ID,
            object : Ambeent.RegisterDeviceCallback {
                override fun onDeviceRegistered(deviceId: String): String? {
//                    initializeUI(AmbeentDeviceId(deviceId));
                    Log.e("TAG", "onDeviceRegistered: The device id is not null $deviceId")
                    var locationClient =
                        LocationServices.getFusedLocationProviderClient(applicationContext)

                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@MainActivity, arrayOf<String>(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            PERMISSION_LOCATION_SERVICES
                        )

                    } else {
                        locationClient.lastLocation.addOnSuccessListener { location ->
                            if (location != null) {
                                Log.d("Location here", location.toString())
                                initialize(AmbeentDeviceId(deviceId), location)
                            } else {
                                // Handle location not available
                            }
                        }.addOnFailureListener { exception ->
                            // Handle location retrieval failure
                        }
                    }
                    optimizeRouterButton.visibility = View.VISIBLE
                    return deviceId
                }

                override fun onDeviceRegistrationFailed(t: Throwable) {
                    Log.e("TAG", "onDeviceRegistrationFailed: " + t.cause + "  " + t.message)

                    // handle error
                }
            })



        //displaySupportedBrands()
        // First, call getWiFiInfo() to initialize wifiManager
        getWiFiInfo()
        wifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        // Then, call initialize()
//        initialize()



        snackLayout = findViewById(R.id.layout)
        extractedBrand = findViewById<TextView>(R.id.brandExtracted)
        extractedRouter = findViewById<TextView>(R.id.routerExtracted)
        // optimizeRouter()
        optimizeRouterButton?.setOnClickListener {
            try {

                performTest()
                //Setting brand and router name
//                extractedBrand.setText(routerInfo0)
//                extractedRouter.setText(routerInfo1)
//
//                    if (builder.isTestDetectRouterModel) {
//
//                    } else {
//
//                    }
            } catch (e: AmbeentExceptions.WiFiNotEnabledException) {
                e.printStackTrace()
            } catch (e: AmbeentExceptions.WiFiConnectionNullException) {
            } catch (e: AmbeentExceptions.WiFiConnectionNullException) {
                e.printStackTrace()
            } catch (e: AmbeentExceptions.WiFiBssidNullException) {
                e.printStackTrace()
            } catch (e: AmbeentExceptions.LocationNotGrantedException) {
                e.printStackTrace()
            } catch (e: AmbeentExceptions.LocationServiceNotEnabledException) {
                e.printStackTrace()
            }
        }

        if (webViewButton != null){
            //val webViewButton = findViewById<Button>(R.id.btnWebViewOpen)
            //webViewButton!!.visibility = View.VISIBLE
            webViewButton!!.setOnClickListener(){
                //val webView = WebView(this)
                val webView = findViewById<WebView>(R.id.webView)
                val webSettings = webView.settings
                webSettings.javaScriptEnabled = true
                optimizeRouterButton.visibility = View.GONE
                webViewButton!!.visibility = View.GONE
                webView.visibility = View.VISIBLE
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
                        return super.onConsoleMessage(consoleMessage)
                    }
                }


                Log.d("GatewayAddress" ,"https://$formattedGateway")
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
                     Connected to:
                     SSID: $ssid
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


    fun initialize(ambeentDeviceId: AmbeentDeviceId, location: Location) {

        Log.e("TAG", "initializeUI: " + location.latitude + " " + location.longitude)

//        var locationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        //Log.d("locationClient", locationClient.toString())
        modemMac = Ambeent.getConnectionBSSID(wifiManager)
        Log.d("modem", modemMac)


        try {

            Log.e("Location Test Tag", "initializeUI: Location is :  " + geocoder)

            builder = AmbBuilder(
                ambeentDeviceId,
                LocationServices.getFusedLocationProviderClient(applicationContext),
                wifiManager,
                applicationContext,
                telephony,
                geocoder,
                location,
                modemMac
            )
            Log.e("TAG", "initialize builder: ${builder.location.toString()}")
            Log.d("LocationCheck", location.toString())
            builder.setCompanyId(PUBLIC_TESTING_COMPANY_ID)
            builder.setCustomerId(PUBLIC_TESTING_CUSTOMER_ID)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("TAG", "initialize: Error in initialize: ${e.message}")
        }
    }

    fun displaySnackbar(id: View, message: String) {
        val snack: Snackbar = Snackbar.make(id, message, Snackbar.LENGTH_LONG)
        snack.show()

    }

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun performTest() {

        try {
            val testRunner = builder.build()
            builder.setDetectRouterModel(true)
            var thread = testRunner.executeTest { msg, data ->

                when (msg) {
                    MessageType.FAILURE -> {
                        if (data is Failure) {
                            displaySnackbar(snackLayout, "Router is not supported")
                            Log.d("Failure", "${data.message} , ${data.code}")
                            displaySupportedBrands()
                        }
                    }

                    MessageType.INPROGRESS -> {
                        if (data is InProgress) {
                            displaySnackbar(snackLayout, "Waiting to detect router")
                            Log.d("Progress", "in Progress")
                        }
                    }

                    MessageType.SUCCESS -> {
                        if (data is Success) {
                            if (data.status !== null && data.status == "RouterModelSuccess") {
                                val dataMap = data.data as Map<*, *>

                                // Convert the Map to a JSON string
                                val jsonData = JSONObject(dataMap)
                                val jsonString = jsonData.toString()

                                try {
                                    // Parse the jsonString as a JSON object
                                    val jsonObject = JSONObject(jsonString)

                                    // Extract values
                                    macAddress = jsonObject.getString("macAddress")
                                    routerInfo0 = jsonObject.getString("routerInfo0")
                                    routerInfo1 = jsonObject.getString("routerInfo1")

                                    extractedBrand.setText(routerInfo0)
                                    extractedRouter.setText(routerInfo1)

                                    Log.d(
                                        "SuccessEDS",
                                        "macAddress: $macAddress, routerInfo0: $routerInfo0, routerInfo1: $routerInfo1"
                                    )

                                    //Different conditions to check router and brand
                                    if (routerInfo0.isNotEmpty() && routerInfo1 == ""){
                                        displaySupportedRouters(routerInfo0)
                                    }
                                    else{
                                        Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show()
                                       displaySnackbar(snackLayout, "successful")
                                    }

                                    webViewButton!!.visibility = View.VISIBLE

                                    //getSupportedRouterList()
                                } catch (e: JSONException) {
                                    // Handle JSON parsing exception
                                    e.printStackTrace()
                                }
                            }
                        }
                    }


                }
            }

        } catch (e: Exception) {
            Log.e("MainActivity", "Exception: ${e.message}", e)

        } finally {
            // Re-enable the button after the task is completed
            //executeTestsButton.isEnabled = true
        }
    }

    private fun getSupportedRouterList() {
        if (AccessPointFactory.supportedRouters.get(routerInfo0) != null) {

            if (AccessPointFactory.supportedRouters.containsKey(routerInfo0)) {
                Log.d("Found", "We found in the list " + routerInfo0)
               // optimizeRouter()
            }


            Log.e("SUPPORTED", "getSupportedRouterList: " + "FOUNDED")
//            optimizeRouter()

        }
//        for (router in AccessPointFactory.supportedRouters) {
//            Log.e("SUPPORTED", "getSupportedRouterList: " + router.key)
//            for (model in router.value) {
//                Log.e("SUPPORTED", "getSupportedRouterList: " + model)
//            }
//        }
//        if(APFactory.supportedRouters.containsKey(routerInfo0)){
//            Log.e("SUPPORTED", "getSupportedRouterList: " + "FOUNDED")
//            optimizeRouter()
//        }
    }
    fun displaySupportedBrands(){
        //Settting spinner
        var brandSpinner : Spinner = findViewById(R.id.brandSpinner)
        brandSpinner.visibility = View.VISIBLE
        val supportedBrands : List<String> = AccessPointFactory.supportedRouters.keys.toList()
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
                displaySupportedRouters(selectedBrand)
               // Toast.makeText(this@MainActivity, "Selected: $selectedBrand", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }
    }

    fun displaySupportedRouters(brand:String){
        var routerSpinner: Spinner = findViewById(R.id.routerSpinner)
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
                Log.d("Selected Router:", selectedRouter)
                // Do something with the selected item
                //Toast.makeText(this@MainActivity, "Selected: $selectedBrand", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }
    }


}
