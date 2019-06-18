package com.example.crudsqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crudsqlite.usuario.Usuario;
import com.example.crudsqlite.usuario.UsuarioDAO;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TelaUsuario extends AppCompatActivity {

    Usuario usuario;
    Usuario usuarioCarregado;
    UsuarioDAO usuarioDAO;
    EditText cadNome;
    EditText cadUsuario;
    EditText cadSenha;
    Button btSalvar;
    Button btPesquisar;
    Button btExcluir;
    Button btSincronizar;
    Button btVoltar;
    Button btListaBanco;

    boolean enviarDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_usuario);

        cadNome = findViewById(R.id.cadNome);
        cadUsuario = findViewById(R.id.cadUsuario);
        cadSenha = findViewById(R.id.cadSenha);

        enviarDados = false;


        btSalvar = findViewById(R.id.btSalvar);
        btPesquisar = findViewById(R.id.btPesquisar);
        btExcluir = findViewById(R.id.btExcluir);
        btSincronizar = findViewById(R.id.btSincronizar);
        btVoltar = findViewById(R.id.btVoltar);
        btListaBanco = findViewById(R.id.bt_listaBanco);

        usuario = new Usuario();
        usuarioCarregado = new Usuario();
        usuarioDAO =
                new UsuarioDAO(openOrCreateDatabase(usuarioDAO.NOME_BANCO,
                        MODE_PRIVATE, null));

        new Thread(new Runnable() {
            @Override
            public void run() {
                listarUsuario();
            }
        }).start();


        btListaBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaUsuario.this,
                        JSON.class));
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaUsuario.this,
                        MenuPrincipal.class));
            }
        });

        btSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // btSincronizar.setEnabled(false);
                //btSincronizar.setText("Sincronizando");

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList lista = usuarioDAO.listarUsuario();
                        if (lista != null) {
                            sincronizarUsuario(lista);
                        }
                    }
                });
                thread.start();

                synchronized (thread) {
                    // btSincronizar.setEnabled(true);
                    // btSincronizar.setText("Sincronizar");

                    if (enviarDados) {
                        Toast.makeText(getApplicationContext(), "Erro ao Sincronizar!"
                                , Toast.LENGTH_SHORT).show();
                        //    enviarDados = false;
                    } else {
                        Toast.makeText(getApplicationContext(), "Dados Sincronizados!"
                                , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuarioCarregado == null) {
                    usuarioCarregado = new Usuario();
                }
                usuario.setNome(criptografar(cadNome.getText().toString().toLowerCase()));
                usuario.setLogin(criptografar(cadUsuario.getText().toString().toLowerCase()));
                usuario.setSenha(criptografar(cadSenha.getText().toString().toLowerCase()));
                usuarioDAO.salvarUsuario(usuario);

                Toast.makeText(getApplicationContext(), "Dados salvos!",
                        Toast.LENGTH_SHORT).show();
                limparCampos();

            }
        });

        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuarioCarregado.getId() != 0) {
                    usuarioDAO.deletarUsuario(usuarioCarregado);
                    limparCampos();
                }
            }
        });

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaUsuario.this,
                        TelaPesquisaUsuario.class));
            }
        });

        carregarUsuario();

    }

    public void limparCampos() {
        cadNome.setText("");
        cadUsuario.setText("");
        cadSenha.setText("");

        usuario = new Usuario();
        usuarioCarregado = new Usuario();
    }

    public void carregarUsuario() {
        Intent i = getIntent();
        usuarioCarregado = (Usuario) i.getSerializableExtra("usuario");
        if (usuarioCarregado != null) {
            cadNome.setText(usuarioCarregado.getNome());
            cadUsuario.setText(usuarioCarregado.getLogin());
            cadSenha.setText(usuarioCarregado.getSenha());

            btExcluir.setEnabled(true);
        } else {
            btExcluir.setEnabled(false);
        }
    }

    public void sincronizarUsuario(ArrayList<Usuario> lista) {
        OkHttpClient client = new OkHttpClient();
        Request request;
        for (Usuario usuario : lista) {
            try {
                request = new Request.Builder()
                        .url("http://projetoandroidwebservice.000webhostapp.com/salvar.php?" +
                                "id=" + usuario.getId() +
                                "&nome=" + usuario.getNome() +
                                "&usuario=" + usuario.getLogin() +
                                "&senha=" + usuario.getSenha())
                        .get()
                        .build();
                client.newCall(request).execute();
                usuarioDAO.deletarUsuario(usuario);
                enviarDados = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String listarUsuario() {
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url("http://projetoandroidwebservice.000webhostapp.com/listar.php?")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String teste = response.body().string();
            return teste;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String criptografar(String crip) {
        return new String(Base64.encode(crip.getBytes(), Base64.DEFAULT));
    }
}
