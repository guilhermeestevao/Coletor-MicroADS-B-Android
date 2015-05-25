package si.ufc.br.coletor2microadsb.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import si.ufc.br.coletor2microadsb.MainActivity;
import si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;
import si.ufc.br.coletor2microadsb.usb.CDCDevice;

/**
 * Created by guilherme on 23/05/15.
 */
public class CapturaMensagens extends Service{

    private boolean ativo = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }
}
