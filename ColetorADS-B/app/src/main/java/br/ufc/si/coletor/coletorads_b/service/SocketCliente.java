package br.ufc.si.coletor.coletorads_b.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Calendar;
import java.util.List;

import br.ufc.si.coletor.coletorads_b.dialogs.DialogConfiguracoesRemotas;
import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;
import br.ufc.si.coletor.coletorads_b.receivers.VerificaConexao;
import br.ufc.si.coletor.coletorads_b.util.ColetorApplication;
import br.ufc.si.coletor.coletorads_b.util.SnackBarUtil;


/**
 * Created by guilherme on 04/06/15.
 */

public class SocketCliente extends IntentService{

    private RepositorioMensagem repositorio;
    private Socket socket;
    private String host;
    private int port;
    private int timeout;
    private long delay = 60*1000;

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.host = prefs.getString(DialogConfiguracoesRemotas.IP_SERVER, "");
        this.port = prefs.getInt(DialogConfiguracoesRemotas.PORT_SERVER, 0);
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
                if(ColetorApplication.SOCKET_SERVIDOR == null) {
                    ColetorApplication.SOCKET_SERVIDOR = socket;
                    SnackBarUtil.getSuccessfulSnackbar(ColetorApplication.COORDINATOR_LAYOUT, "Conectado ao servidor " + host + " na porta " + port, Snackbar.LENGTH_LONG).show();
                    Intent it = new Intent(this, SincronizaMensagens.class);
                    startService(it);
                }

            } catch (IOException e) {
                if(ColetorApplication.COORDINATOR_LAYOUT != null)
                    SnackBarUtil.getWarningSnackbar(ColetorApplication.COORDINATOR_LAYOUT, "Não foi possivel se conectar ao servidor", Snackbar.LENGTH_LONG).show();

                agendar();
            }
    }

    private void agendar() {
        Intent it = new Intent(SocketCliente.this, VerificaConexao.class);
        PendingIntent p = PendingIntent.getBroadcast(SocketCliente.this, 0, it, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 60);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = calendar.getTimeInMillis();
        manager.setRepeating(AlarmManager.RTC_WAKEUP, time, delay, p);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("connection", "onDestroy do service");
    }



}
