package com.example.millenicomtestapk.ap.realtek;

import android.util.Log;

import com.example.millenicomtestapk.AccessPoint;

public class RealtekIGD extends AccessPoint {

    public static final String TAG = RealtekIGD.class.getSimpleName();

    public RealtekIGD(String gateway, String username, String password, int callbackPort) {
        super(gateway, username, password, callbackPort);
    }

    @Override
    public String getJSCodeForUrl(String url) {
        Log.d(TAG, url);

        return null;
    }
}
