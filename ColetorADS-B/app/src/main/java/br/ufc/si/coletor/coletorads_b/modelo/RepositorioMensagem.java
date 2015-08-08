package br.ufc.si.coletor.coletorads_b.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.ufc.si.coletor.coletorads_b.modelo.Mensagem.Mensagens;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilherme on 21/05/15.
 */
public class RepositorioMensagem implements Serializable{

    private static final String NOME_BANCO = "adsb";
    private static final String NOME_TABELA = "mensagens";
    private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS "+NOME_TABELA;
    private static final String SCRIPT_DATABASE_CREATE = new String("create table "+NOME_TABELA+" (_id integer primary key, data text, timestamp integer, sinc integer)");
    private static final int VERSAO_BANCO = 1;
    private SQLiteDatabase db;
    private SQLiteHelper dbHelper;

    public RepositorioMensagem(Context context){
        dbHelper = new SQLiteHelper(context, NOME_BANCO, null, VERSAO_BANCO, SCRIPT_DATABASE_CREATE, SCRIPT_DATABASE_DELETE);
        db = dbHelper.getWritableDatabase();
    }

    public long inserir(Mensagem msg){
        ContentValues values = new ContentValues();
        values.put(Mensagens.DATA, msg.data );
        values.put(Mensagens.TIMESTAMP, msg.timestamp);
        values.put(Mensagens.SINC, msg.sinc);
        return inserir(values);
    }

    public long inserir(ContentValues values){
        long id = db.insert(NOME_TABELA, "", values);
        return id;
    }

    public long removerTudo(){
        long id = db.delete(NOME_TABELA, "", null);
        db.execSQL(SCRIPT_DATABASE_CREATE);
        return id;
    }

    public Mensagem find(long id){
        Cursor c = db.query(true, NOME_TABELA, Mensagem.colunas, Mensagens._ID + "=" + id, null, null, null, null, null);
        if(c.getCount() > 0){
            c.moveToFirst();
            Mensagem msg = new Mensagem();
            msg.id = c.getLong(0);
            msg.data = c.getString(1);
            msg.timestamp = c.getLong(2);
            msg.sinc = c.getInt(3);
            return msg;
        }
        return null;
    }

    public synchronized List<Mensagem> findNaoSincronizadas(){
        Cursor c = db.query(true, NOME_TABELA, Mensagem.colunas, Mensagens.SINC + "=" + 0, null, null, null, null, null);
        List<Mensagem> msgs = new ArrayList<Mensagem>();
        if(c.getCount() > 0){
            c.moveToFirst();
            int idxId = c.getColumnIndex(Mensagens._ID);
            int idxData = c.getColumnIndex(Mensagens.DATA);
            int idxTimestamp = c.getColumnIndex(Mensagens.TIMESTAMP);
            int idxSinc = c.getColumnIndex(Mensagens.SINC);
            do{
                Mensagem msg = new Mensagem();
                msg.id = c.getLong(idxId);
                msg.data = c.getString(idxData);
                msg.timestamp = c.getLong(idxTimestamp);
                msg.sinc = c.getInt(idxSinc);
                msgs.add(msg);
            }while(c.moveToNext());
        }
        return msgs;
    }


    private Cursor getCursor() {
        try {
            return db.query(NOME_TABELA, Mensagem.colunas, null, null, null, null, null, null);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Mensagem> findAll(){
        Cursor c = getCursor();
        List<Mensagem> msgs = new ArrayList<Mensagem>();
        if(c.getCount() > 0){
            c.moveToFirst();
            int idxId = c.getColumnIndex(Mensagens._ID);
            int idxData = c.getColumnIndex(Mensagens.DATA);
            int idxTimestamp = c.getColumnIndex(Mensagens.TIMESTAMP);
            int idxSinc = c.getColumnIndex(Mensagens.SINC);
            do{
                Mensagem msg = new Mensagem();
                msg.id = c.getLong(idxId);
                msg.data = c.getString(idxData);
                msg.timestamp = c.getLong(idxTimestamp);
                msg.sinc = c.getInt(idxSinc);
                msgs.add(msg);
            }while(c.moveToNext());
        }
        return msgs;
    }

    public int atualizar(Mensagem mensagem){
        ContentValues values = new ContentValues();
        values.put(Mensagens.SINC, mensagem.sinc);
        String _id = String.valueOf(mensagem.id);
        String where = Mensagens._ID+"=?";
        String[] whereArgs = new String[]{_id};
        int count = atualizar(values, where, whereArgs);
        return count;
    }

    private int atualizar(ContentValues values, String where, String[] whereArgs){
         return db.update(NOME_TABELA, values, where, whereArgs);
    }

    public void fechar(){
        if(db != null){
            db.close();
        }

        if(dbHelper != null){
            dbHelper.close();
        }
    }
}