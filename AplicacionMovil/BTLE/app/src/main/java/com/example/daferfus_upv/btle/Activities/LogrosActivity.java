package com.example.daferfus_upv.btle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.POJOS.Logros;
import com.example.daferfus_upv.btle.POJOS.RecyclerViewAdapter;
import com.example.daferfus_upv.btle.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.daferfus_upv.btle.ConstantesAplicacion.URL_CONSULTA_LOGROS;

public class LogrosActivity extends AppCompatActivity {

    private static final String TAG = "Logros";

    private final ArrayList<Logros> mEnProceso = new ArrayList<>();
    private final ArrayList<Logros> mHecho = new ArrayList<>();
    private final ArrayList<Logros> mPorHacer = new ArrayList<>();

    private TextView tvTextoHecho;
    private TextView tvTextoProceso;
    private TextView tvTextoPorHacer;

    private ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(ContentLoadingProgressBar.VISIBLE);
        tvTextoHecho = findViewById(R.id.tvConseguido);
        tvTextoPorHacer = findViewById(R.id.tvPorHacer);
        tvTextoProceso = findViewById(R.id.tvEnProceso);
        getLogros(URL_CONSULTA_LOGROS);

        findViewById(R.id.linearAtras).setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    //----------------------------------------------------------------------------------------------
    //Devuelve los logros creados en la BD y los añade a los cards adecuados
    //----------------------------------------------------------------------------------------------
    public void getLogros(String URL) {
        Log.d(TAG, "-----" + URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            Log.d(TAG, "-----" + response);
            if (!response.isEmpty()) {
                Log.d(TAG, "-----" + response);
                //Usamos el gson de google
                Gson gson = new Gson();
                JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
                for (JsonElement s : jsonArray) {
                    Log.d(TAG, "getLogros: " + s);
                    Logros logro = gson.fromJson(s, Logros.class);
                    Log.d(TAG, "*****" + logro);
                    Log.d(TAG, "*****" + logro.getNombreLogro());
                    rellenarEstados(logro);
                }
                initRecyclerViewEnProceso();
                initRecyclerViewPorHacer();
                initRecyclerViewConseguidos();
            }
        }, error -> Toast.makeText(LogrosActivity.this, error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        progressBar.setVisibility(ContentLoadingProgressBar.GONE);
    }

    public void rellenarEstados(Logros logro) {
        int meta = 0;
        if ("Pasos".equals(logro.getTipo())) {
            meta = ConstantesAplicacion.PASOS;
        } else if ("Distancia".equals(logro.getTipo())) {
            meta = ConstantesAplicacion.DISTANCIA;
        }

        if (meta > 0) {
            if (meta >= logro.dificultadCompletar) {
                mHecho.add(logro);
            } else {
                mEnProceso.add(logro);
            }
        } else {
            mPorHacer.add(logro);
        }
        if (!mHecho.isEmpty()) {
            tvTextoHecho.setVisibility(View.GONE);
        } else {
            tvTextoHecho.setVisibility(View.VISIBLE);
        }
        if (!mPorHacer.isEmpty()) {
            tvTextoPorHacer.setVisibility(View.GONE);
        } else {
            tvTextoPorHacer.setVisibility(View.VISIBLE);
        }
        if (!mEnProceso.isEmpty()) {
            tvTextoProceso.setVisibility(View.GONE);
        } else {
            tvTextoProceso.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, ",,,,," + logro.getDificultadCompletar());
    }

    private void initRecyclerViewEnProceso() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewHorizontal = findViewById(R.id.recyclerViewEnProceso);
        recyclerViewHorizontal.setLayoutManager(layoutManagerHorizontal);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mEnProceso);
        recyclerViewHorizontal.setAdapter(adapter);
    }

    private void initRecyclerViewPorHacer() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManagerVertical = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewVertical = findViewById(R.id.recyclerViewPorHacer);
        recyclerViewVertical.setLayoutManager(layoutManagerVertical);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mPorHacer);
        recyclerViewVertical.setAdapter(adapter);
    }


    private void initRecyclerViewConseguidos() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManagerVertical = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewVertical = findViewById(R.id.recyclerViewConseguidos);
        recyclerViewVertical.setLayoutManager(layoutManagerVertical);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mHecho);
        recyclerViewVertical.setAdapter(adapter);
    }
}