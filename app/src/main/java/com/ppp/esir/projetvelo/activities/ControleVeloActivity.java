package com.ppp.esir.projetvelo.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.ppp.esir.projetvelo.R;
import com.ppp.esir.projetvelo.listeners.MyLocationChangeListener;
import com.ppp.esir.projetvelo.maps.ItineraireTask;
import com.ppp.esir.projetvelo.services.BluetoothLeService;
import com.ppp.esir.projetvelo.utils.BluetoothUtils;
import com.ppp.esir.projetvelo.utils.Datacontainer;
import com.ppp.esir.projetvelo.utils.ProjetVeloCommandsUtils;
import com.ppp.esir.projetvelo.views.IDrawerItemSearchItinerary;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ppp.esir.projetvelo.R.id.iconBattery;

public class ControleVeloActivity extends AppCompatActivity {
    public static boolean mapLock = false;
    private final int BATTERY_ELEMENT = 1;
    private final int SPEED_ELEMENT = 2;
    private final int ASSIST_ELEMENT = 3;
    private final int ERROR = 4;
    private TextView assistanceTextView, speedTextView;
    private SeekBar seekBarSpeed;
    private MaterialIconView iconViewBattery;
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
                if (intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA_RAW) != null) {
                    String str = new String(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA_RAW));
                    Log.i("bronCharacteristicRead", str);
                    Pattern pattern = Pattern.compile("<([0-9],[0-9]+(.[0-9]+)?)>");
                    Matcher matcher = pattern.matcher(str);
                    while (matcher.find()) {
                        String allDATA = matcher.group().replace("<", "").replace(">", "");
                        String element = allDATA.split(",")[0];
                        String data = allDATA.split(",")[1];
                        switch (Integer.valueOf(element)) {
                            case BATTERY_ELEMENT:
                                int batteryLevel = Integer.valueOf(data);
                                if (batteryLevel == 0)
                                    iconViewBattery.setIcon(MaterialDrawableBuilder.IconValue.BATTERY_ALERT);
                                else if (batteryLevel == 25)
                                    iconViewBattery.setIcon(MaterialDrawableBuilder.IconValue.BATTERY_30);
                                else if (batteryLevel == 50)
                                    iconViewBattery.setIcon(MaterialDrawableBuilder.IconValue.BATTERY_50);
                                else if (batteryLevel == 75)
                                    iconViewBattery.setIcon(MaterialDrawableBuilder.IconValue.BATTERY_80);
                                else if (batteryLevel == 100)
                                    iconViewBattery.setIcon(MaterialDrawableBuilder.IconValue.BATTERY);

                                break;
                            case SPEED_ELEMENT:
                                speedTextView.setText(data);
                                break;
                            case ASSIST_ELEMENT:
                                if (data.equals("0"))
                                    seekBarSpeed.setProgress(0);
                                assistanceTextView.setText(data);
                                break;
                            case ERROR:
                                if (!data.equals("0")) {
                                    System.out.println("ERROR : " + data);
                                    Toast.makeText(context, "Erreur du controller : " + getErreur(data), Toast.LENGTH_SHORT).show();
                                }
                                break;

                        }
                    }
                }
            }
        }
    };
    private Timer myTimer;


    private GoogleMap gMap;
    private MaterialIconView buttonMore, buttonLess, pedestrianSpeed, buttonEmergencyStop, buttonDrawer;
    private TextView distanceParcourue, distanceRest, timeRest;
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
    private Drawer drawer;
    private IDrawerItemSearchItinerary iDrawerItemSearchItinerary;

    private String getErreur(String data) {
        switch (data) {
            case "1":
                return "Throttle Signal Abnormality";
            case "3":
                return "Motor Hall Signal Abnormality";
            case "4":
                return "Torque Sensor Signal Abnormality";
            case "5":
                return "Speed Sensor Signal Abnormality (Suitable for torque system)";
            case "6":
                return "Motor or Controller Short Circuit Abnormality";
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_velo);
        Datacontainer.setActivity(this);
        ProjetVeloCommandsUtils.initProjetVeloCommandsUtils(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ((TextView) toolbar.findViewById(R.id.title)).setText("Projet Vélo");
        speedTextView = ((TextView) toolbar.findViewById(R.id.speed));
        iconViewBattery = (MaterialIconView) toolbar.findViewById(iconBattery);
        buttonDrawer = (MaterialIconView) toolbar.findViewById(R.id.buttonDrawer);
        buttonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer();
            }
        });
        setSupportActionBar(toolbar);


        DrawerBuilder drawerBuilder = new DrawerBuilder();

        iDrawerItemSearchItinerary = new IDrawerItemSearchItinerary(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Itineraire
                itineraire(true);
                if (myTimer != null)
                    myTimer.cancel();
                myTimer = new Timer();
                myTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ControleVeloActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                itineraire(false);
                            }
                        });
                    }
                }, 10000, 10000); // initial delay 30 second, interval 30 second
                drawer.closeDrawer();
                View view = ControleVeloActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        //Add DRAWER
        if (Datacontainer.isConnected()) {
            // Create the AccountHeader
            AccountHeader headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withTextColorRes(R.color.colorPrimary)
                    .addProfiles(
                            new ProfileDrawerItem().withName(Datacontainer.getActualUser().getNomPrenom()).withEmail(Datacontainer.getActualUser().getmEmail()).withIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                    )
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            return false;
                        }
                    })
                    .build();

            //Now create your drawer and pass the AccountHeader.Result
            drawerBuilder.withAccountHeader(headerResult);
        }
        drawer = drawerBuilder
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .addDrawerItems(iDrawerItemSearchItinerary, new DividerDrawerItem(), new PrimaryDrawerItem().withName("Déconnexion").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        getmBluetoothLeService().disconnect();
                        finish();
                        return true;
                    }
                })).build();
        //On récupère les composants graphiques
        gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        assistanceTextView = (TextView) findViewById(R.id.assistanceNumber);
        distanceParcourue = (TextView) findViewById(R.id.distanceParcourue);
        distanceRest = (TextView) findViewById(R.id.distanceRest);
        timeRest = (TextView) findViewById(R.id.timeRest);
        buttonLess = (MaterialIconView) findViewById(R.id.buttonLess);
        buttonMore = (MaterialIconView) findViewById(R.id.buttonMore);
        seekBarSpeed = (SeekBar) findViewById(R.id.seekBarSpeed);
        pedestrianSpeed = (MaterialIconView) findViewById(R.id.pedestrianSpeed);
        buttonEmergencyStop = (MaterialIconView) findViewById(R.id.buttonEmergencyStop);

        buttonEmergencyStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjetVeloCommandsUtils.stopAll();
                seekBarSpeed.setProgress(0);
            }
        });
        pedestrianSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjetVeloCommandsUtils.setAssistancePieton();
            }
        });

        seekBarSpeed.setMax(25);
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ProjetVeloCommandsUtils.setPot(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjetVeloCommandsUtils.upAssistance();
            }
        });
        buttonLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjetVeloCommandsUtils.downAssistance();
            }
        });

        gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                ControleVeloActivity.mapLock = true;
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gMap.getMyLocation().getLatitude(), gMap.getMyLocation().getLongitude()), 18));
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                return true;
            }
        });
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ControleVeloActivity.mapLock = false;
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });
        gMap.setMyLocationEnabled(true);
        gMap.setOnMyLocationChangeListener(new MyLocationChangeListener(distanceParcourue, gMap));

        final Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void itineraire(boolean animate) {
        ItineraireTask myTask = new ItineraireTask(ControleVeloActivity.this, gMap, iDrawerItemSearchItinerary.getDepart(), iDrawerItemSearchItinerary.getArrivee(), animate, distanceRest, timeRest);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            myTask.execute();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                seekBarSpeed.setProgress(seekBarSpeed.getProgress() + 5);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                seekBarSpeed.setProgress(seekBarSpeed.getProgress() - 5);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
