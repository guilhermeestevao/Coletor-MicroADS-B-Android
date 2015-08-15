package br.ufc.si.coletor.coletorads_b.dialogs;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import br.ufc.si.coletor.coletorads_b.modelo.RepositorioMensagem;


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