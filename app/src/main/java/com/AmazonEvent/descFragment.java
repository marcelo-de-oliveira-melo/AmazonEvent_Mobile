package com.AmazonEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.AmazonEvent.R;
import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class descFragment extends Fragment {

    String nome,categoria, data, hora, local, endereco, descricao, itemImage, id;

    public descFragment() {

    }

    public descFragment(String nome, String categoria, String data, String hora, String local, String endereco, String descricao, String itemImage, String id) {
        this.nome = nome;
        this.categoria = categoria;
        this.data = data;
        this.hora = hora;
        this.local = local;
        this.endereco = endereco;
        this.descricao = descricao;
        this.itemImage = itemImage;
        this.id = id;

        Log.d("TaId", "testa id " + id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_desc, container, false);
        ImageView imageholder = view.findViewById(R.id.imageholder);
        TextView nom = view.findViewById(R.id.nomeholder);
        TextView dat = view.findViewById(R.id.dataholder);
        TextView hor = view.findViewById(R.id.horaholder);
        TextView loc = view.findViewById(R.id.localholder);
        TextView end = view.findViewById(R.id.endeholder);
        TextView desc = view.findViewById(R.id.descholder);
        TextView cat = view.findViewById(R.id.cateholder);
        TextView bt = view.findViewById(R.id.part);
        nom.setText(nome);
        cat.setText(categoria);
        dat.setText(data);
        hor.setText(hora);
        loc.setText(local);
        end.setText(endereco);
        desc.setText(descricao);

        Glide.with(getContext()).load(itemImage).into(imageholder);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser emailSenhaUsuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
                final String email = emailSenhaUsuarioAtual.getEmail();

                User user = new User(email);

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference("Data");
                mDatabase.child(id + "/participante").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Inscrito!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        return view;
    }

    public void onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recFragment()).addToBackStack(null).commit();

    }
}