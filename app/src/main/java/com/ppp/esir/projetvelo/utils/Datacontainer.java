package com.ppp.esir.projetvelo.utils;

import android.app.Activity;
import android.content.Context;

/**
 * Created by thoma on 02/05/2016.
 */
public class Datacontainer {
    private static Activity activity;
    private static Context context;

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Datacontainer.activity = activity;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        context = context;
    }


}
