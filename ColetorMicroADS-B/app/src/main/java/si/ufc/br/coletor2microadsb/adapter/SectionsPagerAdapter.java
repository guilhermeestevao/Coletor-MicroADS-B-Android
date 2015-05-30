package si.ufc.br.coletor2microadsb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.Locale;
import si.ufc.br.coletor2microadsb.fragments.Inicio;
import si.ufc.br.coletor2microadsb.fragments.ListaMensagens;

/**
 * Created by guilherme on 23/05/15.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Log.i("usb", "Position: " + position);
        switch (position){
            case 0: return Inicio.newInstance(position+1);
            case 1: return ListaMensagens.newInstance(position+1);
        }
        return null;

    }

    @Override
    public int getCount() {

        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Início";
            case 1:
                return "Mensagens";

        }
        return null;
    }
}