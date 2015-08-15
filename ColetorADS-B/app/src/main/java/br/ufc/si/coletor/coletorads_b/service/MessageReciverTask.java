package br.ufc.si.coletor.coletorads_b.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import br.ufc.si.coletor.coletorads_b.interfaces.NewMessageListener;
import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;
import br.ufc.si.coletor.coletorads_b.usb.CDCDevice;


/**
 * Created by guilherme on 23/05/15.
 */

public class MessageReciverTask extends AsyncTask<Void, Void, String> {

    private byte[] buffer = new byte[512];
    private String resultRead = null;
    private RepositorioMensagem repositorio;
    private CDCDevice cdcDevice;
    private boolean ativo;
    private Context context;
    private NotificationManager mNotifyManager;
    private Notification.Builder mBuilder ;
    private int id = 1;
    private final String TAG = "TAG";
    private NewMessageListener listener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
           while(true){
               int i = getCdcDevice().read(buffer, 100);
               if (i > 0) {
                   resultRead = new String(buffer).substring(0, i);
                   salvar(resultRead);
               }
           }
        } catch (IOException e) {
            return e.getMessage();
        }

    }


    private void salvar(String mensagem) {

        mensagem = mensagem.replaceAll("[\\t\\n\\r]"," ");
        mensagem = mensagem.replaceAll(" ", "");
        String msgs[] =  mensagem.split("@");
                for (int i = 1; i < msgs.length; i++) {
                    try {
                        String msg = msgs[i];
                        msg = msg.substring(12, mensagem.length() - 2);
                        Log.d("usb", msg);
                        long timestamp = System.currentTimeMillis();
                        Mensagem m = new Mensagem(msg, timestamp);
                        getRepositorio().inserir(m);
                        listener.onNewMenssge(m);
                    }catch (Exception e){
                        continue;
                    }
                }
    }

    public void parar(){
        onPostExecute("Coleta interrompida!");
    }

    public RepositorioMensagem getRepositorio() {
        return repositorio;
    }

    public void setRepositorio(RepositorioMensagem repositorio) {
        this.repositorio = repositorio;
    }

    public CDCDevice getCdcDevice() {
        return cdcDevice;
    }

    public void setCdcDevice(CDCDevice cdcDevice) {
        this.cdcDevice = cdcDevice;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public NewMessageListener getListener() {
        return listener;
    }

    public void setListener(NewMessageListener listener) {
        this.listener = listener;
    }
}