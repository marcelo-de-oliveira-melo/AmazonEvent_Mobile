package com.AmazonEvent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Cadastro extends AppCompatActivity {
    private static final String TAG = "atividade";
    private EditText emailEt, senhaEt;
    private Button cadastro;
    private TextView loginView;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        emailEt = findViewById(R.id.email);
        senhaEt = findViewById(R.id.senha);
        cadastro = findViewById(R.id.btnCadastro);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cadastrando...");
        loginView = findViewById(R.id.textlogin);

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Este botão abaixo não está sendo utilizado, reveja
                //btnCadastro();

                if (isDadosCorretos(emailEt, senhaEt)) {
                    String email = emailEt.getText().toString().trim();
                    String senha = senhaEt.getText().toString().trim();
                    registerUser(email, senha);
                } else {
                    return;
                }
                Log.e(TAG, "entrou na função btnCadastro");
            }
        });

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cadastro.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(final String email, final String senha) {
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            salvaUsuarioNoBanco();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Cadastro.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Cadastro.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Log.e(TAG, "entrou na função registerUser");
    }


    private void salvaUsuarioNoBanco() {
        User user = new User(
                emailEt.getText().toString()
        );


        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        mDatabase.child("usuarios").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Cadastro.this, "Registrado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Cadastro.this, Principal.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Cadastro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // DatabaseReference mDatabase;
        //mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        //mDatabase.child("usuarios").child(usuario.getEmail()).setValue(usuario);
        Log.e(TAG, "entrou na função salvarUsuarioBanco");
    }

    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Cadastro.this);
        builder.setMessage("Você deseja sair da aplicação?");
        builder.setCancelable(true);
        builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private Boolean isDadosCorretos(@NotNull EditText emailEt, @NotNull EditText senhaEt) {
        String email = emailEt.getText().toString().trim();
        String senha = senhaEt.getText().toString().trim();

        Boolean isCorretos = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("email inválido!");
            emailEt.setFocusable(true);
            isCorretos = false;
        } else if (senha.length() < 6) {
            senhaEt.setError("senha >= 6 digitos");
            senhaEt.setFocusable(true);
            isCorretos = false;
        } else if (TextUtils.isEmpty(email)) {
            emailEt.setError("Enter your email");
            isCorretos = false;
        } else if (TextUtils.isEmpty(senha)) {
            senhaEt.setError("Enter your password");
            isCorretos = false;
        }
        return isCorretos;
    }


    /**
     * Isso aqui vai no botão da tela que o usuario clica para se cadastrar no evento
     * IdEvento - na hora que ele clicar pegar o id do evento
     * IdUsuario - o id do usuário é o email, então usa a função:
     *             FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
     *             String email = user.getEmail();
     */

   /* private void clickInscreverEmEvento(final String idEvento, final String idUsuario){
    //    final DatabaseReference mDatabase;
    //    mDatabase = FirebaseDatabase.getInstance().getReference();

    // Metodo 1

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios");
        final User usuarioAtual = ref.child(idUsuario);

        mDatabase.child("eventos")
                .child(idEvento)
                .child("participantes")
                .child(idUsuario)
                .setValue(usuarioAtual);

    // Metodo 2
       DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios/"+idUsuario);
       ref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshop.exists()){
                   User usuarioAtual = dataSnapshot.getValue(USer.class);
                   mDatabase.child("eventos")
                           .child(idEvento)
                           .child("participantes")
                           .child(idUsuario)
                           .setValue(usuarioAtual);
                }
           }

          @Override
               public void onCancelled(DatabaseError databaseError) {
               Log.e("TAG ALO ALO ALO ALO ALO", databaseError.getMessage());
          }
      });
    }*/


}
