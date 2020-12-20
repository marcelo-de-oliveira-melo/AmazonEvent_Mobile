package com.AmazonEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.AmazonEvent.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;


public class ParticipandoFrag extends Fragment {

    RecyclerView recview;
    MyAdapter adapter;
    Button butaoPesq,butaoCan;
    EditText pesquisa ;
    String texto;
    public ParticipandoFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_participando, container, false);

        butaoCan = view.findViewById(R.id.btnCancela3);
        butaoPesq = view.findViewById(R.id.btnPesquisa3);
        pesquisa = view.findViewById(R.id.campoPesquisa3);

        butaoCan.setVisibility(View.INVISIBLE);

        FirebaseUser emailSenhaUsuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        final String email = emailSenhaUsuarioAtual.getEmail();
        recview = (RecyclerView) view.findViewById(R.id.listview3);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d(TAG,email);

        FirebaseRecyclerOptions<DataItem> options = new FirebaseRecyclerOptions.Builder<DataItem>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Data/participante"), DataItem.class)
                .build();

/*
        butao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto = pesquisa.getText().toString().trim();

                pesquisa(texto);
            }
        });


 */

        adapter = new MyAdapter(options);
        recview.setAdapter(adapter);

        return view;

    }

    private void pesquisa(String texto){

        FirebaseRecyclerOptions<DataItem> options = new FirebaseRecyclerOptions.Builder<DataItem>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Data").orderByChild("nome").startAt(texto).endAt(texto + "\uf8ff"), DataItem.class)
                .build();

        adapter.updateOptions(options);

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