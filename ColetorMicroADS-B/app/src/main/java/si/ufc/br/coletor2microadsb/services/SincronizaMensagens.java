package si.ufc.br.coletor2microadsb.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import si.ufc.br.coletor2microadsb.modelo.Mensagem;
import si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;

/**
 * Created by guilherme on 31/05/15.
 */
public class SincronizaMensagens extends IntentService {

    public SincronizaMensagens(){
        super("SincronizaMensagens");
    }

    public SincronizaMensagens(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        RepositorioMensagem repositorio = new RepositorioMensagem(SincronizaMensagens.this);
        List<Mensagem> mensagens = repositorio.findNaoSincronizadas();
        if(mensagens.size() > 0){
            MensagemREST rest = new MensagemREST();
            String[] retorno = rest.enviarMensagensServidor(mensagens);
        }
    }
}