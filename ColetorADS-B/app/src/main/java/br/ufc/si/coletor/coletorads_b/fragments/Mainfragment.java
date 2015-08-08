package br.ufc.si.coletor.coletorads_b.fragments;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import br.ufc.si.coletor.coletorads_b.R;
import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;
import br.ufc.si.coletor.coletorads_b.service.MessageReciverTask;
import br.ufc.si.coletor.coletorads_b.usb.CDCDevice;
import br.ufc.si.coletor.coletorads_b.usb.UsbController;

/**
 * Created by Guilherme on 07/08/2015.
 */
public class Mainfragment extends Fragment {

    private Context mContext;
    private static final String MENSAGEM = "#43-03\r\n";
    private UsbController controller;
    private CDCDevice cdcDevice;
    private TextView mTextView;
    private MessageReciverTask receiver;
    private RepositorioMensagem repositorio;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        receiver = new MessageReciverTask();
        repositorio = new RepositorioMensagem(mContext);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mTextView = (TextView) v.findViewById(R.id.teste);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(cdcDevice != null){
            new InitColetor().execute();
        }else{
           List<Mensagem> mensagens =  repositorio.findAll();
            mTextView.setText(""+mensagens.size());
        }
    }

    private void checkInfo() {
        controller = new UsbController(mContext, getActivity().getIntent());
        UsbDevice device = controller.getMicroAdsb();
        if (device != null) {
            cdcDevice = new CDCDevice(controller.getManager(), device, mContext);
            cdcDevice.setParameters(115200, 8, 1, 0);
        }
    }

    class InitColetor extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            receiver.setCdcDevice(cdcDevice);
            receiver.setRepositorio(repositorio);
            receiver.setAtivo(true);
            receiver.setContext(mContext);
            receiver.execute();

        }

        @Override
        protected String doInBackground(Void... voids) {

            byte[] bytes = new byte[64];
            String resultRead = null;
            try {
                int i = cdcDevice.write(MENSAGEM.getBytes(), 100);
                i = cdcDevice.read(bytes, 1000);
                resultRead = new String(bytes, "UTF-8");
                return  resultRead;
            } catch (IOException e) {
                return e.getMessage();
            }
        }
    }
}
