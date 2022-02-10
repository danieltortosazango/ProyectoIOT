package com.example.daferfus_upv.btle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.daferfus_upv.btle.AcercaDe.InstruccionesActivity;
import com.example.daferfus_upv.btle.Activities.AdminActivity;
import com.example.daferfus_upv.btle.Activities.LoginActivity;
import com.example.daferfus_upv.btle.Activities.MainActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
import static com.example.daferfus_upv.btle.ConstantesAplicacion.URL_ACTUALIZAR_DATOS_PERFIL;

public class Perfil extends AppCompatActivity {

    // --------------------------------------------------------------
    TextView tvNombrePerfil;
    TextView tvEmailPerfil;
    EditText tvContrasenyaPerfil;
    EditText tvCiudadPerfil;
    // --------------------------------------------------------------
    Button btEditarPerfil;
    Boolean editando = true;
    Boolean ojeando = false;

    ImageView ojoPerfil;
    ImageView uk;
    ImageView sp;
    ImageView vl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_activity);
        tvNombrePerfil = findViewById(R.id.tvNombrePerfil);
        tvEmailPerfil = findViewById(R.id.tvCorreoPerfil);
        tvContrasenyaPerfil = findViewById(R.id.tvContrasenyaPerfil);
        tvCiudadPerfil = findViewById(R.id.tvCiudadPerfil);
        btEditarPerfil = findViewById(R.id.btEditarPerfil);

        ojoPerfil= findViewById(R.id.ojoPerfil);
        uk= findViewById(R.id.english);
        sp = findViewById(R.id.spanish);
        vl = findViewById(R.id.valencian);
        mostrarUsuario();
        List<String> emailContra = LoginActivity.cargarPreferenciasString(getApplicationContext());
        findViewById(R.id.linearAtras).setOnClickListener(v -> {
            if(!emailContra.get(0).equals("admin@admin.com")) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(i);

            }else{
                Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                finish();
                startActivity(i);

            }
        });
        btEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG", "onClick: " + btEditarPerfil.getText());
                if (!editando) {
                    tvContrasenyaPerfil.setEnabled(false);
                    tvContrasenyaPerfil.setBackgroundColor(Color.TRANSPARENT);
                    tvCiudadPerfil.setEnabled(false);
                    tvCiudadPerfil.setBackgroundColor(Color.TRANSPARENT);
                    btEditarPerfil.setText(R.string.boton_editar_perfil);
                    editando=!editando;
                    actualizarPerfil(URL_ACTUALIZAR_DATOS_PERFIL);
                } else {

                    tvContrasenyaPerfil.setEnabled(true);
                    tvContrasenyaPerfil.setBackgroundResource(R.drawable.input);
                    tvCiudadPerfil.setEnabled(true);
                    tvCiudadPerfil.setBackgroundResource(R.drawable.input);
                    btEditarPerfil.setText(R.string.boton_guardar_perfil);
                    editando = !editando;
                }
            }
        });

        uk.setOnClickListener(view -> {
            setLocale("en");
            recreate();

        });
        sp.setOnClickListener(view -> {
            setLocale("es");
            recreate();

        });
        vl.setOnClickListener(view -> {
            setLocale("ca");
            recreate();

        });
        ojoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ojeando){
                    ojoPerfil.setImageResource(R.drawable.ojo_tachado);
                    tvContrasenyaPerfil.setInputType(TYPE_TEXT_VARIATION_PASSWORD);
                    ojeando=!ojeando;
                }else{
                    ojoPerfil.setImageResource(R.drawable.ojo);
                    tvContrasenyaPerfil.setInputType(TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    ojeando=!ojeando;
                }

            }
        });

    } // onCreate()

    public void mostrarUsuario() {
        List<String> emailContra = LoginActivity.cargarPreferenciasString(getApplicationContext());
        tvNombrePerfil.setText(emailContra.get(2) + " " + emailContra.get(3));
        tvEmailPerfil.setText(emailContra.get(0));
        tvContrasenyaPerfil.setText(emailContra.get(1));
        tvCiudadPerfil.setText(emailContra.get(4));
    }

    public void actualizarPerfil(String URL) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if (!response.isEmpty()) {
                List<String> emailContra = LoginActivity.cargarPreferenciasString(getApplicationContext());
                Log.d("TAG", "ACTUALIZARPERFIL:  "+response);
                Toast.makeText(this, "Datos cambiados correctamente", Toast.LENGTH_SHORT).show();
                guardarPreferencias(emailContra.get(2),emailContra.get(3),tvCiudadPerfil.getText().toString());
            } else {
                Toast.makeText(Perfil.this, R.string.textoContrasenyaOEmailIncorrecto, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(Perfil.this, error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idUsuario", tvEmailPerfil.getText().toString());
                parametros.put("contrasenya", tvContrasenyaPerfil.getText().toString());
                parametros.put("Ciudad", tvCiudadPerfil.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //Guarda en el archivo credenciales el email y conrasenya
    public void guardarPreferencias( String usuario, String apellido, String ciudad) {
        SharedPreferences preferences = getSharedPreferences("Credenciales", MODE_PRIVATE);



        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", tvEmailPerfil.getText().toString());
        editor.putString("contrasenya",  tvContrasenyaPerfil.getText().toString());
        editor.putString("usuario", usuario);
        editor.putString("apellidos", apellido);
        editor.putString("ciudad", ciudad);
        editor.putBoolean("sesionIniciada", true);
        editor.apply();
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
