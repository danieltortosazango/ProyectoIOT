package com.example.daferfus_upv.btle.POJOS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.daferfus_upv.btle.Activities.ConsejosActivity;
import com.example.daferfus_upv.btle.R;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Log.d(TAG, "onCreate: started.");

        getIncomingIntent();

        findViewById(R.id.linearAtras).setOnClickListener(v -> {
            Intent i = new Intent (getApplicationContext(), ConsejosActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("image_name")){
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            String imageUrl = getIntent().getStringExtra("image_url");
            String imageName = getIntent().getStringExtra("image_name");
            String imageDescripcion = getIntent().getStringExtra("image_descripcion");

            setImage(imageUrl, imageName, imageDescripcion);
        }
    }


    private void setImage(String imageUrl, String imageName, String imageDescripcion){
        Log.d(TAG, "setImage: setting te image and name to widgets.");

        TextView name = findViewById(R.id.titulo);
        name.setText(imageName.toUpperCase());//Las establecemos en mayuscula
        TextView descripcion = findViewById(R.id.descripcion);
        descripcion.setText(imageDescripcion);

        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }

}