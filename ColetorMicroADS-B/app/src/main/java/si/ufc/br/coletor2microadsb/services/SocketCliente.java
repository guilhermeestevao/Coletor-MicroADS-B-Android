package si.ufc.br.coletor2microadsb.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

import si.ufc.br.coletor2microadsb.modelo.Mensagem;
import si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;

/**
 * Created by guilherme on 04/06/15.
 */

public class SocketCliente extends IntentService{

    private RepositorioMensagem repositorio;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private String host;
    private int port;
    private int timeout;
    private boolean conectado;

    public SocketCliente(String name) {
        super(name);
    }

    public SocketCliente() {
        super("SincronizaMensagens");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("connection", "onCreate do service");
        repositorio = new RepositorioMensagem(this);
        this.host = "192.168.0.104";
        this.port = 5000;
        this.timeout = 10000;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

            SocketAddress sockaddr = new InetSocketAddress(host, port);
            socket = new Socket();
            try {
                socket.connect(sockaddr, timeout);
                conectado = true;
                while(conectado){
                    List<Mensagem> mensagens = repositorio.findNaoSincronizadas();
                    Log.i("connection", "Encontradas "+mensagens.size());
                    is = socket.getInputStream();
                    os = socket.getOutputStream();
                    enviar(mensagens);
                    Thread.sleep(500);
                }

            } catch (IOException e) {
                Log.e("connection", "Erro de entrada e saida", e);
                conectado = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        conectado = false;
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
            Log.i("connection", msg);
            os.write(msg.getBytes());
            mensagem.sinc = 1;
            repositorio.atualizar(mensagem);

        }
    }


}
