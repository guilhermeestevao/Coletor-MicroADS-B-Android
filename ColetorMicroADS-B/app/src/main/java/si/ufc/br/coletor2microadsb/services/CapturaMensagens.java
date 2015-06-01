package si.ufc.br.coletor2microadsb.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import si.ufc.br.coletor2microadsb.modelo.Mensagem;
import si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;
import si.ufc.br.coletor2microadsb.usb.CDCDevice;
import si.ufc.br.coletor2microadsb.usb.UsbController;

/**
 * Created by guilherme on 31/05/15.
 */
public class CapturaMensagens extends IntentService {

    private Context context;
    private boolean ativo;
    private UsbController controller;
    private CDCDevice cdcDevice;
    private RepositorioMensagem repositorio;
    private byte[] buffer = new byte[512];
    private String resultRead = null;

    public CapturaMensagens(){
        super("Captura_Mensagens");
    }

    public CapturaMensagens(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("status", "onStartCommand");
        context = CapturaMensagens.this;
        ativo = true;
        repositorio = new RepositorioMensagem(context);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("status", "onHandleIntent");
        Bundle b =intent.getExtras();
        ativo = b.getBoolean("status");
        while(ativo){
            int i = 0;
            try {
                i = getCdcDevice().read(buffer, 100);
                if (i > 0) {
                    resultRead = new String(buffer).substring(0, i);
                    Log.i("status", resultRead);
                    salvar(resultRead);
                }else{
                    Log.i("status", "nada");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                repositorio.inserir(m);
            }catch (Exception e){
                continue;
            }
        }
    }

    public UsbController getController() {
        return controller;
    }

    public void setController(UsbController controller) {
        this.controller = controller;
    }

    public CDCDevice getCdcDevice() {
        return cdcDevice;
    }

    public void setCdcDevice(CDCDevice cdcDevice) {
        this.cdcDevice = cdcDevice;
    }
}
