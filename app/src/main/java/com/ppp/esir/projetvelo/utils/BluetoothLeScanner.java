package com.ppp.esir.projetvelo.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BluetoothLeScanner {
    private final Handler mHandler;
    private final BluetoothAdapter.LeScanCallback mLeScanCallback;
    private final BluetoothUtils mBluetoothUtils;
    private final ScanCallback mScanCallback;
    private boolean mScanning;
    private ScanSettings settings;
    private List<ScanFilter> filters;

    public BluetoothLeScanner(final BluetoothAdapter.LeScanCallback leScanCallback, final BluetoothUtils bluetoothUtils, final ScanCallback mScanCallback) {
        mHandler = new Handler();
        mLeScanCallback = leScanCallback;
        mBluetoothUtils = bluetoothUtils;
        this.mScanCallback = mScanCallback;
        settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        filters = new ArrayList<ScanFilter>();
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void scanLeDevice(final int duration, final boolean enable) {
        if (enable) {
            if (mScanning) {
                return;
            }
            Log.d("TAG", "~ Starting Scan");
            // Stops scanning after a pre-defined scan period.
            if (duration > 0) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG", "~ Stopping Scan (timeout)");
                        mScanning = false;
                        if (Build.VERSION.SDK_INT < 21) {
                            mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);
                        } else {
                            mBluetoothUtils.getBluetoothAdapter().getBluetoothLeScanner().stopScan(mScanCallback);

                        }
                        mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);

                    }
                }, 15000);
            }
            mScanning = true;
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothUtils.getBluetoothAdapter().startLeScan(mLeScanCallback);
            } else {
                mBluetoothUtils.getBluetoothAdapter().getBluetoothLeScanner().startScan(filters, settings, mScanCallback);
            }
        } else {
            Log.d("TAG", "~ Stopping Scan");
            mScanning = false;
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);
            } else {
                mBluetoothUtils.getBluetoothAdapter().getBluetoothLeScanner().stopScan(mScanCallback);

            }
        }
    }
}
