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
        sendMessage("2");
        getAssistance();
    }

    public static void downAssistance() {
        sendMessage("3");
        getAssistance();
    }

    public static void setAssistancePieton() {
        sendMessage("5");
        getAssistance();
    }
    public static void setAssistance(int i) {
        if (i >= 0 && i <= 6) {
            sendMessage("4" + "," + String.valueOf(i));
        }
    }

    public static void setPot(int i) {
        int rst = 103 + i * 2;
        if (i == 0) {

            sendMessage("6" + "," + "000");
        } else {
            sendMessage("6" + "," + String.valueOf(rst));

        }
    }
    public static void getAssistance() {
        sendMessage("1");
    }
}
