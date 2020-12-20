package com.AmazonEvent;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class EventoFragment extends Fragment {
    ImageButton bdata, bhora, cancel;
    String imageUrl;
    Uri uri;
    ImageView imageView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView seleciona;
    Boolean situacao;

    private Button bsalva;
    private EditText edata, ehora, enome, ecategoria, elocal, eendereco, edesc;
    private int dia, mes, ano, hora, minutos;

    public EventoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.criar_evento_fragment, container, false);
        enome = view.findViewById(R.id.nome);
        ecategoria = view.findViewById(R.id.categoria);
        elocal = view.findViewById(R.id.local);
        eendereco = view.findViewById(R.id.endereco);
        edesc = view.findViewById(R.id.desc);
        imageView = view.findViewById(R.id.image_view);
        bsalva = view.findViewById(R.id.salva);
        bdata = view.findViewById(R.id.btnData);
        bhora = view.findViewById(R.id.btnHora);
        edata = view.findViewById(R.id.data);
        ehora = view.findViewById(R.id.horaup);
        // bdata.setOnClickListener(this);
        //bhora.setOnClickListener(this);
        seleciona = view.findViewById(R.id.selecionar);
        cancel = view.findViewById(R.id.btnVoltar);

        bdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfyear, int dayOfMonth) {
                        edata.setText(dayOfMonth + "/" + (monthOfyear + 1) + "/" + year);

                    }
                }
                        , dia, mes, ano);
                datePickerDialog.show();
            }
        });

        bhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                hora = c.get(Calendar.HOUR_OF_DAY);
                minutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ehora.setText(hourOfDay + ":" + minute);
                    }
                }, hora, minutos, false);
                timePickerDialog.show();
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

        bsalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploudImage();
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            // imageView.setImageURI(uri);
            Picasso.get()
                    .load(uri)
                    .fit()
                    .into(imageView);

        }
    }

    public void uploudImage() {
        if (uri == null){
            Toast.makeText(getActivity(), "sem imagem", Toast.LENGTH_SHORT).show();
        }
        if (verificaCampos() == false) {
            Toast.makeText(getActivity(), "campo vazio", Toast.LENGTH_SHORT).show();
        }
          else {

                StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference().child("EventoImg").child(uri.getLastPathSegment());

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Publicando Evento...");
                progressDialog.show();

                storageReference.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri urlImage = uriTask.getResult();
                        imageUrl = urlImage.toString();

                        uploudEvento();

                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });

        }

    }

  public boolean verificaCampos(){
        if (enome.getText().length() == 0){
            if (ecategoria.getText().length() == 0){
                if (elocal.getText().length() == 0){
                    if (eendereco.getText().length() == 0){
                        if (edata.getText().length() == 0){
                            if (ehora.getText().length() == 0){
                                if (edesc.getText().length() == 0){
                                    if (enome.getText().length() == 0) {

                                        situacao = false;
                                    }}}}}}}}

        else {
        situacao = true;

        }
        return situacao;

  }
    public void uploudEvento() {

            String myCurrentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            FirebaseUser emailSenhaUsuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
            final String email = emailSenhaUsuarioAtual.getEmail();
            Log.d("email", "verifica emai" + email);
            DataItem dataItem = new DataItem(
                    enome.getText().toString(),
                    ecategoria.getText().toString(),
                    elocal.getText().toString(),
                    eendereco.getText().toString(),
                    edata.getText().toString(),
                    ehora.getText().toString(),
                    edesc.getText().toString(),
                    imageUrl,
                    myCurrentDateTime,
                    email

            );

            FirebaseDatabase.getInstance().getReference("Data")
                    .child(myCurrentDateTime).setValue(dataItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(getActivity(), "Evento publicado", Toast.LENGTH_SHORT).show();
                        AppCompatActivity activity = (AppCompatActivity)getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recFragment()).addToBackStack(null).commit();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


    }
}
