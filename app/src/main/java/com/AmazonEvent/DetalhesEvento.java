package com.AmazonEvent;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DetalhesEvento extends AppCompatActivity {
    String nome, data, hora, local, endereco, descricao, itemImage, id;

    public DetalhesEvento(){}

    public DetalhesEvento(String nome, String data, String hora, String local, String endereco, String descricao, String itemImage, String id) {
        this.nome = nome;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);
        getSupportActionBar().hide();


        ImageView imageholder = findViewById(R.id.imageholder);
        TextView nom = findViewById(R.id.nomeholder);
        TextView dat = findViewById(R.id.dataholder);
        TextView hor = findViewById(R.id.horaholder);
        TextView loc = findViewById(R.id.localholder);
        TextView end = findViewById(R.id.endeholder);
        TextView desc = findViewById(R.id.descholder);
        TextView bt = findViewById(R.id.part);

        nom.setText(nome);
        dat.setText(data);
        hor.setText(hora);
        loc.setText(local);
        end.setText(endereco);
        desc.setText(descricao);

        Glide.with(DetalhesEvento.this).load(itemImage).into(imageholder);



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
                            Toast.makeText(DetalhesEvento.this, "Inscrito!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetalhesEvento.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });




    }

    public void onBackPressed() {

    }
}

















