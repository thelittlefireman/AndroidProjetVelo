package com.ppp.esir.projetvelo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.ppp.esir.projetvelo.R;
import com.ppp.esir.projetvelo.listeners.SmoothBluetoothListenerControleVelo;
import com.ppp.esir.projetvelo.utils.BluetoothCommandsUtils;
import com.ppp.esir.projetvelo.utils.Datacontainer;
import com.ppp.esir.projetvelo.utils.ProjetVeloCommandsUtils;

public class ControleVeloActivity extends AppCompatActivity {

    private ImageButton buttonMore, buttonLess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_velo);
        Datacontainer.setActivity(this);

        BluetoothCommandsUtils.setmSmoothBluetoothListener(new SmoothBluetoothListenerControleVelo(this));
        buttonLess = (ImageButton) findViewById(R.id.buttonLess);
        buttonMore = (ImageButton) findViewById(R.id.buttonMore);
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
    }
}
