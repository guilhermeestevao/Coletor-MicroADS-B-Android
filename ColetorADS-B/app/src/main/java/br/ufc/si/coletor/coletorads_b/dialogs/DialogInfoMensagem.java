package br.ufc.si.coletor.coletorads_b.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;

import br.ufc.si.coletor.coletorads_b.R;

/**
 * Created by guilherme on 02/09/15.
 */
public class DialogInfoMensagem extends DialogFragment {

    private Map<String, String> infos;

    public static DialogInfoMensagem newInstance(Map<String, String> data){

        DialogInfoMensagem frag = new DialogInfoMensagem();
        frag.infos = data;
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_message_decoded, null);

        TextView data = (TextView) view.findViewById(R.id.decoded_message);

        Set<String> keys = infos.keySet();

        StringBuilder string = new StringBuilder();

        for (String key : keys){
            string.append(infos.get(key)+"\n");
        }

        data.setText(string);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

}
