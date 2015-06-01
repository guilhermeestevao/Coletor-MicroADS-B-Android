package si.ufc.br.coletor2microadsb.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import si.ufc.br.coletor2microadsb.R;
import si.ufc.br.coletor2microadsb.adapter.MensagemAdapter;
import si.ufc.br.coletor2microadsb.modelo.Mensagem;
import si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;

public class ListaMensagens extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RepositorioMensagem repositorio;
    private MensagemAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<Mensagem>();

    // TODO: Rename and change types and number of parameters
    public static ListaMensagens newInstance(int sectionNumber) {
        ListaMensagens fragment = new ListaMensagens();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ListaMensagens() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repositorio = new RepositorioMensagem(getActivity());
        try {
            mensagens = repositorio.findAll();
            adapter = new MensagemAdapter(getActivity(), mensagens);
        }catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_mensagens, container, false);
        ListView list = (ListView) v.findViewById(R.id.listView);
        list.setAdapter(adapter);
        return v;
    }

    public void notifacaNovamensagem(){
        adapter.notifyDataSetChanged();
        Log.i("TAG", "modificado");
    }

}
