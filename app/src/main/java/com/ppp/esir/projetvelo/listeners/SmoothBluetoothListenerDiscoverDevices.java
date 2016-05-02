package com.ppp.esir.projetvelo.listeners;

import android.content.Intent;

import com.ppp.esir.projetvelo.activities.ControleVeloActivity;
import com.ppp.esir.projetvelo.adapters.RecyclerViewAdapterListBuetoothDevices;
import com.ppp.esir.projetvelo.utils.Datacontainer;

import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

/**
 * Created by thoma on 02/05/2016.
 */
public class SmoothBluetoothListenerDiscoverDevices implements SmoothBluetooth.Listener {
    private RecyclerViewAdapterListBuetoothDevices recyclerViewAdapterListBuetoothDevices;

    public SmoothBluetoothListenerDiscoverDevices(RecyclerViewAdapterListBuetoothDevices recyclerViewAdapterListBuetoothDevices) {
        this.recyclerViewAdapterListBuetoothDevices = recyclerViewAdapterListBuetoothDevices;
    }

    @Override
    public void onBluetoothNotSupported() {

    }

    @Override
    public void onBluetoothNotEnabled() {

    }

    @Override
    public void onConnecting(Device device) {

    }

    @Override
    public void onConnected(Device device) {
        Datacontainer.getActivity().startActivity(new Intent(Datacontainer.getActivity(), ControleVeloActivity.class));
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(Device device) {

    }

    @Override
    public void onDiscoveryStarted() {

    }

    @Override
    public void onDiscoveryFinished() {

    }

    @Override
    public void onNoDevicesFound() {

    }

    @Override
    public void onDevicesFound(List<Device> deviceList, SmoothBluetooth.ConnectionCallback connectionCallback) {
        this.recyclerViewAdapterListBuetoothDevices.getDeviceList().clear();
        this.recyclerViewAdapterListBuetoothDevices.getDeviceList().addAll(deviceList);
        this.recyclerViewAdapterListBuetoothDevices.notifyDataSetChanged();
    }

    @Override
    public void onDataReceived(int data) {

    }
}
