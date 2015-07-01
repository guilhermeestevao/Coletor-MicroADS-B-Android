package si.ufc.br.coletor2microadsb.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;;
import java.io.IOException;
import si.ufc.br.coletor2microadsb.R;
import si.ufc.br.coletor2microadsb.fragments.ListaMensagens;
import si.ufc.br.coletor2microadsb.modelo.Mensagem;
import si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;
import si.ufc.br.coletor2microadsb.usb.CDCDevice;

/**
 * Created by guilherme on 23/05/15.
 */
@SuppressWarnings("deprecation")
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

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
    protected String doInBackground(Void... voids) {
        try {
           while(!this.isCancelled()){
               publishProgress();
               int i = getCdcDevice().read(buffer, 100);
               if (i > 0) {
                   resultRead = new String(buffer).substring(0, i);
                   salvar(resultRead);
               }
           }
        } catch (IOException e) {
            return e.getMessage();
        }
        return "Coleta interrompida!";
    }

    @Override
    protected void onPostExecute(String s) {
        mBuilder.setContentText(s).setProgress(0,0,false);
        mNotifyManager.notify(id, mBuilder.getNotification());
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

}