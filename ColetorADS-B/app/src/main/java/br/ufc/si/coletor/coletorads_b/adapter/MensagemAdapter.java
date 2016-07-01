package br.ufc.si.coletor.coletorads_b.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufc.si.coletor.coletorads_b.R;
import br.ufc.si.coletor.coletorads_b.dialogs.DialogInfoMensagem;
import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;
import br.ufc.si.coletor.coletorads_b.service.MessageReciverTask;
import br.ufc.si.coletor.coletorads_b.util.ColetorApplication;
import br.ufc.si.coletor.coletorads_b.util.DecoderADSB;


/**
 * Created by guilherme on 23/05/15.
 */
public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.ViewHolder>{

    private List<Mensagem> mDataset;
    private Context context;
    private DecoderADSB decoder;
    private FragmentManager mManager;

    public MensagemAdapter(Context mContext, FragmentManager manager) {
        this.context = mContext;
        RepositorioMensagem repositorio = new RepositorioMensagem(mContext);
        mManager = manager;
        mDataset = repositorio.findAll();
        ColetorApplication.MENSAGENS = mDataset;
        decoder = new DecoderADSB();
        for(Mensagem msg : mDataset){
            msg.infos = getMapInfo(msg.data);
        }

    }

    private Map<String, String> getMapInfo(String data){
        Map<String, String> infoList = new HashMap<String, String>();
        try {
            infoList = decoder.decodeMsg(System.currentTimeMillis(), data);
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        public TextView mesagem;
        public TextView tipo;
        public TextView icao;
        public TextView data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mesagem = (TextView) v.findViewById(R.id.mensagem);
            tipo = (TextView) v.findViewById(R.id.tipo);
            icao = (TextView) v.findViewById(R.id.icao);
            data = (TextView) v.findViewById(R.id.data);
        }

        @Override
        public void onClick(View view) {
            Mensagem msg = mDataset.get(getPosition());
            try {
                DialogInfoMensagem dialog = DialogInfoMensagem.newInstance(msg.infos);
                dialog.show(mManager, "dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mensagem_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Mensagem mensagem = mDataset.get(position);
        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
            holder.mesagem.setText(mensagem.data);
            holder.data.setText(dateFormat.format(new Date(mensagem.timestamp)));
            holder.icao.setText(decoder.getIcao24(mensagem.timestamp, mensagem.data));
            holder.tipo.setText(decoder.getTypeMessage(mensagem.data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(int position, Mensagem item) {
        item.infos  = getMapInfo(item.data);
        mDataset.add(item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
}
