package com.ppp.esir.projetvelo.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ppp.esir.projetvelo.R;
import com.ppp.esir.projetvelo.adapters.RecyclerViewAdapterListBuetoothDevices;
import com.ppp.esir.projetvelo.listeners.SmoothBluetoothListenerDiscoverDevices;
import com.ppp.esir.projetvelo.utils.BluetoothCommandsUtils;
import com.ppp.esir.projetvelo.utils.Datacontainer;

public class BluetoothActivity extends AppCompatActivity {


    private final int REQUEST_BLUETOOTH = 12;
    private RecyclerView recyclerViewListBluetoothDevices;
    private RecyclerViewAdapterListBuetoothDevices recyclerViewAdapterListBuetoothDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Datacontainer.setActivity(this);
        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        //Enable bluetooth
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            if (!BTAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            } else {
                init();
            }

        }
    }

    public void init() {
        recyclerViewAdapterListBuetoothDevices = new RecyclerViewAdapterListBuetoothDevices();
        BluetoothCommandsUtils.initSmoothBluetooth(this, new SmoothBluetoothListenerDiscoverDevices(recyclerViewAdapterListBuetoothDevices));
        recyclerViewListBluetoothDevices = (RecyclerView) findViewById(R.id.recyclerViewListBluetoothDevices);

        recyclerViewListBluetoothDevices.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewListBluetoothDevices.setAdapter(recyclerViewAdapterListBuetoothDevices);

        BluetoothCommandsUtils.getmSmoothBluetooth().tryConnection();
        BluetoothCommandsUtils.getmSmoothBluetooth().doDiscovery();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mSmoothBluetooth.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_BLUETOOTH) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                init();
            }
        }
    }
}
