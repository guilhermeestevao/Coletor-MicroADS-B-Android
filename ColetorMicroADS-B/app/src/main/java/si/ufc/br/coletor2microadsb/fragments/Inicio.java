package si.ufc.br.coletor2microadsb.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import si.ufc.br.coletor2microadsb.R;

public class Inicio extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Inicio newInstance(int sectionNumber) {
        Inicio fragment = new Inicio();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Inicio() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_inicio, container, false);

        return v;
    }

}
