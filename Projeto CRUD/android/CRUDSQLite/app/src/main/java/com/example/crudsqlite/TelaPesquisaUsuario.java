package com.example.crudsqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.crudsqlite.usuario.Usuario;
import com.example.crudsqlite.usuario.UsuarioDAO;

import java.util.ArrayList;

public class TelaPesquisaUsuario extends AppCompatActivity {

    UsuarioDAO usuarioDAO;
    ArrayList<Usuario> listaUsuarios;

    ArrayAdapter usuariosAdaptador;

    Button btPesquisar;
    ListView listUsuarios;
    EditText usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_pesquisa_usuario);

        btPesquisar = findViewById(R.id.btFiltrarUsuario);
        listUsuarios = findViewById(R.id.listViewUsuarios);
        usuario = findViewById(R.id.campoPesquisar);



        usuarioDAO =
                new UsuarioDAO(openOrCreateDatabase(usuarioDAO.NOME_BANCO, MODE_PRIVATE, null));

        listaUsuarios = usuarioDAO.listarUsuario();

        listarUsuarios(listaUsuarios);

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!usuario.getText().toString().isEmpty()){
                    listaUsuarios =
                            usuarioDAO.listarUsuario(usuario.getText().toString());

                    listarUsuarios(listaUsuarios);
                }


            }
        });

        listUsuarios.setLongClickable(true);
        listUsuarios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =
                new Intent(TelaPesquisaUsuario.this,
                        TelaUsuario.class);

                intent.putExtra("usuario", listaUsuarios.get(position));
                startActivity(intent);
                return true;
            }
        });


    }

    public void listarUsuarios(ArrayList lista){
        usuariosAdaptador = new ArrayAdapter<Usuario>(this,
                android.R.layout.simple_expandable_list_item_2,
                android.R.id.text2,
                lista);
        listUsuarios.setAdapter(usuariosAdaptador);
    }
}
