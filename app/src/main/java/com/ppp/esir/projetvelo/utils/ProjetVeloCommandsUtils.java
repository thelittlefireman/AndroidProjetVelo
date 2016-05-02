package com.ppp.esir.projetvelo.utils;

/**
 * Created by thoma on 02/05/2016.
 */
public class ProjetVeloCommandsUtils {

    public static void upAssistance() {
        BluetoothCommandsUtils.getmSmoothBluetooth().send("up");
    }

    public static void downAssistance() {
        BluetoothCommandsUtils.getmSmoothBluetooth().send("down");
    }

}
