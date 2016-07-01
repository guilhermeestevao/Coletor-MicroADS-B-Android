package br.ufc.si.coletor.coletorads_b.service;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;
import br.ufc.si.coletor.coletorads_b.util.ColetorApplication;

/**
 * Created by guilherme on 24/08/15.
 */
public class SincronizaMensagens extends IntentService {

    private InputStream in;
    private OutputStream out;
    private RepositorioMensagem repositorio;

    public SincronizaMensagens(String name) {
        super(name);
    }

    public SincronizaMensagens(){
        super("Sincronizador");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        repositorio = new RepositorioMensagem(this);
        try {
            in = ColetorApplication.SOCKET_SERVIDOR.getInputStream();
            out = ColetorApplication.SOCKET_SERVIDOR.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("connection", "Sincroniza chamado!");
        while(ColetorApplication.SOCKET_SERVIDOR.isConnected()){
            List<Mensagem> mensagens = repositorio.findNaoSincronizadas();
            Log.i("connection", "Encontradas " + mensagens.size());

            try {
                enviar(mensagens);
                Thread.sleep(500);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    private void enviar(List<Mensagem> mensgaens) throws IOException {
        for (Mensagem mensagem : mensgaens){

            //Formatando para enviar ao servidor
            //["[["\8D4841C458B985AAE19DBAE812C0\", null, 1433208074151]", 1433538871008, "RLC-0001A", "ADSBHEXDATA", 0, 0]
            //["[[\"5DE48FFB2F56D2\", null, 1433540908.13], [\"8DE48FFB584F6117915677D3AB72\", null, 1433540908.48], [\"8DE48FFB991476B22038232A1BAD\", null, 1433540908.63]]", 1433540908.758625, "RLC-0001A", "ADSBHEXDATA", 0, 0]


            long time = System.currentTimeMillis();

            //["[[\"8D4841C49904ECB7280B5CE3E96C\", null, 1433208044104]", 1433541340805, "RLC-0001A", "ADSBHEXDATA", 0, 0]

            String msg = "[\"[";
            msg+="[\\\""+mensagem.data+"\\\", ";
            msg+="null, ";
            msg+=mensagem.timestamp+"]]";
            msg+="\", ";
            msg+=time+", ";
            msg+="\"RLC-0001A\", \"ADSBHEXDATA\", 0, 0]";

            out.write(msg.getBytes());

            mensagem.sinc = 1;
            repositorio.atualizar(mensagem);

        }
    }

}
