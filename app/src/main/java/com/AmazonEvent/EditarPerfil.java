package com.AmazonEvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

public class EditarPerfil extends AppCompatActivity{
    Uri uri;
    String imageUrl;
    EditText nome;
    Button seleciona;
    ImageButton atualizar,voltar;
    ImageView foto,bar;
    int verifica=0;
    static int ondeTava=0 ;
    private AnimationDrawable mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);
        getSupportActionBar().hide();
        seleciona = findViewById(R.id.seleciona);
        nome = findViewById(R.id.nome);
        atualizar = findViewById(R.id.atualizar);
        voltar = findViewById(R.id.btnVoltar);
        foto = findViewById(R.id.foto);
        foto.setBackgroundResource(R.drawable.carrega_img);

        mAnimation = (AnimationDrawable)foto.getBackground();
        mAnimation.start();

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finaliza();
            }
        });
        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        seleciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(Intent.ACTION_PICK);
                photo.setType("image/*");
                startActivityForResult(photo, 1);
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            nome.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null) {
                imageUrl = user.getPhotoUrl().toString();
                Picasso
                        .get()
                        .load(user.getPhotoUrl())
                        .fit()
                        .noFade()
                        .into(foto);
            }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            Picasso.get()
                    .load(uri)
                    .fit()
                    .into(foto);

            verifica=1;
        }

    }
    private void uploadImage() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        if (verifica == 0){
            salvar();
        }
        else {
            if (verifica == 1) {


                StorageReference storageReference = FirebaseStorage.getInstance()
                        .getReference().child("FotoPerfil").child(uri.getLastPathSegment());

                progressDialog.setMessage("Salvando imagem...");
                progressDialog.show();

                storageReference.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isComplete()) ;
                                Uri urlImage = uriTask.getResult();
                                imageUrl = urlImage.toString();

                                Toast.makeText(EditarPerfil.this, "Imagem salva", Toast.LENGTH_SHORT).show();

                                Log.d(TAG, "Imagem upada");
                                Log.d(TAG, "a imageUrl: " + imageUrl);
                                progressDialog.dismiss();

                               salvar();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });

            }
        }
    }
    public void salvar() {
            final ProgressDialog progressDialog = new ProgressDialog(EditarPerfil.this);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String novoNome = nome.getText().toString().trim();
        if (nome.getText().length()==0){
            Toast.makeText(EditarPerfil.this, "campo vazio", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Salvando alterações...");
            progressDialog.show();

            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(novoNome)
                    .setPhotoUri(Uri.parse(imageUrl))
                    .build();

            user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                                progressDialog.dismiss();
                                Toast.makeText(EditarPerfil.this, "Alterações salvas", Toast.LENGTH_SHORT).show();
                                finaliza();

                            }
                        }
                    });}

            }
    public void onBackPressed(){
      finaliza();

    }
    public void finaliza(){
        startActivity(new Intent(this,Principal.class));
        finish();
        ondeTava=1;
        Log.e(TAG,"onde está => "+ondeTava+" <= ");
}


}



