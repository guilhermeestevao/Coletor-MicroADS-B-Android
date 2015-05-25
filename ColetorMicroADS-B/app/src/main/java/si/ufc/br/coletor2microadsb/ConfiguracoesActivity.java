package si.ufc.br.coletor2microadsb;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by guilherme on 24/05/15.
 */
public class ConfiguracoesActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_configuracoes);
    }

}
