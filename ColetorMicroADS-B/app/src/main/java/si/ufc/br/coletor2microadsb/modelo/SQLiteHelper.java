package si.ufc.br.coletor2microadsb.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by guilherme on 21/05/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    private String scriptCreate;
    private String scriptDelete;

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, String scriptCreate, String scriptDelete) {
        super(context, name, null, version);
        this.scriptCreate = scriptCreate;
        this.scriptDelete = scriptDelete;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(scriptCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(scriptDelete);
        onCreate(db);
    }
}
