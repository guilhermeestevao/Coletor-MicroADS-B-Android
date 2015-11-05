package br.ufc.si.coletor.coletorads_b.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import br.ufc.si.coletor.coletorads_b.fragments.SettingsFragment;
import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;

/**
 * Created by guilherme on 24/09/15.
 */
public class ExcluiMensagens extends IntentService {


    public ExcluiMensagens() {
        super("ExcluirMensagens");
    }

    public ExcluiMensagens(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        int forma = Integer.parseInt(sharedPref.getString(SettingsFragment.FORMA, "1"));

        RepositorioMensagem repositorio = new RepositorioMensagem(this);


        if(forma == 1) {

            Log.i("apagar", "executou o comportamento");

        }else{

            int quantidadeAtual = repositorio.getQuantidadeMensagens();
            Log.i("apagar", "Atual :"+quantidadeAtual);
            int quantidadePermitida = Integer.parseInt(sharedPref.getString(SettingsFragment.QUANTIDADE, "0"));
            Log.i("apagar", "Permitida :"+quantidadePermitida);
            if(quantidadeAtual >= quantidadePermitida)
                repositorio.removerTudo();

        }


    }
}
