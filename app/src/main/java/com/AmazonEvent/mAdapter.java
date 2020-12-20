package com.AmazonEvent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class mAdapter extends FirebaseRecyclerAdapter<DataItem, mAdapter.myviewHolder>
{
    private Context context;
    public mAdapter(@NonNull FirebaseRecyclerOptions<DataItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull final DataItem model) {
        holder.nm.setText(model.getNome());
        Glide.with(holder.img.getContext()).load(model.getItemImage()).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new listaPart(model.getId())).addToBackStack(null).commit();

            }
        });
        holder.exclui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Data").child(model.getId());
                dr.removeValue();
               // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UpdateEvento.class);
                i.putExtra("id", model.getId());
                i.putExtra("nome", model.getNome());
                i.putExtra("categoria", model.getCategoria());
                i.putExtra("local", model.getLocal());
                i.putExtra("endereco", model.getEndereco());
                i.putExtra("data", model.getData());
                i.putExtra("hora", model.getHora());
                i.putExtra("descricao", model.getDescricao());
                i.putExtra("itemImage", model.getItemImage());
                i.putExtra("id", model.getId());
                i.putExtra("dono", model.getDono());
                v.getContext().startActivity(i);
            }
        });


        Log.d("testando", "testando isso"+model.getId());
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meuseventoscard,parent,false);
        return new myviewHolder(view);
    }

    class myviewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView nm, exclui, edit;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.cimage);
            nm = (TextView) itemView.findViewById(R.id.card_nome);
            exclui = (TextView) itemView.findViewById(R.id.delet);
            edit = (TextView) itemView.findViewById(R.id.editar);
        }
    }
}
