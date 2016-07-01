package br.ufc.si.coletor.coletorads_b.util;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;

/**
 * Created by guilherme on 27/10/15.
 */
public class SaveMessageFile extends IntentService {

    private static final String TAG = SaveMessageFile.class.getSimpleName();

    public SaveMessageFile() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e(TAG, "Chamou!");
        RepositorioMensagem repositorio = new RepositorioMensagem(this);

        List<Mensagem> msgs = repositorio.findAll();

        for (Mensagem m : msgs){
            String line = m.timestamp+","+m.data+"\n";
            FileUtil.save(line);
        }



    }
}
