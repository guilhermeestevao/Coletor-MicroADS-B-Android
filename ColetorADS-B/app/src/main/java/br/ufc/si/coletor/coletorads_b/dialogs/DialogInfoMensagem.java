package br.ufc.si.coletor.coletorads_b.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Map;

import br.ufc.si.coletor.coletorads_b.R;

/**
 * Created by guilherme on 02/09/15.
 */
public class DialogInfoMensagem extends DialogFragment {

    Map<String, String> infos;

    public static DialogInfoMensagem newInstance(){

        DialogInfoMensagem frag = new DialogInfoMensagem();
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_message_decoded, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

}
