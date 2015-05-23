package si.ufc.br.coletor2microadsb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import si.ufc.br.coletor2microadsb.R;
import si.ufc.br.coletor2microadsb.fragments.Configuracoes;
import si.ufc.br.coletor2microadsb.fragments.Inicio;
import si.ufc.br.coletor2microadsb.fragments.ListaMensagens;
import si.ufc.br.coletor2microadsb.fragments.PlaceholderFragment;

/**
 * Created by guilherme on 23/05/15.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return Inicio.newInstance(position+1);
            case 1: return ListaMensagens.newInstance(position+1);
            case 2: return new Configuracoes();
        }

        return null;

    }

    @Override
    public int getCount() {

        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Início";
            case 1:
                return "Mensagens";
            case 2:
                return "Configurações";
        }
        return null;
    }
}