package com.lexot.cenicafe;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AcquisitionActivity extends AppCompatActivity {



    AudioManager audiomanager;
    Context contextual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquisition);

        //TODO: Activar modo silencioso

    }

    @Override
    protected void onDestroy() {

        //TODO: Destruir modo silencioso
        super.onDestroy();

    }
}
