package com.AmazonEvent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MyAdapter extends FirebaseRecyclerAdapter<DataItem, MyAdapter.myviewHolder>
{

    public MyAdapter(@NonNull FirebaseRecyclerOptions<DataItem> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull final DataItem model) {
        holder.nm.setText(model.getNome());
        holder.dt.setText(model.getData());
        holder.hr.setText(model.getHora());
        holder.lc.setText(model.getLocal());
       // holder.cate.setText(model.getCategoria());
        Glide.with(holder.img.getContext()).load(model.getItemImage()).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new descFragment(model.getNome(),model.getCategoria(), model.getData(), model.getHora(), model.getLocal(), model.getEndereco(), model.getDescricao(), model.getItemImage(),model.getId())).addToBackStack(null).commit();

            }
        });

        Log.d("testando", "testando isso"+model.getId());
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_row,parent,false);
        return new myviewHolder(view);
    }

    class myviewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView nm, dt, hr, lc,cate;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.post_image);
            nm = (TextView) itemView.findViewById(R.id.post_nome);
            dt = (TextView) itemView.findViewById(R.id.data);
            hr = (TextView) itemView.findViewById(R.id.horaup);
            lc = (TextView) itemView.findViewById(R.id.local);
         //   cate = (TextView) itemView.findViewById(R.id.post_categoria);

        }
    }
}


