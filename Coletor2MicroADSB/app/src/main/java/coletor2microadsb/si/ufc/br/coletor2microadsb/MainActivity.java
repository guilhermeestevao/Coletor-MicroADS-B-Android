package coletor2microadsb.si.ufc.br.coletor2microadsb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import coletor2microadsb.si.ufc.br.coletor2microadsb.modelo.Mensagem;
import coletor2microadsb.si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;
import coletor2microadsb.si.ufc.br.coletor2microadsb.usb.CDCDevice;
import coletor2microadsb.si.ufc.br.coletor2microadsb.usb.UsbController;


public class MainActivity extends ActionBarActivity {


    private TextView info;
    private List<String> mensagens = new ArrayList<String>();
    private UsbController controller;
    private CDCDevice cdcDevice;
    private Button start;
    private RepositorioMensagem repositorio;
    private ListView list;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repositorio = new RepositorioMensagem(this);
        info = (TextView) findViewById(R.id.info);
        start = (Button) findViewById(R.id.enviar);
        list = (ListView) findViewById(R.id.lisView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mensagens);
        list.setAdapter(adapter);
        checkInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_list){
            Intent it = new Intent(this, ListaMensagens.class);
            startActivity(it);
        }



        return super.onOptionsItemSelected(item);
    }

    private void checkInfo() {
        controller = new UsbController(this, this.getIntent());
        final UsbDevice device = controller.getMicroAdsb();

        if (device != null) {
            cdcDevice = new CDCDevice(controller.getManager(), device, this);
            cdcDevice.setParameters(115200, 8, 1, 0);
        }

    }



    public void teste(View view) {
        String inicializacao = "#43-02";
        InitTask initTask =  new InitTask();
        initTask.execute(inicializacao);
        start.setEnabled(false);
    }

    class InitTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            byte[] bytes = new byte[64];
            String resultRead = null;
            int i;
            int pos = 1000;
            try {
                i = cdcDevice.read(bytes, 100);
                resultRead = new String(bytes, "UTF-8");
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(MainActivity.this, resultRead, Toast.LENGTH_LONG).show();
            MessageReciverTask reciver = new MessageReciverTask();
            reciver.execute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            String teste = strings[0] + "\r\n";
            try {
                int i = cdcDevice.write(teste.getBytes(), 1000);
                return i;
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return null;
        }
    }

    class MessageReciverTask extends AsyncTask<Void, Void, String> {

        byte[] buffer = new byte[64];
        String resultRead = null;
        int i = 0;


        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "Iniciando a captura...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            mensagens.add(s);
            adapter.notifyDataSetChanged();

            long timestamp = System.currentTimeMillis();

            Mensagem msg = new Mensagem(s, timestamp);
            repositorio.inserir(msg);

            this.execute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                i = cdcDevice.read(buffer, 100);

                if (i > 0) {

                    resultRead = new String(buffer, "UTF-8");

                    return resultRead;

                }

            } catch (IOException e) {
                return e.getMessage();
            }

            return "Coletor desconectado!";
        }

    }

}
