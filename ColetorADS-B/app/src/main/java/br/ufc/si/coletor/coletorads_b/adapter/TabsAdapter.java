package br.ufc.si.coletor.coletorads_b.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.ufc.si.coletor.coletorads_b.fragments.Mainfragment;

/**
 * Created by Guilherme on 07/08/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private Context context;
    private static final int QTD_TABS = 2;

    public TabsAdapter(Context context, FragmentManager fm){
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return QTD_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0){
            return "";
        }

        return "";
    }

    @Override
    public Fragment getItem(int position) {

        Bundle params = new Bundle();

        if(position == 0){
            params.putString("tab", "principal");
        }else if (position == 1){
            params.putString("tab", "secundaria");
        }

        Fragment fragment = new Mainfragment();
        fragment.setArguments(params);

        return fragment;
    }

}
