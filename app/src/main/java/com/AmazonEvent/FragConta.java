package com.AmazonEvent;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.AmazonEvent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.AmazonEvent.EditarPerfil;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class FragConta extends Fragment {
     EditarPerfil ep = new EditarPerfil();

    Button edit, logout;
    ImageButton casa;
    TextView nome ,email ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
      ImageView foto;

    private AnimationDrawable mAnimation;

    public FragConta() {
        // Required empty public constructor
    }

/////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.minhaconta, container, false);
        edit =view.findViewById(R.id.edit);
        casa =view.findViewById(R.id.home);
        logout = view.findViewById(R.id.sair);
        email = view.findViewById(R.id.emailview);
        nome = view.findViewById(R.id.nomeview);


        Log.e(TAG," onde está => "+ep.ondeTava+" <= ");

        foto = view.findViewById(R.id.foto);
        foto.setBackgroundResource(R.drawable.carrega_img);

        mAnimation = (AnimationDrawable)foto.getBackground();
        mAnimation.start();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(),EditarPerfil.class));
                getActivity().finish();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity act = getActivity();

                if (act != null) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(act, Login.class));
                    getActivity().finish();
                }
            }

        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email.setText(user.getEmail());
            nome.setText(user.getDisplayName());
            System.out.println("oq veio: " + user.getPhotoUrl());
            Picasso.get()
                    .load(user.getPhotoUrl())
                    .fit()
                    .into(foto);
        }
        ////
        return view;
    }


    public void onBackPressed(){
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recFragment()).addToBackStack(null).commit();

    }


}
