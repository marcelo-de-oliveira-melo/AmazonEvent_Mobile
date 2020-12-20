package com.AmazonEvent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.AmazonEvent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.content.ContentValues.TAG;


public class Principal extends AppCompatActivity {

    EditarPerfil ep = new EditarPerfil();
    UpdateEvento ue = new UpdateEvento();


    Button butao;

    ImageButton crie, conta, meu;
    mAdapter adapter;
    RecyclerView recview;

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selected = null;
            switch (menuItem.getItemId()) {
                case R.id.home:
                    selected = new recFragment();
                    break;
                case R.id.meu:
                    selected = new fragmen();
                    break;
                case R.id.part:
                    selected = new ParticipandoFrag();
                    break;
                case R.id.criar:
                    selected = new EventoFragment();
                    break;
                case R.id.perfill:
                    selected = new FragConta();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, selected).commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getSupportActionBar().hide();



        Log.e(TAG,"onde está perfil => "+ep.ondeTava+" <= ");
        Log.e(TAG,"onde está meu evento=> "+ue.ondeTavaUp+" <= ");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (ep.ondeTava == 1){
            ep.ondeTava = 0;
            getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new FragConta()).commit();
            bottomNavigationView.setSelectedItemId(R.id.perfill);

        }

        else if (ue.ondeTavaUp == 1){
            ue.ondeTavaUp = 0;
            getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new FragConta()).commit();
            bottomNavigationView.setSelectedItemId(R.id.meu);

        }

        else {

            getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recFragment()).commit();


        }

    }


}