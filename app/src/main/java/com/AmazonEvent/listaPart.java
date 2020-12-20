package com.AmazonEvent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class listaPart extends Fragment {

    RecyclerView recview;
    AdapterParticipante adapter;
    String nome,id;
    Button enviar,butaoPesq,butaoCan,butaoPdf;
    TextView empa;
    EditText pesquisa;
    String texto;


    public listaPart() {
        // Required empty public constructor
    }
    public listaPart(String id) {

        this.id=id;

        Log.d("Ta","testa id "+id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_lista_part, container, false);

        butaoPdf= view.findViewById(R.id.impPdf);
        butaoCan= view.findViewById(R.id.btnCancela4);
        butaoPesq= view.findViewById(R.id.btnPesquisa4);
        pesquisa= view.findViewById(R.id.campoPesquisa4);

        butaoCan.setVisibility(View.INVISIBLE);

        recview = (RecyclerView)view.findViewById(R.id.listpart);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Data/"+id+"/participante"), User.class)
                .build();

        butaoPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imprimePdf();

            }
        });
        butaoPesq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto = pesquisa.getText().toString().trim();
                if (pesquisa.getText().length() == 0){
                    Toast.makeText(getContext(), "campo vazio", Toast.LENGTH_SHORT).show();
                }else {
                    pesquisa(texto);
                    butaoCan.setVisibility(View.VISIBLE);
                }

            }
        });

        butaoCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisa.setText("");
                texto = pesquisa.getText().toString().trim();
                apaga(texto);

                butaoCan.setVisibility(View.INVISIBLE);
            }
        });

        adapter = new AdapterParticipante(options);
        recview.setAdapter(adapter);
        return view;
    }

    private void pesquisa(String texto){

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Data/"+id+"/participante").orderByChild("email").startAt(texto).endAt(texto + "\uf8ff"), User.class)
                .build();

        adapter.updateOptions(options);

    }
    private void apaga(String texto){
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Data/"+id+"/participante"), User.class)
                .build();

        adapter.updateOptions(options);

    }
    private void imprimePdf(){
        PdfDocument pdfDocument= new PdfDocument();

        PdfDocument.PageInfo detalhePagina=
                new PdfDocument.PageInfo.Builder(500,600,1).create();

        PdfDocument.Page novaPagina = pdfDocument.startPage(detalhePagina);

        Canvas canvas = novaPagina.getCanvas();

        Paint corDoTexto = new Paint();
        corDoTexto.setColor(Color.BLACK);

        canvas.drawText("AmazonEvent",1,1,corDoTexto);

        //canvas.drawText();

        pdfDocument.finishPage(novaPagina);

        String data = "data ai";

        String targetPdf = "/ListPart.pdf";
        File filePath = new File(targetPdf);

        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getContext(), "PDF Gerado com sucesso", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Falha ao gerar o PDF: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        pdfDocument.close();

    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
