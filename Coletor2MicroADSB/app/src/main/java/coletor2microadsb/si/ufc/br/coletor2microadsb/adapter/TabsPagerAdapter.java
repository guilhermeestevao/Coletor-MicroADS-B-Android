package coletor2microadsb.si.ufc.br.coletor2microadsb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import coletor2microadsb.si.ufc.br.coletor2microadsb.fragments.Configuracoes;
import coletor2microadsb.si.ufc.br.coletor2microadsb.fragments.Inicio;
import coletor2microadsb.si.ufc.br.coletor2microadsb.fragments.ListaMensagens;

/**
 * Created by guilherme on 22/05/15.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {


    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new Inicio();
            case 1: return new ListaMensagens();
            case 2: return new Configuracoes();
        }
        
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
