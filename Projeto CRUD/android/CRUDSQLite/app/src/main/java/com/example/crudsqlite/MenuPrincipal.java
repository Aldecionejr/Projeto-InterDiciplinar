package com.example.crudsqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuPrincipal extends AppCompatActivity {

    Button btCadUsuario;

    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);


        btCadUsuario = findViewById(R.id.btCadastroUsuario);
        welcome = findViewById(R.id.welcome);

        Intent i = getIntent();
        String usuario = i.getStringExtra("usuario");

        if (usuario != null) {
            welcome.setText("Bem vindo(a), " + usuario);
        }

        btCadUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this,
                        TelaUsuario.class));
            }
        });


    }
}
