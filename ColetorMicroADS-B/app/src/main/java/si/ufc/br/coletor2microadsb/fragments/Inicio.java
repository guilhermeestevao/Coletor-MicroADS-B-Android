package si.ufc.br.coletor2microadsb.fragments;

import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import si.ufc.br.coletor2microadsb.R;
import si.ufc.br.coletor2microadsb.modelo.Mensagem;
import si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;
import si.ufc.br.coletor2microadsb.usb.CDCDevice;
import si.ufc.br.coletor2microadsb.usb.UsbController;



public class Inicio extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView info;
    private List<String> mensagens = new ArrayList<String>();
    private UsbController controller;
    private CDCDevice cdcDevice;
    private Button start;
    private RepositorioMensagem repositorio;
    private ListView list;
    private ArrayAdapter<String> adapter;


    // TODO: Rename and change types and number of parameters
    public static Inicio newInstance(int sectionNumber) {
        Inicio fragment = new Inicio();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Inicio() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_inicio, container, false);
        repositorio = new RepositorioMensagem(getActivity());
        info = (TextView) v.findViewById(R.id.info);
        start = (Button) v.findViewById(R.id.enviar);
        list = (ListView) v.findViewById(R.id.lisView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mensagens);
        list.setAdapter(adapter);
        checkInfo();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inicializacao = "#43-02";
                Toast.makeText(getActivity(), inicializacao, Toast.LENGTH_LONG).show();
                InitTask initTask = new InitTask();
                initTask.execute(inicializacao);
                start.setEnabled(false);

            }
        });

        return v;
    }



    private void checkInfo() {
        controller = new UsbController(getActivity(), getActivity().getIntent());
        final UsbDevice device = controller.getMicroAdsb();

        if (device != null) {
            cdcDevice = new CDCDevice(controller.getManager(), device, getActivity());
            cdcDevice.setParameters(115200, 8, 1, 0);
        }

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
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getActivity(), resultRead, Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), "Iniciando a captura...", Toast.LENGTH_SHORT).show();
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
