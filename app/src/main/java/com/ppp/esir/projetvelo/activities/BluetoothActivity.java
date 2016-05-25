package com.ppp.esir.projetvelo.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ppp.esir.projetvelo.R;
import com.ppp.esir.projetvelo.adapters.RecyclerViewAdapterListBuetoothDevices;
import com.ppp.esir.projetvelo.services.BluetoothLeService;
import com.ppp.esir.projetvelo.utils.BluetoothLeDeviceStore;
import com.ppp.esir.projetvelo.utils.BluetoothLeScanner;
import com.ppp.esir.projetvelo.utils.BluetoothUtils;
import com.ppp.esir.projetvelo.utils.Datacontainer;
import com.refresh.menuitem.RefreshMenuItemHelper;

import java.util.List;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.easycursor.objectcursor.EasyObjectCursor;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothUtils mBluetoothUtils;
    private BluetoothLeScanner mScanner;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                startActivity(new Intent(context, ControleVeloActivity.class));
                mScanner.scanLeDevice(-1, false);
                finish();
                Log.i(this.getClass().getName(), "bluetooth connected");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i(this.getClass().getName(), "ACTION_GATT_DISCONNECTED");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.i(this.getClass().getName(), "ACTION_GATT_SERVICES_DISCOVERED");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.i(this.getClass().getName(), "ACTION_DATA_AVAILABLE");
            }
        }
    };
    private RecyclerViewAdapterListBuetoothDevices mLeDeviceListAdapter;
    private BluetoothLeDeviceStore mDeviceStore;
    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {

            final BluetoothLeDevice deviceLe = new BluetoothLeDevice(device, rssi, scanRecord, System.currentTimeMillis());
            mDeviceStore.addDevice(deviceLe);
            final EasyObjectCursor<BluetoothLeDevice> c = mDeviceStore.getDeviceCursor();
            Log.i(this.getClass().getName(), "new device" + deviceLe.getName() + deviceLe.getAddress());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.swapCursor(c);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
    private BluetoothLeDevice previousBluetooth = null;
    private final ScanCallback mNormalScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            final BluetoothLeDevice deviceLe = new BluetoothLeDevice(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes(), System.currentTimeMillis());
            if (previousBluetooth == null) {
                previousBluetooth = deviceLe;
            }
            if (deviceLe.getAddress() != previousBluetooth.getAddress()) {
                previousBluetooth = deviceLe;

                mDeviceStore.addDevice(deviceLe);
                final EasyObjectCursor<BluetoothLeDevice> c = mDeviceStore.getDeviceCursor();
                Log.i(this.getClass().getName(), "lnew device" + deviceLe.getName() + deviceLe.getAddress());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLeDeviceListAdapter.swapCursor(c);
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        /**
         * Callback when batch results are delivered.
         *
         * @param results List of scan results that are previously scanned.
         */
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.i(this.getClass().getName(), "ee");
        }

        /**
         * Callback when scan could not be started.
         *
         * @param errorCode Error code (one of SCAN_FAILED_*) for scan failure.
         */
        @Override
        public void onScanFailed(int errorCode) {
            Log.i(this.getClass().getName(), "failed" + errorCode);
        }

    };
    private BluetoothLeService mBluetoothLeService;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName componentName, final IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(this.getClass().getName(), "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            // mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(final ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private RecyclerView recyclerViewListBluetoothDevices;
    private RefreshMenuItemHelper refreshHelper;

    public BluetoothActivity() {
    }

    public BluetoothLeService getmBluetoothLeService() {
        return mBluetoothLeService;
    }

    public RefreshMenuItemHelper getRefreshHelper() {
        return refreshHelper;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Datacontainer.setActivity(this);

        refreshHelper = new RefreshMenuItemHelper();
        mDeviceStore = new BluetoothLeDeviceStore();
        mBluetoothUtils = new BluetoothUtils(this);
        mScanner = new BluetoothLeScanner(mLeScanCallback, mBluetoothUtils, mNormalScanCallback);

        mLeDeviceListAdapter = new RecyclerViewAdapterListBuetoothDevices(this, mDeviceStore.getDeviceCursor());
        recyclerViewListBluetoothDevices = (RecyclerView) findViewById(R.id.recyclerViewListBluetoothDevices);
        recyclerViewListBluetoothDevices.setAdapter(mLeDeviceListAdapter);

        recyclerViewListBluetoothDevices.setLayoutManager(new LinearLayoutManager(this));
        final Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        startScan();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
        if (!mScanner.isScanning()) {
            refreshHelper.stopLoading();
        } else {
            refreshHelper.startLoading();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshHelper.setMenuItem(item);
                startScan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, BluetoothUtils.makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            // final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            //   Log.d(this.getClass().getName(), "Connect request result=" + result);
        }
        invalidateOptionsMenu();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == BluetoothUtils.REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //init();
            }
        }
    }

    public void stopScan() {
        mScanner.scanLeDevice(-1, false);
    }
    private void startScan() {
        refreshHelper.startLoading();
        final boolean mIsBluetoothOn = mBluetoothUtils.isBluetoothOn();
        final boolean mIsBluetoothLePresent = mBluetoothUtils.isBluetoothLeSupported();
        mDeviceStore.clear();

        mLeDeviceListAdapter = new RecyclerViewAdapterListBuetoothDevices(this, mDeviceStore.getDeviceCursor());
        recyclerViewListBluetoothDevices.setAdapter(mLeDeviceListAdapter);

        mBluetoothUtils.askUserToEnableBluetoothIfNeeded();
        if (mIsBluetoothOn && mIsBluetoothLePresent) {
            mScanner.scanLeDevice(-1, true);
            invalidateOptionsMenu();
        }
    }
}
