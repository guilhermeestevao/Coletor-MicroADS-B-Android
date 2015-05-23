package si.ufc.br.coletor2microadsb.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import si.ufc.br.coletor2microadsb.R;



public class Configuracoes extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types and number of parameters
    public static Configuracoes newInstance(int sectionNumber) {
        Configuracoes fragment = new Configuracoes();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Configuracoes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuracoes, container, false);
    }


}
