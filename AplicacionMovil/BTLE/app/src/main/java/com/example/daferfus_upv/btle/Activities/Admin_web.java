package com.example.daferfus_upv.btle.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.R;

public class Admin_web extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_web);

        Bundle bundle = getIntent().getExtras();
        String seccion = bundle.getString("seccion");
        String url = null;
        if (seccion.equals("Usuarios")) {
            url = ConstantesAplicacion.URL_ADMIN_USUARIOS;
        } else if (seccion.equals("Sensores")) {
            url = ConstantesAplicacion.URL_ADMIN_SENSORES;
        }
        WebView webView = findViewById(R.id.webViewUsuarios);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("Admin_web", "La pagina ha terminado de cargarse");
                //Load HTML
                webView.evaluateJavascript("quitarMenu()",null);
            // document.getElementById('sidebarToggle').style.display='none'
            }
        });
        webView.loadUrl(url);
    }
}