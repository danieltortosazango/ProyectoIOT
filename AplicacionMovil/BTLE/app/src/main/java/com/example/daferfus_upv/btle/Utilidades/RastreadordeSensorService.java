package com.example.daferfus_upv.btle.Utilidades;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.daferfus_upv.btle.Activities.MainActivity;
import com.example.daferfus_upv.btle.Workers.MantenimientoDeMedidasWorker;

import static android.content.ContentValues.TAG;

public class RastreadordeSensorService extends Service {
    Handler handler;
    public RastreadordeSensorService() {
        handler = new Handler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado...");

        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(MainActivity.aSwitch.isChecked())
                {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        double metros = TratamientoDeLecturas.recogerMetros();
                        Log.d("RastreadordeSensorService", "Rastreando...:" + metros + " metros");
                        MainActivity.imageCarga.requestLayout();
                        if (metros >= 0.1 && metros < 1) {
                            MainActivity.imageCarga.getLayoutParams().width = 600;
                            MainActivity.textoSeguimiento.setText("El sensor esta cerca tuya. Concretamente esta a " + metros + " metros.");
                        } else if (metros == 1) {
                            MainActivity.imageCarga.getLayoutParams().width = 400;
                            MainActivity.textoSeguimiento.setText("El sensor se encuentra en tu zona. Concretamente esta a " + metros + " metros");
                        }  else if (metros == 2) {
                            MainActivity.imageCarga.getLayoutParams().width = 200;
                            MainActivity.textoSeguimiento.setText("El sensor esta lejos. El sensor esta a unos " + metros + " metros.");
                        }
                        else{
                            MainActivity.imageCarga.getLayoutParams().width = 0;
                            MainActivity.textoSeguimiento.setText("No se deteca el sensor. Te informare cuando se capte algo.");
                        }
                    });
                }
            }
        }).start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Servicio destruido...");
    }
}
