package br.ufc.si.coletor.coletorads_b.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import br.ufc.si.coletor.coletorads_b.R;
import br.ufc.si.coletor.coletorads_b.interfaces.NewMessageListener;
import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;
import br.ufc.si.coletor.coletorads_b.usb.CDCDevice;
import br.ufc.si.coletor.coletorads_b.util.ColetorApplication;
import br.ufc.si.coletor.coletorads_b.util.SnackBarUtil;

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
    private boolean controle;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SnackBarUtil.getSuccessfulSnackbar(ColetorApplication.COORDINATOR_LAYOUT, "Coleta iniciada!", Snackbar.LENGTH_LONG).show();
        controle = true;
        Resources res = context.getResources();
        Bitmap icom = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        mNotifyManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new Notification.Builder(context);
        mBuilder.setContentTitle("Capturando mensagens")
                .setContentText("Aguardando a chegada de mensagens...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icom);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(id, mBuilder.getNotification());
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SnackBarUtil.getSuccessfulSnackbar(ColetorApplication.COORDINATOR_LAYOUT, s, Snackbar.LENGTH_LONG).show();
        mBuilder.setContentText(s).setProgress(0, 0, false);
        mNotifyManager.notify(id, mBuilder.getNotification());
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            boolean controleAux = controle;
            publishProgress();
            while(controleAux){
                int i = getCdcDevice().read(buffer, 100);
                if (i > 0) {
                    resultRead = new String(buffer).substring(0, i);
                    salvar(resultRead);
                }
                controleAux = controle;

            }
        } catch (IOException e) {
            return e.getMessage();
        }

        return "Coleta finalozada!";

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
                listener.onNewMenssge(m);
                getRepositorio().inserir(m);
            }catch (Exception e){
                continue;
            }
        }
    }

    public void parar(){
        cdcDevice.close();
        controle = false;

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