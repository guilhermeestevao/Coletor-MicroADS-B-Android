package coletor2microadsb.si.ufc.br.coletor2microadsb;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import coletor2microadsb.si.ufc.br.coletor2microadsb.modelo.Mensagem;
import coletor2microadsb.si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;


public class ListaMensagens extends ListActivity {

    private RepositorioMensagem repositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repositorio = new RepositorioMensagem(this);

        List<Mensagem> msgs = repositorio.findAll();
        List<String> lista = lista(msgs);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        setListAdapter(adapter);
    }


    private List<String> lista(List<Mensagem> msgs){
        List<String> lista = new ArrayList<String>();
        for (Mensagem m : msgs){
            lista.add(m.data);
        }
        return lista;
    }

}
