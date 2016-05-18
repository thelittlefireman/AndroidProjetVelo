package com.ppp.esir.projetvelo.utils;

import com.ppp.esir.projetvelo.activities.ControleVeloActivity;

/**
 * Created by thoma on 02/05/2016.
 */
public class ProjetVeloCommandsUtils {
    private static final String begin = "<";
    private static final String end = ">";
    private static ControleVeloActivity activity;

    public static void initProjetVeloCommandsUtils(ControleVeloActivity activityy) {
        activity = activityy;
    }

    private static void sendMessage(String message) {
        activity.getmBluetoothLeService().send(begin + message + end);
    }

    public static void bonjour() {
        sendMessage("bonjour");
    }
    public static void upAssistance() {

    }

    public static void downAssistance() {
        //BluetoothUtils.getmSmoothBluetooth().send("down");
    }

}
