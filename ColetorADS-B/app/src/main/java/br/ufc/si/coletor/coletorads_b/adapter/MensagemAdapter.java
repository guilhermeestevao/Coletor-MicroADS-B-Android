package br.ufc.si.coletor.coletorads_b.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufc.si.coletor.coletorads_b.R;
import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;
import br.ufc.si.coletor.coletorads_b.service.MessageReciverTask;


/**
 * Created by guilherme on 23/05/15.
 */
public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.ViewHolder>{

    private List<Mensagem> mDataset;
    private Context context;

    public MensagemAdapter(Context mContext) {
        this.context = mContext;
        RepositorioMensagem repositorio = new RepositorioMensagem(mContext);
        mDataset = repositorio.findAll();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mesagem;

        public ViewHolder(View v) {
            super(v);
            mesagem = (TextView) v.findViewById(R.id.mensagem);
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

        holder.mesagem.setText(position+" - "+mensagem.toString());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(int position, Mensagem item) {
        mDataset.add(item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }



}
