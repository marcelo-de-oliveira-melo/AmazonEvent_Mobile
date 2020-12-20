package com.AmazonEvent;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static android.content.ContentValues.TAG;

public class recFragment extends Fragment {

    RecyclerView recview;
    MyAdapter adapter;
    Button butaoPesq,butaoCan;
    EditText pesquisa ;
    FirebaseRecyclerOptions<DataItem> options;
    String texto;
    TextView titulo;


    public recFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rec, container, false);

        butaoCan = view.findViewById(R.id.btnCancela);
        butaoPesq = view.findViewById(R.id.btnPesquisa);
        pesquisa = view.findViewById(R.id.campoPesquisa);
        titulo = view.findViewById(R.id.textView17);

        //titulo.setTypeface(ResourcesCompat.getFont(getContext(), R.font.comfortaa));


        butaoCan.setVisibility(View.INVISIBLE);

        recview = (RecyclerView) view.findViewById(R.id.listview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<DataItem>()
               .setQuery(FirebaseDatabase.getInstance().getReference().child("Data").orderByChild("nome"), DataItem.class)
                .build();

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

        adapter = new MyAdapter(options);
        recview.setAdapter(adapter);

        return view;

    }

    private void pesquisa(String texto){

        options = new FirebaseRecyclerOptions.Builder<DataItem>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Data").orderByChild("nome").startAt(texto).endAt(texto + "\uf8ff"), DataItem.class)
                .build();

        adapter.updateOptions(options);

    }
    private void apaga(String texto){

        options = new FirebaseRecyclerOptions.Builder<DataItem>()
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