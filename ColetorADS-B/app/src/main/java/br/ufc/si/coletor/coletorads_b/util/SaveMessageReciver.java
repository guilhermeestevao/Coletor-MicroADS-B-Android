package br.ufc.si.coletor.coletorads_b.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by guilherme on 27/10/15.
 */
public class SaveMessageReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent it = new Intent(context, SaveMessageFile.class);
        context.startService(it);
    }
}
