package com.ppp.esir.projetvelo.utils;

import android.app.Application;
import android.util.Log;

/**
 * Created by thoma on 02/05/2016.
 */
public class ProjetVeloApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getName(), "onCreate Rennes1Tools_Application");
        Datacontainer.setContext(this);
    }
}
