package rafaela.kabuchi.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import rafaela.kabuchi.myapplication.modelo.Contato;

public class DaoContatos extends SQLiteOpenHelper {

    public DaoContatos(@Nullable Context context) {
        super(context, "BDContato", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cmd = "create table contatos(id varchar(255)PRIMARY KEY," + "nome varchar(255)," + "endereco varchar(255)," + "telefone varchar(255)," + "email varchar(255)," + "foto varchar(255)," + "lastupdate varchar(255)" + ")";
        db.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String cmd = "drop table contatos";
        onCreate(db);
    }

    public void CadastraContato(Contato contato)
    {
        ContentValues valores = new ContentValues();
        valores.put("id", contato.getId());
        valores.put("nome",contato.getNome());
        valores.put("endereco",contato.getEndereco());
        valores.put("telefone",contato.getTelefone());
        valores.put("email",contato.getEmail());
        valores.put("foto",contato.getFoto());
        valores.put("lastupdate",contato.getLastupdate());

        SQLiteDatabase db = getWritableDatabase();
        db.insert("contatos",null, valores);
    }

    public List<Contato> TodosContatos()
    {
        String[] colunas = new String [] {
                "id","nome","endereco","telefone","email","foto","lastupdate"
        };

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("contatos", colunas,null,null,null,null,"nome");
        List<Contato> listaContato = new ArrayList<>();
        while(cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getString(cursor.getColumnIndex("id")));
            contato.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            contato.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            contato.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            contato.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            contato.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
            contato.setLastupdate(cursor.getString(cursor.getColumnIndex("lastupdate")));

            listaContato.add(contato);
        }

        return listaContato;

    }

    public void AtualizaContato(Contato contato)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("nome",contato.getNome());
        valores.put("endereco",contato.getEndereco());
        valores.put("telefone",contato.getTelefone());
        valores.put("email",contato.getEmail());
        valores.put("foto",contato.getFoto());
        valores.put("lastupdate",contato.getLastupdate());

        String[] arg = new String[]{contato.getId()};
        db.update("contatos", valores,"id=?",arg);
    }

    public void Excluir(Contato contato)
    {
        SQLiteDatabase db = getWritableDatabase();
        String[] arg = new String[]{contato.getId()};
        db.delete("contatos","id=?",arg);
    }
}
