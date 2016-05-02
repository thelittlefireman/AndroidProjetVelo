package com.ppp.esir.projetvelo.utils;

import android.content.Context;

import com.ppp.esir.projetvelo.listeners.SmoothBluetoothListenerDiscoverDevices;

import io.palaima.smoothbluetooth.SmoothBluetooth;

/**
 * Created by thoma on 02/05/2016.
 */
public class BluetoothCommandsUtils {
    private static SmoothBluetooth mSmoothBluetooth;
    private static SmoothBluetooth.Listener mSmoothBluetoothListener;
    private static SmoothBluetooth.ConnectionCallback connectionCallback;

    public static SmoothBluetooth getmSmoothBluetooth() {
        return mSmoothBluetooth;
    }

    public static SmoothBluetooth.Listener getmSmoothBluetoothListener() {
        return mSmoothBluetoothListener;
    }

    public static void setmSmoothBluetoothListener(SmoothBluetooth.Listener mSmoothBluetoothListener) {
        BluetoothCommandsUtils.mSmoothBluetoothListener = mSmoothBluetoothListener;
    }

    public static SmoothBluetooth.ConnectionCallback getConnectionCallback() {
        return connectionCallback;
    }

    public static void setConnectionCallback(SmoothBluetooth.ConnectionCallback connectionCallback) {
        BluetoothCommandsUtils.connectionCallback = connectionCallback;
    }


    public static SmoothBluetooth initSmoothBluetooth(Context ctx, SmoothBluetoothListenerDiscoverDevices listenerCustom) {
        if (mSmoothBluetooth == null) {
            mSmoothBluetoothListener = listenerCustom;
            mSmoothBluetooth = new SmoothBluetooth(ctx, SmoothBluetooth.ConnectionTo.OTHER_DEVICE, SmoothBluetooth.Connection.INSECURE, mSmoothBluetoothListener);
        }
        return mSmoothBluetooth;
    }
}
