package com.example.crudsqlite.usuario;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UsuarioDAO {

    private SQLiteDatabase banco;
    public static final String NOME_BANCO = "crud";
    private static final String NOME_TABELA = "usuario";

    public UsuarioDAO(SQLiteDatabase banco) {
        try {
            this.banco = banco;
            banco.execSQL("CREATE TABLE IF NOT EXISTS " + NOME_TABELA +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome VARCHAR(100), " +
                    "usuario VARCHAR(30), " +
                    "senha VARCHAR(30))");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean salvarUsuario(Usuario usuario) {
        try {
            if (usuario.getId() == 0) {
                banco.execSQL("INSERT INTO " + NOME_TABELA +
                        "(nome, usuario, senha) VALUES " +
                        "('" + usuario.getNome() + "'," +
                        " '" + usuario.getLogin() + "'," +
                        " '" + usuario.getSenha() + "')");
            } else {
                banco.execSQL("UPDATE " + NOME_TABELA + " SET " +
                        "nome = '" + usuario.getNome() + "'," +
                        "usuario = '" + usuario.getLogin() + "'," +
                        "senha = '" + usuario.getSenha() + "' WHERE " +
                        "id = " + usuario.getId());

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deletarUsuario(Usuario usuario) {
        try {
            banco.execSQL("DELETE FROM " + NOME_TABELA +
                    " WHERE id=" + usuario.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public ArrayList<Usuario> listarUsuario(String nome) {
        ArrayList<Usuario> usuarios =
                new ArrayList<>();
        Usuario usuario;
        try {
            Cursor cursor =
                    banco.rawQuery
                            ("SELECT * FROM " + NOME_TABELA +" WHERE nome LIKE "+
                                    "'%"+nome+"%'",
                                    null);

            int indiceId =
                    cursor.getColumnIndex("id");
            int indiceNome =
                    cursor.getColumnIndex("nome");
            int indiceUsuario =
                    cursor.getColumnIndex("usuario");
            int indiceSenha =
                    cursor.getColumnIndex("senha");

            cursor.moveToFirst();

            while (cursor != null) {
                usuario = new Usuario();
                usuario.setId(Integer.
                        parseInt(
                                cursor.getString(indiceId)));

                usuario.setNome(
                        cursor.getString(indiceNome));
                usuario.setLogin(
                        cursor.getString(indiceUsuario));
                usuario.setSenha(
                        cursor.getString(indiceSenha));

                usuarios.add(usuario);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public ArrayList<Usuario> listarUsuario() {
        ArrayList<Usuario> usuarios =
                new ArrayList<>();
        Usuario usuario;
        try {
            Cursor cursor =
                    banco.rawQuery
                            ("SELECT * FROM " + NOME_TABELA,
                                    null);

            int indiceId =
                    cursor.getColumnIndex("id");
            int indiceNome =
                    cursor.getColumnIndex("nome");
            int indiceUsuario =
                    cursor.getColumnIndex("usuario");
            int indiceSenha =
                    cursor.getColumnIndex("senha");

            cursor.moveToFirst();

            while (cursor != null) {
                usuario = new Usuario();
                usuario.setId(Integer.
                        parseInt(
                                cursor.getString(indiceId)));

                usuario.setNome(
                        cursor.getString(indiceNome));
                usuario.setLogin(
                        cursor.getString(indiceUsuario));
                usuario.setSenha(
                        cursor.getString(indiceSenha));

                usuarios.add(usuario);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }


    public boolean autenticarUsuario(String usuario, String senha) {
        if (!usuario.isEmpty() && !senha.isEmpty()) {
            try {
                Cursor cursor =
                        banco.rawQuery
                         ("SELECT * FROM " + NOME_TABELA +
                          " WHERE usuario = '"+ usuario + "' AND " +
                          "senha = '" + senha + "'" ,null);
                int indiceUsuario =
                        cursor.getColumnIndex("usuario");

                cursor.moveToFirst();

                if(cursor != null){
                    if(!cursor.getString(indiceUsuario).isEmpty()){
                        return true;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return false;
    }

}
