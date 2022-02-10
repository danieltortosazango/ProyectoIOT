// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-18
// ----------------------------------------------------------


package com.example.daferfus_upv.btle.Activities;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.daferfus_upv.btle.AcercaDe.InstruccionesActivity;
import com.example.daferfus_upv.btle.BD.Logica;
import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.MyApplication;
import com.example.daferfus_upv.btle.POJOS.Lecturas;
import com.example.daferfus_upv.btle.PaginaGraficas;
import com.example.daferfus_upv.btle.Perfil;
import com.example.daferfus_upv.btle.R;
import com.example.daferfus_upv.btle.Utilidades.LecturaCallback;
import com.example.daferfus_upv.btle.Utilidades.RastreadordeSensorService;
import com.example.daferfus_upv.btle.Workers.CronWorker;
import com.example.daferfus_upv.btle.Workers.EscaneadoWorker;
import com.example.daferfus_upv.btle.Workers.GeolocalizacionWorker;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.example.daferfus_upv.btle.ConstantesAplicacion.DESVIACION;
import static com.example.daferfus_upv.btle.ConstantesAplicacion.ID_USUARIO;
import static com.example.daferfus_upv.btle.MyApplication.getAppContext;


// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {

    // ------------------------------------------------------------------
    // Concesión de Permisos
    // ------------------------------------------------------------------
    TextView textViewUsuario;
    @SuppressLint("StaticFieldLeak")
    public static TextView textViewvalorSO2;
    @SuppressLint("StaticFieldLeak")
    public static TextView valorMedia;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar porcentajeCont;
    //Radar PopUp
    private boolean rastreandoSensor = false;
    public static ImageView imageCarga;
    public static TextView textoSeguimiento;
    public static Switch aSwitch;
    Dialog myDialog;
    // --------------------------------------------------------------
    //              solicitarPermiso() <-
    //
    // Invocado desde: onCreate()
    // Función: Solicita permiso al usuario para emplear la funcionalidad de localización
    public static void solicitarPermiso(final String permiso, String
            justificacion, final int codigoPeticion, final Activity actividad) {
        Log.d("Permisos: ", "Preparando permisos");
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)){
            new android.app.AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", (dialog, whichButton) -> ActivityCompat.requestPermissions(actividad,
                            new String[]{permiso}, codigoPeticion))
                    .show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, codigoPeticion);
        }
    }

    // --------------------------------------------------------------
    //              onRequestPermissionsResult() ->
    //              <- N, Texto[], N[]
    //
    // Invocado desde: getPosicionGPS()
    // Función: Gestiona la concesión de permisos.
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int codigoPeticion, @NonNull String[] permisos, @NonNull int[] resultadosConcesion) {
        super.onRequestPermissionsResult(codigoPeticion, permisos, resultadosConcesion);
        // Si la petición es de permisos de geolocalización...
        if (codigoPeticion == 1000) {
            // ...y el usuario ha dado permiso...
            if (resultadosConcesion.length > 0
                    && resultadosConcesion[0] == PackageManager.PERMISSION_GRANTED) {
                // ...se crea una tarea exclusivamente dedicada a la Geolocalización.
                WorkRequest geolocalizacionWorkRequest =
                        new OneTimeWorkRequest.Builder(GeolocalizacionWorker.class)
                                .build();
                WorkManager
                        .getInstance()
                        .enqueue(geolocalizacionWorkRequest);
                // En caso contario...
            } else {
                // ...recuerda al usuario de que no ha dado permiso a la aplicación para conceder
                // sus servicios.
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // --------------------------------------------------------------
    //              onActivityResult() <-
    //              <- N, N, Intent
    //
    // Invocado desde: onCreate()
    // Función: Activa GPS tras conceder los permisos
    // --------------------------------------------------------------
    @Override
    protected void onActivityResult(int codigoPeticion, int codigoResultado, Intent datos) {
        super.onActivityResult(codigoPeticion, codigoResultado, datos);
        if (codigoResultado == Activity.RESULT_OK) {
            if (codigoResultado == ConstantesAplicacion.PETICION_GPS) {
                GeolocalizacionWorker.estaGPSActivo = true; // Se activa GPS antes de coger localización.
            } // if()
        } // if()
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        //Guardamos paen preferencias para ver sia es la primera vez que entra el usuario y mostrarle el manual
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {

            startActivity(new Intent(MainActivity.this, InstruccionesActivity.class));
            //Muestra primer arranque
            //Toast.makeText(InvitadoActivity.this, "First Run", Toast.LENGTH_LONG).show();
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();


        //Creamos nuevo Dialog para mostrar el radar
        myDialog = new Dialog(this);


        //Mostramos el valor del SO2 en el panel principal
        textViewvalorSO2 = findViewById(R.id.valorSO2);

        // Se activa el adaptador Bluetooth
        BluetoothAdapter adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!adaptadorBluetooth.isEnabled()) {
            Intent intentActivarBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            int PETICION_ACTIVAR_BT = 1;
            startActivityForResult(intentActivarBt, PETICION_ACTIVAR_BT);
        }

        // Se comprueba el estado de la red para meter en el servidor los datos no sincronizados.
        //registerReceiver(new ComprobadorEstadoRed(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Se comprueban los permisos de geolocalización.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permisos: ", "Pidiendo permisos");
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso localización no puedo ubicar tus" +
                            " lecturas.", ConstantesAplicacion.PETICION_LOCALIZACION, this);
        }

        Log.d("MainActivity", ID_USUARIO);
        // Se empieza con la ejecución automática de la búsqueda del dispositivo del usuario.
        if(!ID_USUARIO.matches("invitado")){
            Logica logica = new Logica(getApplicationContext());
            logica.borrar();
            Log.d("MainActivity", "usuario");

            WorkRequest medicionesWorkRequest =
                    new OneTimeWorkRequest.Builder(EscaneadoWorker.class)
                            .build();
            WorkManager
                    .getInstance()
                    .enqueue(medicionesWorkRequest);

           WorkRequest cronWorkRequest =
                    new PeriodicWorkRequest.Builder(CronWorker.class, 1, TimeUnit.HOURS)
                            // Constraints
                            .build();
            WorkManager
                    .getInstance()
                    .enqueue(cronWorkRequest);


        Logica.consultarDesviacion(ID_USUARIO, new LecturaCallback() {
            @Override
            public void hacerScrapping() {

            }

            @Override
            public void crearCopiaDeSeguridad() {

            }

            @Override
            public void cogerDesviacion(String desviacion) {
                Log.d("MainActivity", desviacion);
                DESVIACION = Integer.parseInt(desviacion);
            }

            @Override
            public void actualizarDesviacion(String valorLecturaEstacion, String valorLectura) {

            }

            @Override
            public void onFailure() {

            }
        });
        }
        textViewUsuario= findViewById(R.id.textViewMostrarUsuario);
        mostrarUsuario();


//      MENU FLOTANTE
        final FloatingActionsMenu menuBotones = findViewById(R.id.grupofab);
        menuBotones.setScaleX(0);
        menuBotones.setScaleY(0);

        //aplicamos un efecto de entrada
        final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                android.R.interpolator.fast_out_slow_in);

        menuBotones.animate()
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(interpolador)
                .setDuration(600)
                .setStartDelay(500)
        ;
        //Llamamos a los botones
        final FloatingActionButton fab1 = findViewById(R.id.fab1);
        final FloatingActionButton fab2 = findViewById(R.id.fab2);
        final FloatingActionButton fab3 = findViewById(R.id.fab3);
        final FloatingActionButton fab4 = findViewById(R.id.fab4);

        //cerrar sesion
        fab1.setOnClickListener(view -> {
            LoginActivity.cerrarSesion(getApplicationContext(), false);
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
            menuBotones.collapse();

        });
        //acerca de
        fab2.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), InstruccionesActivity.class);

            startActivity(i);
            menuBotones.collapse();
        });
        //perfil
        fab3.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), Perfil.class);

            startActivity(i);
            menuBotones.collapse();

        });
        //Radar
        fab4.setOnClickListener(view -> {
            //Elementos PopUP
            TextView txtclose;
            ImageView iconoRadar;
            ImageView iconoStop;
            myDialog.setContentView(R.layout.buscarsensorpopup);
            imageCarga = myDialog.findViewById(R.id.imageCarga);
            aSwitch = myDialog.findViewById(R.id.Switch);
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", ""+isChecked);
                    if(isChecked){
                        Log.v("Switch State=", "Checkado");
                        startService(new Intent(MainActivity.this, RastreadordeSensorService.class));
                    }
                    else{
                        stopService(new Intent(MainActivity.this, RastreadordeSensorService.class));
                    }
                }
            });
            aSwitch.setChecked(false);
            txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
            textoSeguimiento = myDialog.findViewById(R.id.textoSeguimiento);
            iconoRadar =(ImageView) myDialog.findViewById(R.id.iconoRadar);
            iconoStop =(ImageView) myDialog.findViewById(R.id.iconoStop);
            txtclose.setText("X");
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            iconoRadar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    incioRadar();
                    iconoStop.setVisibility(View.VISIBLE);
                    rastreandoSensor = !rastreandoSensor;
                }
            });

            iconoStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iconoStop.setVisibility(View.GONE);
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();

            menuBotones.collapse();

        });


