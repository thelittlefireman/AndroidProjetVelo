package com.ppp.esir.projetvelo.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.ppp.esir.projetvelo.R;
import com.ppp.esir.projetvelo.services.BluetoothLeService;
import com.ppp.esir.projetvelo.utils.BluetoothUtils;
import com.ppp.esir.projetvelo.utils.Datacontainer;
import com.ppp.esir.projetvelo.utils.ProjetVeloCommandsUtils;

public class ControleVeloActivity extends AppCompatActivity {

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.i(this.getClass().getName(), "bluetooth connecter");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i(this.getClass().getName(), "ACTION_GATT_DISCONNECTED");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.i(this.getClass().getName(), "ACTION_GATT_SERVICES_DISCOVERED");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.i(this.getClass().getName(), "ACTION_DATA_AVAILABLE");
            }
        }
    };
    private ImageButton buttonMore, buttonLess;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_velo);
        Datacontainer.setActivity(this);

        // BluetoothUtils.setmSmoothBluetoothListener(new SmoothBluetoothListenerControleVelo(this));
        buttonLess = (ImageButton) findViewById(R.id.buttonLess);
        buttonMore = (ImageButton) findViewById(R.id.buttonMore);
        ProjetVeloCommandsUtils.initProjetVeloCommandsUtils(this);
        buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjetVeloCommandsUtils.bonjour();
            }
        });
        buttonLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjetVeloCommandsUtils.bonjour();
            }
        });

        final Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
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

    public BluetoothLeService getmBluetoothLeService() {
        return mBluetoothLeService;
    }
}
