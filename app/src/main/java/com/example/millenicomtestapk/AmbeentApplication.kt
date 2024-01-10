package com.example.millenicomtestapk

import android.app.Application
import com.wireless.ambeentutil.Ambeent


class AmbeentApplication : Application() {
    companion object{
        var ambeentSdk = Ambeent.getInstance()
    }
    override fun onCreate() {
        super.onCreate()

    }

}