//      CARDVIEW RECORRIDO
        CardView cardViewRecorrido = findViewById(R.id.cardViewRecorrido); //Creamos cardview y le asignamos un valor

        cardViewRecorrido.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), RecorridoActivity.class);

            startActivity(i);
        });
//      CARDVIEW MEDICIONES
        CardView cardViewMediciones = findViewById(R.id.cardViewMediciones);

        cardViewMediciones.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PaginaGraficas.class);

            startActivity(i);
        });
//      CARDVIEW LOGROS
        CardView cardViewLogros = findViewById(R.id.cardViewLogros);

        cardViewLogros.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), LogrosActivity.class);

            startActivity(i);
        });
//      CARDVIEW CONSEJOS
        CardView cardViewConsejos = findViewById(R.id.cardViewConsejos);

        cardViewConsejos.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ConsejosActivity.class);

            startActivity(i);
        });
        //      CARDVIEW MAPA
        CardView cardViewMapa = findViewById(R.id.cardView7); // creating a CardView and assigning a value.

        cardViewMapa.setOnClickListener(v -> {
            // do whatever you want to do on click (to launch any fragment or activity you need to put intent here.)
            Intent i = new Intent(getApplicationContext(), MapaActivity.class);
            startActivity(i);
        });


    } // onCreate()
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
    //muestra el usuario registrado
    @SuppressLint("SetTextI18n")
    public void mostrarUsuario() {
        List<String> emailContra=LoginActivity.cargarPreferenciasString(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        textViewUsuario.setText(getLocaleStringResource(new Locale(language), R.string.saludo, this) + " "+emailContra.get(2));
    }
    //muestra Toast
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void incioRadar(){
        mostrarToast("Empezamos la búsqueda");
        aSwitch.setChecked(!aSwitch.isChecked());
    }
    //obtiene el string del idioma actual
    public static String getLocaleStringResource(Locale requestedLocale, int resourceId, Context context) {
        String result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // use latest api
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.setLocale(requestedLocale);
            result = context.createConfigurationContext(config).getText(resourceId).toString();
        }
        else { // support older android versions
            Resources resources = context.getResources();
            Configuration conf = resources.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = requestedLocale;
            resources.updateConfiguration(conf, null);

            // retrieve resources from desired locale
            result = resources.getString(resourceId);

            // restore original locale
            conf.locale = savedLocale;
            resources.updateConfiguration(conf, null);
        }

        return result;
    }
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------