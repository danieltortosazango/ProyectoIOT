package com.example.daferfus_upv.btle.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.daferfus_upv.btle.Perfil;
import com.example.daferfus_upv.btle.R;

import java.util.List;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity {

    private TextView textViewAdmin;
    Button cerrarSesion;

    CardView cardViewPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);
        textViewAdmin= findViewById(R.id.tvNombreAdmin);
        cerrarSesion= findViewById(R.id.cerrarSesion);
        mostrarUsuario();
        CardView botonUsuarios=findViewById(R.id.cardViewAñadirUsuario);
        botonUsuarios.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), Admin_web.class);
            i.putExtra("seccion", "Usuarios");
            startActivity(i);
        });
        CardView botonSensores=findViewById(R.id.cardViewMediciones);
        botonSensores.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), Admin_web.class);
            i.putExtra("seccion", "Sensores");
            startActivity(i);
        });
        cardViewPerfil=findViewById(R.id.cardViewPerfilAdmin);
        cardViewPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Perfil.class);
                startActivity(i);
            }
        });
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.cerrarSesion(getApplicationContext(), false);
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                finish();
                startActivity(i);

            }
        });
    }

    //muestra el usuario registrado
    @SuppressLint("SetTextI18n")
    public void mostrarUsuario() {
        List<String> emailContra=LoginActivity.cargarPreferenciasString(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        assert language != null;
        textViewAdmin.setText(MainActivity.getLocaleStringResource(new Locale(language), R.string.saludo, this) + " "+emailContra.get(2));
    }
}
