package com.ppp.esir.projetvelo.listeners;

import android.content.Intent;

import com.ppp.esir.projetvelo.activities.BluetoothActivity;
import com.ppp.esir.projetvelo.activities.ControleVeloActivity;

import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

/**
 * Created by thoma on 02/05/2016.
 */
public class SmoothBluetoothListenerDiscoverDevices implements SmoothBluetooth.Listener {
 private BluetoothActivity bluetoothActivity;

 public SmoothBluetoothListenerDiscoverDevices(BluetoothActivity bluetoothActivity) {
  this.bluetoothActivity = bluetoothActivity;
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
     this.bluetoothActivity.startActivity(new Intent(this.bluetoothActivity, ControleVeloActivity.class));
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(Device device) {

    }

    @Override
    public void onDiscoveryStarted() {
     this.bluetoothActivity.getRefreshHelper().startLoading();
    }

    @Override
    public void onDiscoveryFinished() {
     this.bluetoothActivity.getRefreshHelper().stopLoading();
    }

    @Override
    public void onNoDevicesFound() {

    }

    @Override
    public void onDevicesFound(List<Device> deviceList, SmoothBluetooth.ConnectionCallback connectionCallback) {
     /*   BluetoothUtils.setConnectionCallback(connectionCallback);
        this.bluetoothActivity.getRecyclerViewAdapterListBuetoothDevices().getDeviceList().clear();
        this.bluetoothActivity.getRecyclerViewAdapterListBuetoothDevices().getDeviceList().addAll(deviceList);
        this.bluetoothActivity.getRecyclerViewAdapterListBuetoothDevices().notifyDataSetChanged();*/
    }

    @Override
    public void onDataReceived(int data) {

    }
}
