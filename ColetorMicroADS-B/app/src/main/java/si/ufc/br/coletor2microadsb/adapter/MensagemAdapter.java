package si.ufc.br.coletor2microadsb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import si.ufc.br.coletor2microadsb.R;
import si.ufc.br.coletor2microadsb.modelo.Mensagem;

/**
 * Created by guilherme on 23/05/15.
 */
public class MensagemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Mensagem> mensagems = new ArrayList<Mensagem>();

    public MensagemAdapter(Context ctx, List<Mensagem> mensagens){
        this.mensagems = mensagens;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mensagems.size();
    }

    @Override
    public Object getItem(int i) {
        return mensagems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Mensagem msg = mensagems.get(i);
        View v = inflater.inflate(R.layout.mensagem_layout, null);
        TextView text = (TextView)v.findViewById(R.id.mensagem);
        text.setText(msg.toString());
        return v;
    }
}
