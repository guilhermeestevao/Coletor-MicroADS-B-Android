package br.ufc.si.coletor.coletorads_b.fragments;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import br.ufc.si.coletor.coletorads_b.R;
import br.ufc.si.coletor.coletorads_b.adapter.MensagemAdapter;
import br.ufc.si.coletor.coletorads_b.interfaces.NewMessageListener;
import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;
import br.ufc.si.coletor.coletorads_b.service.MessageReciverTask;
import br.ufc.si.coletor.coletorads_b.usb.CDCDevice;
import br.ufc.si.coletor.coletorads_b.usb.UsbController;

/**
 * Created by Guilherme on 07/08/2015.
 */
public class MainFragment extends Fragment implements NewMessageListener{

    private Context mContext;
    private static final String MENSAGEM = "#43-03\r\n";
    private UsbController controller;
    private CDCDevice cdcDevice;
    private MessageReciverTask receiver;
    private RepositorioMensagem repositorio;
    private RecyclerView mRecyclerView;
    private MensagemAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Mensagem> mensagens;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        receiver = new MessageReciverTask();
        repositorio = new RepositorioMensagem(mContext);

    }

    public static MainFragment newInstance(int position){
        MainFragment frag = new MainFragment();
        return frag;
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
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLayoutManager);
        
        mAdapter = new MensagemAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(cdcDevice != null){
            new InitColetor().execute();
        }else{

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

    @Override
    public void onNewMenssge(Mensagem msg) {

        mAdapter.add(mAdapter.getItemCount(), msg);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());

    }


    class InitColetor extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("init", s);
            receiver.setCdcDevice(cdcDevice);
            receiver.setRepositorio(repositorio);
            receiver.setAtivo(true);
            receiver.setContext(mContext);
            receiver.setListener(MainFragment.this);
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
