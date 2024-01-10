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
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
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
import com.wireless.ambeentutil.LibRepository
import com.wireless.ambeentutil.ambpkg.AmbBuilder
import com.wireless.ambeentutil.ambpkg.responseType.Failure
import com.wireless.ambeentutil.ambpkg.responseType.InProgress
import com.wireless.ambeentutil.ambpkg.responseType.MessageType
import com.wireless.ambeentutil.ambpkg.responseType.Success


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

                    return deviceId
                }

                override fun onDeviceRegistrationFailed(t: Throwable) {
                    Log.e("TAG", "onDeviceRegistrationFailed: " + t.cause + "  " + t.message)
                    // handle error
                }
            })


        // First, call getWiFiInfo() to initialize wifiManager
        getWiFiInfo()
        wifiManager =
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        // Then, call initialize()
//        initialize()


        // Set up the "Optimize Router" button click listener
        val optimizeRouterButton = findViewById<Button>(R.id.btnOptimizeRouter)
        snackLayout = findViewById(R.id.layout)

            // optimizeRouter()
        optimizeRouterButton?.setOnClickListener {
                try {

                       // builder.setDetectRouterModel(true)
                    performTest()
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
        val webView = WebView(this)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Replace "http://$gatewayIpAddress" with the actual IP address of the router
        webView.loadUrl("http://${formatIpAddress(gatewayIpAddress)}")
        setContentView(webView)
    }

    @RequiresApi(34)
    fun initialize(ambeentDeviceId: AmbeentDeviceId, location: Location) {
        Log.e("TAG", "initializeUI: "+ location.latitude + " " + location.longitude)

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
            var thread = testRunner.executeTest { msg, data ->

                when (msg) {
                    MessageType.FAILURE -> {
                        if (data is Failure) {
                            displaySnackbar(snackLayout, "Router is not supported")
                            Log.d("Failure", "${data.message} , ${data.code}" )
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
                            displaySnackbar(snackLayout, "successful")
                            Log.d("Success", "in success")
                            //optimizeRouter()
                        }
                    }
                }
            }

        } catch (e: Exception) {
            // Handle any exceptions that might occur during execution
            e.printStackTrace()
//                    val snackBar = Snackbar.make(
//                        //requireView(), "Error running tests: ${e.message}", Snackbar.LENGTH_LONG
//                    )
//                    snackBar.show()
        } finally {
            // Re-enable the button after the task is completed
            //executeTestsButton.isEnabled = true
        }
    }

}
