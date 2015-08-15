package br.ufc.si.coletor.coletorads_b.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import br.ufc.si.coletor.coletorads_b.dialogs.DialogConfiguracoesRemotas;
import br.ufc.si.coletor.coletorads_b.service.SocketCliente;

/**
 * Created by guilherme on 31/05/15.
 */
public class VerificaConexao extends BroadcastReceiver {

    private static String SINCRONIZAR_MENSGENS = "SINCRONIZAR_MENSGENS";

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean statusRede = hasInternet(context);
        boolean statusProcesso = isMyServiceRunning(SocketCliente.class, context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = prefs.getString(DialogConfiguracoesRemotas.IP_SERVER, "");
        int port = prefs.getInt(DialogConfiguracoesRemotas.PORT_SERVER, 0);

        if(statusRede && !statusProcesso && !ip.equals("") && port != 0){
            Intent it = new Intent(context, SocketCliente.class);
            context.startService(it);
        }

    }

    private boolean hasInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
            return true;

        return false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}