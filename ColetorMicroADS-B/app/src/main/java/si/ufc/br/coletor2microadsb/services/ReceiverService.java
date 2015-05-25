package si.ufc.br.coletor2microadsb.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by guilherme on 25/05/15.
 */
public class ReceiverService extends IntentService {

    public ReceiverService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
