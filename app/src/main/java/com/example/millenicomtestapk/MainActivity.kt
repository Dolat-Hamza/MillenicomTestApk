// MainActivity.java
package com.example.millenicomtestapk

import android.net.wifi.WifiManager
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wireless.ambeentutil.Ambeent
import com.wireless.ambeentutil.Ambeent.RegisterDeviceCallback
import com.wireless.ambeentutil.AmbeentDeviceId
import com.wireless.ambeentutil.ambpkg.AmbBuilder
import com.wireless.ambeentutil.ambpkg.responseType.Failure
import com.wireless.ambeentutil.ambpkg.responseType.MessageType
import com.wireless.ambeentutil.ambpkg.responseType.Success

class MainActivity : AppCompatActivity() {

    private var gatewayIpAddress: Int = 0 // Property to store the gateway IP address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get Wi-Fi information
        getWiFiInfo()

        // Set up the "Optimize Router" button click listener
        val optimizeRouterButton = findViewById<Button>(R.id.btnOptimizeRouter)
        optimizeRouterButton.setOnClickListener {
            optimizeRouter()
        }
    }

    private fun getWiFiInfo() {
        val wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
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
}
