package br.ufc.si.coletor.coletorads_b.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.software.shell.fab.ActionButton;

import java.io.IOException;
import java.util.List;

import br.ufc.si.coletor.coletorads_b.R;
import br.ufc.si.coletor.coletorads_b.adapter.MensagemAdapter;
import br.ufc.si.coletor.coletorads_b.interfaces.ClearMessagesList;
import br.ufc.si.coletor.coletorads_b.interfaces.NewMessageListener;
import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;
import br.ufc.si.coletor.coletorads_b.service.MessageReciverTask;
import br.ufc.si.coletor.coletorads_b.usb.CDCDevice;
import br.ufc.si.coletor.coletorads_b.usb.UsbController;
import br.ufc.si.coletor.coletorads_b.util.ColetorApplication;
import br.ufc.si.coletor.coletorads_b.util.SnackBarUtil;

/**
 * Created by Guilherme on 07/08/2015.
 */
public class MainFragment extends Fragment implements NewMessageListener, ClearMessagesList{

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
    private SharedPreferences mPreferences;
    private static final String AUTO_START = "autorun";
    private ActionButton mActionButton;
    private boolean controle;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        repositorio = new RepositorioMensagem(mContext);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        checkInfo();
    }

    public static MainFragment newInstance(int position){
        MainFragment frag = new MainFragment();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mActionButton = (ActionButton) v.findViewById(R.id.action_button);
        ColetorApplication.RECYCLEVIEW = mRecyclerView;

        boolean autoStart = mPreferences.getBoolean(AUTO_START, false);

        if(cdcDevice == null){
            SnackBarUtil.getUnsuccessfulSnackbar(ColetorApplication.COORDINATOR_LAYOUT, "Desculpe, é necessario conectar o Micro ADS-B", Snackbar.LENGTH_LONG).show();
            mActionButton.hide();
        }



        if(cdcDevice != null && autoStart){
            new InitColetor().execute();
            SnackBarUtil.getSuccessfulSnackbar(ColetorApplication.COORDINATOR_LAYOUT, "Coleta iniciada!", Snackbar.LENGTH_LONG).show();
            mActionButton.setState(ActionButton.State.PRESSED);
        }else{
            controle = false;
            mActionButton.show();
        }

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!controle) {
                    mActionButton.setButtonColor(getResources().getColor(R.color.fab_material_lime_900));
                    controle = !controle;
                    Log.e("controle", "Controle: "+controle);
                    new InitColetor().execute();

                } else {
                    mActionButton.setButtonColor(getResources().getColor(R.color.fab_material_lime_500));
                    receiver.parar();
                    controle = !controle;
                }
            }
        });
        
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MensagemAdapter(mContext, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



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

    private void init(){
        receiver = new MessageReciverTask();
        receiver.setCdcDevice(cdcDevice);
        receiver.setRepositorio(repositorio);
        receiver.setAtivo(true);
        receiver.setContext(mContext);
        receiver.setListener(MainFragment.this);
        receiver.execute();
    }

    @Override
    public void clearList() {
        mensagens.clear();
        mAdapter.notifyDataSetChanged();
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
            init();
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
