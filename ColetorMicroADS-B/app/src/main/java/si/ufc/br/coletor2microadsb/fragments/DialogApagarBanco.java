package si.ufc.br.coletor2microadsb.fragments;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import si.ufc.br.coletor2microadsb.R;
import si.ufc.br.coletor2microadsb.SetingsActivity;
import si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;

/**
 * Created by guilherme on 26/05/15.
 */
public class DialogApagarBanco extends DialogPreference {

    private RepositorioMensagem repositorio;


    public DialogApagarBanco(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        persistBoolean(positiveResult);
        repositorio = new RepositorioMensagem(getContext());
        if(positiveResult){
            repositorio.removerTudo();
        }
    }
}