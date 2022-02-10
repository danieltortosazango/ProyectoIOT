package com.example.daferfus_upv.btle.POJOS;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daferfus_upv.btle.ConstantesAplicacion;
import com.example.daferfus_upv.btle.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.daferfus_upv.btle.ConstantesAplicacion.PASOS;
import static com.example.daferfus_upv.btle.ConstantesAplicacion.URL_CONSULTA_FOTOS_LOGROS;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private final ArrayList<Logros> mLogros;
    private final Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Logros> logros) {
        mLogros = logros;
        mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.get()
                .load(Uri.parse(URL_CONSULTA_FOTOS_LOGROS + mLogros.get(position).getImagenLogro()))
                .placeholder(R.drawable.logros)
                .error(R.drawable.f)
                .into(holder.image);

        holder.name.setText(mLogros.get(position).getNombreLogro());

        holder.image.setOnClickListener(view -> {

            Toast.makeText(mContext, mLogros.get(position).getNombreLogro(), Toast.LENGTH_SHORT).show();
        });


        int meta = 0;
        if ("Pasos".equals(mLogros.get(position).getTipo())) {
            meta = ConstantesAplicacion.PASOS;
        } else if ("Distancia".equals(mLogros.get(position).getTipo())) {
            meta = ConstantesAplicacion.DISTANCIA;
        }
        if (holder.increase_progress.getProgress() < 100) {
            double resDivision = meta * 1.0 / mLogros.get(position).getDificultadCompletar();
            Log.d(TAG, ",,,,,--total" + resDivision * 100);
            holder.increase_progress.setProgress((int) (holder.increase_progress.getProgress() + (resDivision * 100)));
        }
        //Log.d(TAG, ",,,,,--total" + holder.increase_progress.getProgress()+(resDivision*100)));
        Log.d(TAG, ",,,,,--meta" + meta);
        Log.d(TAG, ",,,,,--difi" + mLogros.get(position).getDificultadCompletar());
        if(meta>mLogros.get(position).getDificultadCompletar()){
            meta=mLogros.get(position).getDificultadCompletar();
        }
        holder.pasosRealizados.setText(String.format(mContext.getString(R.string.ratio), Integer.toString(meta),
                Integer.toString(mLogros.get(position).getDificultadCompletar())));
    }

    @Override
    public int getItemCount() {
        return mLogros.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name;
        ProgressBar increase_progress;
        TextView pasosRealizados;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            increase_progress = itemView.findViewById(R.id.progress_increase);
            pasosRealizados = itemView.findViewById(R.id.tvPasosRealizados);
        }
    }

}
