package coletor2microadsb.si.ufc.br.coletor2microadsb.fragments;

import android.app.Activity;
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

import coletor2microadsb.si.ufc.br.coletor2microadsb.R;
import coletor2microadsb.si.ufc.br.coletor2microadsb.modelo.Mensagem;
import coletor2microadsb.si.ufc.br.coletor2microadsb.modelo.RepositorioMensagem;
import coletor2microadsb.si.ufc.br.coletor2microadsb.usb.CDCDevice;
import coletor2microadsb.si.ufc.br.coletor2microadsb.usb.UsbController;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Inicio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inicio.
     */
    // TODO: Rename and change types and number of parameters
    public static Inicio newInstance(String param1, String param2) {
        Inicio fragment = new Inicio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Inicio() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        return v;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    private void checkInfo() {
        controller = new UsbController(getActivity(), getActivity().getIntent());
        final UsbDevice device = controller.getMicroAdsb();

        if (device != null) {
            cdcDevice = new CDCDevice(controller.getManager(), device, getActivity());
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