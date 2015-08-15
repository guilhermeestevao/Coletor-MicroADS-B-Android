package br.ufc.si.coletor.coletorads_b.dialogs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ufc.si.coletor.coletorads_b.R;
import br.ufc.si.coletor.coletorads_b.receivers.VerificaConexao;

/**
 * Created by guilherme on 09/08/15.
 */
public class DialogConfiguracoesRemotas extends DialogPreference {

    private EditText mIp;
    private EditText mPort;
    public static final String IP_SERVER = "ip_server";
    public static final String PORT_SERVER = "port_server";



    public DialogConfiguracoesRemotas(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.dialog_configutacao_remota);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mIp = (EditText) view.findViewById(R.id.ip_server);
        mPort = (EditText) view.findViewById(R.id.port_server);

       String ip = getSharedPreferences().getString(IP_SERVER, "");
       int porta = getSharedPreferences().getInt(PORT_SERVER, 0);

        mIp.setText(ip);
        mPort.setText(""+porta);


    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult){
            String ip = mIp.getText().toString();
            int port = Integer.parseInt(mPort.getText().toString());
            SharedPreferences.Editor editor =  getEditor();
            editor.putString(IP_SERVER, ip);
            editor.putInt(PORT_SERVER, port);
            editor.commit();
            Intent it = new Intent(getContext(), VerificaConexao.class);
            getContext().sendBroadcast(it);
        }


    }
}
