package si.ufc.br.coletor2microadsb;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import si.ufc.br.coletor2microadsb.adapter.SectionsPagerAdapter;
import si.ufc.br.coletor2microadsb.fragments.ListaMensagens;
import si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;
import si.ufc.br.coletor2microadsb.services.CapturaMensagens;
import si.ufc.br.coletor2microadsb.services.MessageReciverTask;
import si.ufc.br.coletor2microadsb.usb.CDCDevice;
import si.ufc.br.coletor2microadsb.usb.UsbController;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity implements ActionBar.TabListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String MENSAGEM = "#43-03\r\n";
    private UsbController controller;
    private CDCDevice cdcDevice;
    private ViewPager mViewPager;
    private RepositorioMensagem repositorio;
    private MessageReciverTask receiver;
    private boolean ativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInfo();
        ativo = false;
        receiver = new MessageReciverTask();
        repositorio = new RepositorioMensagem(this);
        setContentView(R.layout.activity_main);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });


        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent it = new Intent(this, SetingsActivity.class);
            startActivity(it);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public void iniciar(View view){
        ImageView img = (ImageView) view;

        if(receiver.isAtivo()) {
            receiver.setAtivo(false);
            receiver.parar();
            img.setImageResource(R.drawable.not_ative);
        }else{
            img.setImageResource(R.drawable.active);
            new InitColetor().execute();
        }

    }

    public void apagar(View view){
        repositorio.removerTudo();
    }


    private void checkInfo() {
        controller = new UsbController(this, getIntent());
        UsbDevice device = controller.getMicroAdsb();
        if (device != null) {
            cdcDevice = new CDCDevice(controller.getManager(), device, this);
            cdcDevice.setParameters(115200, 8, 1, 0);
        }
    }

    class InitColetor extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            receiver.setCdcDevice(cdcDevice);
            receiver.setRepositorio(repositorio);
            receiver.setAtivo(true);
            receiver.setContext(MainActivity.this);
            receiver.execute();

        }

        @Override
        protected String doInBackground(Void... voids) {

            byte[] bytes = new byte[64];
            String resultRead = null;
            try {
                int i = cdcDevice.write(MENSAGEM.getBytes(), 100);
                i = cdcDevice.read(bytes, 100);
                resultRead = new String(bytes, "UTF-8");
                return  resultRead;
            } catch (IOException e) {
                return e.getMessage();
            }
        }
    }
}