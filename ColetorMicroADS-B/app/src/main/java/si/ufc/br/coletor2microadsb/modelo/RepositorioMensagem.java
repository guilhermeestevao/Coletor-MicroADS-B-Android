package si.ufc.br.coletor2microadsb.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import si.ufc.br.coletor2microadsb.modelo.Mensagem.Mensagens;

/**
 * Created by guilherme on 21/05/15.
 */
public class RepositorioMensagem {

    private static final String NOME_BANCO = "adsb";
    private static final String NOME_TABELA = "mensagens";
    private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS "+NOME_TABELA;
    private static final String SCRIPT_DATABASE_CREATE = new String("create table "+NOME_TABELA+" (_id integer primary key, data text, timestamp integer )");
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
        return inserir(values);
    }

    public long inserir(ContentValues values){
        long id = db.insert(NOME_TABELA, "", values);
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
            return msg;
        }
        return null;
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
            do{
                Mensagem msg = new Mensagem();
                msg.id = c.getLong(idxId);
                msg.data = c.getString(idxData);
                msg.timestamp = c.getLong(idxTimestamp);
                msgs.add(msg);
            }while(c.moveToNext());
        }

        return msgs;

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
