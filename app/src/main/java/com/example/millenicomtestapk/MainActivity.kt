// MainActivity.java
package com.example.millenicomtestapk

import android.net.wifi.WifiManager
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get Wi-Fi information
        wiFiInfo

        // Set up the "Optimize Router" button click listener
        val optimizeRouterButton = findViewById<Button>(R.id.btnOptimizeRouter)
        optimizeRouterButton.setOnClickListener { // Call the method to optimize the router or perform any desired action
            optimizeRouter()
        }
    }

    private val wiFiInfo: Unit
        private get() {
            val wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
            if (wifiManager != null && wifiManager.isWifiEnabled) {
                val wifiInfo = wifiManager.connectionInfo
                val ssid = wifiInfo.ssid // Get the SSID (name) of the connected Wi-Fi network

                // Get the DHCP information
                val dhcpInfo = wifiManager.dhcpInfo
                val gatewayIp = dhcpInfo.gateway // Get the gateway IP address

                // Display the Wi-Fi and gateway information
                val wifiInfoTextView = findViewById<TextView>(R.id.wifiInfoTextView)
                wifiInfoTextView.text = """
                     Connected to:
                     SSID: $ssid
                     Gateway IP Address: ${formatIpAddress(gatewayIp)}
                     """.trimIndent()
            } else {
                // Wi-Fi is not enabled
                val wifiInfoTextView = findViewById<TextView>(R.id.wifiInfoTextView)
                wifiInfoTextView.text = "Wi-Fi is not enabled"
            }
        }

    // Helper method to format IP address from integer
    private fun formatIpAddress(ipAddress: Int): String {
        return (ipAddress and 0xFF).toString() + "." +
                (ipAddress shr 8 and 0xFF) + "." +
                (ipAddress shr 16 and 0xFF) + "." +
                (ipAddress shr 24 and 0xFF)
    }

    // Method to optimize the router or perform any desired action
    private fun optimizeRouter() {
        // Implement your router optimization logic here
        // For example, open a WebView to interact with the router's web interface
        val webView = WebView(this)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Replace "http://router_ip_address" with the actual IP address of the router
        webView.loadUrl("http://router_ip_address")
        setContentView(webView)
    }
}