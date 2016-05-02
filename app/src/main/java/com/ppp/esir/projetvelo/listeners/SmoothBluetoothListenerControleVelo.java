package com.ppp.esir.projetvelo.listeners;

import com.ppp.esir.projetvelo.activities.ControleVeloActivity;

import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

/**
 * Created by thoma on 02/05/2016.
 */
public class SmoothBluetoothListenerControleVelo implements SmoothBluetooth.Listener {
    private ControleVeloActivity controleVeloActivity;

    public SmoothBluetoothListenerControleVelo(ControleVeloActivity controleVeloActivity) {
        this.controleVeloActivity = controleVeloActivity;
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

    }

    @Override
    public void onDataReceived(int data) {

    }
}
