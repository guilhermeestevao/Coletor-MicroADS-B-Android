package si.ufc.br.coletor2microadsb.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by guilherme on 31/05/15.
 */
public class VerificaConexao extends BroadcastReceiver {

    private static String SINCRONIZAR_MENSGENS = "SINCRONIZAR_MENSGENS";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info != null){
            if(info.isConnected()){
                Log.i("connection", "conectado!");
                context.startService(new Intent(context, SincronizaMensagens.class));
            }
        }
    }
}