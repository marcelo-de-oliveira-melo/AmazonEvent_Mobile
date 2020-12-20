package com.AmazonEvent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText emailEt, senhaEt;
    private Button loginbtn, btgoogle;
    private TextView cadastroView, esquece;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = findViewById(R.id.email);
        senhaEt = findViewById(R.id.senha);
        loginbtn = findViewById(R.id.btnLogin);

        cadastroView = findViewById(R.id.textCad);
        progressDialog = new ProgressDialog(this);

        esquece = findViewById(R.id.esqueceuSenha);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  login();
                String email = emailEt.getText().toString();
                String senha = senhaEt.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEt.setError("email inválido!");
                    emailEt.setFocusable(true);
                } else {
                    loginUser(email, senha);
                }
            }
        });

        cadastroView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
                finish();
            }
        });

        esquece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redefinirsenha();
            }
        });

    }
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
    private void Redefinirsenha() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Redefinir senha");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Redefinir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void beginRecovery(String email) {
        progressDialog.setMessage("Enviando email...");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Email enviado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "falhou..", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loginUser(String email, String senha) {

        progressDialog.setMessage("Entrando...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {// Verifica se o usuario está logado
                                startActivity(new Intent(Login.this, Principal.class));
                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
/*if (task.isSuccessful()) {
    FirebaseUser user = mAuth.getCurrentUser();
    Toast.makeText(SignInActivity.this, "Seja bem vindo: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
    if (user != null) {// Verifica se o usuario está logado
        startActivity(new Intent(SignInActivity.this, Login.class));
    }
} else {
    // Se não estiver logado
    Log.w(TAG, "signInWithCredential:failure", task.getException());
    Toast.makeText(SignInActivity.this, "Authentication failed.",
            Toast.LENGTH_SHORT).show();
}*/
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        //  obterUsuarioAtual();
    }
    // private void obterUsuarioAtual(){
    //}
   /* public void obter() {
        FirebaseUser emailSenhaUsuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        final String email = emailSenhaUsuarioAtual.getEmail();

        DatabaseReference databaseReference;

        databaseReference.child("Data").orderByChild("dono").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    Log.d("tag1","snap "+snapshot);
                    Log.d("tag2","snapp "+snapshot.getChildren());
                    Log.d("tag3","snappp "+snapshot.getValue());
                    Log.d("tag4","snapppp "+snapshot.getValue(Evento.class));

                }else{
                    Log.d("tagNao","sei la ");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

}


