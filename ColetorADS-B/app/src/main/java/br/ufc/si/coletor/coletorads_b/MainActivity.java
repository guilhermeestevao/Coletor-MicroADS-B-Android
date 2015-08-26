package br.ufc.si.coletor.coletorads_b;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import br.ufc.si.coletor.coletorads_b.activity.BaseActivity;
import br.ufc.si.coletor.coletorads_b.receivers.VerificaConexao;
import br.ufc.si.coletor.coletorads_b.util.ColetorApplication;

public class MainActivity extends BaseActivity {

    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar =  (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.snackbar);
        Intent it = new Intent(this, VerificaConexao.class);
        sendBroadcast(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ColetorApplication.COORDINATOR_LAYOUT = mCoordinatorLayout;
    }
}
