package com.AmazonEvent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

//import static com.example.testes.Del.emails;

public class AdapterParticipante extends FirebaseRecyclerAdapter<User, AdapterParticipante.myviewHolder>
{
    public AdapterParticipante(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull final User model) {
        holder.nm.setText(model.getEmail());
        Log.d("testando", "testando isso"+model.getEmail());
      //  emails.add(model.getEmail());
      //  Log.d("Temailpart","so emails "+emails);
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participante,parent,false);
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
