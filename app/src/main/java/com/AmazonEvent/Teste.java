package com.AmazonEvent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.AmazonEvent.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Teste extends FirebaseRecyclerAdapter<DataItem, Teste.myviewHolder> {
    public Teste(@NonNull FirebaseRecyclerOptions<DataItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull final DataItem model) {
        holder.nm.setText(model.getId());
        Log.d("testando", "testando isso" + model.getId());
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participante, parent, false);
        return new myviewHolder(view);
    }

    class myviewHolder extends RecyclerView.ViewHolder {


        TextView nm;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);


            nm = (TextView) itemView.findViewById(R.id.emails);


        }
    }
}
