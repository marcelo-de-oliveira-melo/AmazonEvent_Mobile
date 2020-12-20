package com.AmazonEvent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class UpdateEvento extends AppCompatActivity {

    EditText etnome, etcategoria, etlocal, etendereco, etdata, ethora, etdesc;
    Button btnUp;
    ImageButton btnVolta;
    String nome, categoria, local, endereco, data, hora, descricao, itemImage, id, dono;
    static int ondeTavaUp = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        getSupportActionBar().hide();

        etnome = findViewById(R.id.nome);
        etcategoria = findViewById(R.id.categoria);
        etlocal = findViewById(R.id.local);
        etendereco = findViewById(R.id.endereco);
        etdata = findViewById(R.id.Data);
        ethora = findViewById(R.id.horaup);
        etdesc = findViewById(R.id.desc);
        btnUp = findViewById(R.id.atualiza);
        btnVolta = findViewById(R.id.btnVoltaup);

        Intent intent = getIntent();

        nome= intent.getStringExtra("nome");
        categoria= intent.getStringExtra("categoria");
        local= intent.getStringExtra("local");
        endereco= intent.getStringExtra("endereco");
        data= intent.getStringExtra("data");
        hora= intent.getStringExtra("hora");
        descricao= intent.getStringExtra("descricao");
        itemImage= intent.getStringExtra("itemImage");
        id= intent.getStringExtra("id");
        dono= intent.getStringExtra("dono");

        etnome.setText(nome);
        etcategoria.setText(categoria);
        etlocal.setText(local);
        etendereco.setText(endereco);
        etdata.setText(data);
        ethora.setText(hora);
        etdesc.setText(descricao);

        btnVolta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finaliza();
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dtr= FirebaseDatabase.getInstance().getReference("Data").child(id);
                String nome, categoria, local, endereco, data, hora, descricao;

              nome = etnome.getText().toString();
               categoria = etcategoria.getText().toString();
                local = etlocal.getText().toString();
                endereco =etendereco.getText().toString();
                data = etdata.getText().toString();
                hora = ethora.getText().toString();
                descricao = etdesc.getText().toString();


                DataItem item = new DataItem(nome,categoria,local,endereco,data,hora,descricao,itemImage,id,dono);
                dtr.setValue(item);

                finaliza();
                Toast.makeText(UpdateEvento.this, "Evento Atualizado!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onBackPressed(){
        finaliza();

    }
    public void finaliza(){
        startActivity(new Intent(this,Principal.class));
        finish();
        ondeTavaUp=1;
        Log.e(TAG,"onde está => "+ondeTavaUp+" <= ");
    }
}
