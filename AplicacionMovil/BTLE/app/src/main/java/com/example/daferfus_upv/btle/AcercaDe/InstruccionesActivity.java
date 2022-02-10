package com.example.daferfus_upv.btle.AcercaDe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.daferfus_upv.btle.Activities.AdminActivity;
import com.example.daferfus_upv.btle.Activities.InvitadoActivity;
import com.example.daferfus_upv.btle.Activities.LoginActivity;
import com.example.daferfus_upv.btle.Activities.MainActivity;
import com.example.daferfus_upv.btle.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InstruccionesActivity extends AppCompatActivity {

    private ViewPager screenPager;
    InstruccionesViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Que la actividad ocupe toda la pantalla
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_instrucciones);

        //Escondemos la action bar si es necesario
        //getSupportActionBar().hide();

        //Iniciamos las vistas
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.acercade_anim_boton);
        tvSkip = findViewById(R.id.tv_skip);

        //Llenamos las interfaces
        final List<InterfazPantalla> mList = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        mList.add(new InterfazPantalla(MainActivity.getLocaleStringResource(new Locale(language), R.string.controlaTusalud, this),MainActivity.getLocaleStringResource(new Locale(language), R.string.saludMuchoTexto, this),R.drawable.img_1));
        mList.add(new InterfazPantalla(MainActivity.getLocaleStringResource(new Locale(language), R.string.multiplesOpciones, this),MainActivity.getLocaleStringResource(new Locale(language), R.string.OpcionesMuchoTexto, this),R.drawable.img_2));
        mList.add(new InterfazPantalla(MainActivity.getLocaleStringResource(new Locale(language), R.string.monitoriza, this),MainActivity.getLocaleStringResource(new Locale(language), R.string.LogrosMuchoTexto, this),R.drawable.img_4));
        mList.add(new InterfazPantalla(MainActivity.getLocaleStringResource(new Locale(language), R.string.mapaDecont, this), MainActivity.getLocaleStringResource(new Locale(language), R.string.ContaminacionMuchoTexto, this),R.drawable.img_3));

        //ViewPager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new InstruccionesViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //Tablayout con Viewpager
        tabIndicator.setupWithViewPager(screenPager);

        //Botón Siguiente (Listener)
        btnNext.setOnClickListener(v -> {

            position = screenPager.getCurrentItem();
            if (position < mList.size()) {

                position++;
                screenPager.setCurrentItem(position);
            }

            if (position == mList.size()-1) { //Cuando llegamos a la última pantalla

                //Mostramos botón empezar y escondemos el indicador y el botón de siguiente
                loaddLastScreen();
            }
        });

        //Tablayout cambia a Listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    loaddLastScreen();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        List<String> emailContra = LoginActivity.cargarPreferenciasString(getApplicationContext());
        //Botón empezamos
        btnGetStarted.setOnClickListener(v -> {

                if(LoginActivity.devolverEstadoUsuario()){
                    //Abrimos Invitado
                    Intent inivitadoActivity = new Intent(getApplicationContext(), InvitadoActivity.class);

                    startActivity(inivitadoActivity);
                }else {
                    //Abrimos MainActivity
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(mainActivity);
                }
            finish();

        });

        //Botón Saltar
        tvSkip.setOnClickListener(v -> screenPager.setCurrentItem(mList.size()));
    }


    // Mostramos el botón Empezar y escondemos el indicador y el botón de Siguiente
    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        //Lanzamos animación
        btnGetStarted.setAnimation(btnAnim);
    }
}

