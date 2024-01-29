package com.example.millenicomtestapk


import com.example.millenicomtestapk.ap.huawei.*
import com.example.millenicomtestapk.ap.tplink.*
import com.example.millenicomtestapk.ap.zyxel.*
import com.example.millenicomtestapk.Constants.HG531s
import com.example.millenicomtestapk.Constants.HG658V2
import com.example.millenicomtestapk.Constants.HG658cV2
import com.example.millenicomtestapk.Constants.HUAWEI
import com.example.millenicomtestapk.Constants.TDW9970
import com.example.millenicomtestapk.Constants.TDW9970_TR
import com.example.millenicomtestapk.Constants.TPLINK
import com.example.millenicomtestapk.Constants.VMG3312B10B
import com.example.millenicomtestapk.Constants.ZYXEL
import com.wireless.ambeentutil.RouterEntity


object AccessPointFactory {

    private val TAG = AccessPointFactory::class.java.simpleName
    val supportedRouters: Map<String, Array<String>> = mapOf(
        HUAWEI to arrayOf(
           HG531s,  HG658V2, ),
        TPLINK to arrayOf(
            TDW9970, TDW9970_TR,),
        ZYXEL to arrayOf(
            VMG3312B10B,
        ),
    )

    fun getAccessPoint(
        router: com.example.millenicomtestapk.RouterEntity,
        gateway: String,
        callbackPort: Int
    ): AccessPoint? {
        //Log.e("getAccessPoint, router: $router, gateway: $gateway, callbackport: $callbackPort")
        if (router == null) return null
        val supportedModels =
            supportedRouters[router.brand] ?: return null
        for (model in supportedModels) {
            if (router.model.equals(
                    model,
                    true
                ) || router.model == "unknown" || router.model == "Unknown"
            ) return router.brand?.let {
                createAccessPoint(
                    it,
                    model,
                    gateway,
                    router.username,
                    router.password,
                    router.setupUsername,
                    router.setupPassword,
                    callbackPort
                )
            }
        }
//        Timber.e(String.format("%s %s is not supported", router.brand, router.model))
        return null
    }

    private fun createAccessPoint(
        brand: String,
        model: String,
        gateway: String,
        username: String? = null,
        password: String? = null,
        setupUsername: String? = null,
        setupPassword: String? = null,
        callbackPort: Int
    ): AccessPoint? {
//        Timber.e("createAccessPoint($brand, $model)")
        return when (brand) {
            HUAWEI -> createHuawei(model, gateway, username, password, setupUsername, setupPassword, callbackPort)
            TPLINK -> createTPLink(model, gateway, username, password, setupUsername, setupPassword,callbackPort)
            ZYXEL -> createZyxel(model, gateway, username, password, setupUsername, setupPassword, callbackPort)
            else -> null
        }
    }

    private fun createHuawei(
        model: String,
        gateway: String,
        username: String?,
        password: String?,
        setupUsername: String?,
        setupPassword: String?,
        callbackPort: Int
    ): AccessPoint? {
        return when (model) {
            HG531s -> HuaweiHG531s(gateway, username, password,setupUsername, setupPassword, callbackPort)
            HG658V2 -> HuaweiHG658V2(gateway, username, password,setupUsername, setupPassword, callbackPort)
            else -> null
        }
    }


    private fun createTPLink(
        model: String,
        gateway: String,
        username: String?,
        password: String?,
        setupUsername: String?,
        setupPassword: String?,
        callbackPort: Int
    ): AccessPoint? {
        return when (model) {
            TDW9970 -> TPLinkTD_W9970(gateway, username, password, setupUsername, setupPassword, callbackPort)
            TDW9970_TR -> TPLinkTD_W9970_TR(gateway, username, password, setupUsername, setupPassword, callbackPort)
            else -> null
        }
    }


    private fun createZyxel(
        model: String,
        gateway: String,
        username: String?,
        password: String?,
        setupUsername: String?,
        setupPassword: String?,
        callbackPort: Int
    ): AccessPoint? {
        //Timber.e("createZyxel($model)")
        return when (model) {
            VMG3312B10B -> ZyxelVMG3312_B10B(gateway, username, password, setupUsername, setupPassword, callbackPort)
            else -> null
        }
    }

}