package com.example.daferfus_upv.btle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daferfus_upv.btle.Activities.LoginActivity;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    //Variables animaciones
    Animation animArriba, animAbajo;
    ImageView imagen;
    TextView eslogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadLocale();
        setContentView(R.layout.splash_screen);

        //Animaciones
        animArriba = AnimationUtils.loadAnimation(this, R.anim.arriba_splash_anim);
        animAbajo = AnimationUtils.loadAnimation(this, R.anim.abajo_splash_anim);

        //Asignación
        imagen = findViewById(R.id.imagenSplash);
        eslogan = findViewById(R.id.textViewSplash);

        imagen.setAnimation(animArriba);
        eslogan.setAnimation(animAbajo);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        thread.start();
    }
    //cambia el idioma
    private void setLocale(String leng){
        Locale locale = new Locale(leng);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //guardamos los cambios en SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang", leng);
        editor.apply();
    }
    //carga el idioma
    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }
}
