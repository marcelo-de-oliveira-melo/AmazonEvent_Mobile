package com.AmazonEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.AmazonEvent.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class fragmen extends Fragment {

    RecyclerView recview;
    mAdapter adapter;
    Button butaoPesq,butaoCan;
    EditText pesquisa ;
    String texto;

    public fragmen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragmen, container, false);

        butaoCan = view.findViewById(R.id.btnCancela2);
        butaoPesq = view.findViewById(R.id.btnPesquisa2);
        pesquisa = view.findViewById(R.id.campoPesquisa2);

        butaoCan.setVisibility(View.INVISIBLE);

        FirebaseUser emailSenhaUsuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        final String email = emailSenhaUsuarioAtual.getEmail();
        recview = (RecyclerView) view.findViewById(R.id.list);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<DataItem> options = new FirebaseRecyclerOptions.Builder<DataItem>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Data").orderByChild("dono").equalTo(email), DataItem.class)
                .build();

        butaoCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisa.setText("");
                texto = pesquisa.getText().toString().trim();
                apaga(texto);

                butaoCan.setVisibility(View.INVISIBLE);
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

        adapter = new mAdapter(options);
        recview.setAdapter(adapter);

        return view;
    }

    private void pesquisa(String texto){

        FirebaseRecyclerOptions<DataItem> options = new FirebaseRecyclerOptions.Builder<DataItem>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Data").orderByChild("nome").startAt(texto).endAt(texto + "\uf8ff"), DataItem.class)
                .build();

        adapter.updateOptions(options);

    }
    private void apaga(String texto){

        FirebaseUser emailSenhaUsuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        final String email = emailSenhaUsuarioAtual.getEmail();

        FirebaseRecyclerOptions<DataItem> options = new FirebaseRecyclerOptions.Builder<DataItem>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Data").orderByChild("dono").equalTo(email), DataItem.class)
                .build();

        adapter.updateOptions(options);

    }


    public void onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recFragment()).addToBackStack(null).commit();

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