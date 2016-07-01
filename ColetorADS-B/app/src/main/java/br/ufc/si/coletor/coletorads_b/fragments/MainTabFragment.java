package br.ufc.si.coletor.coletorads_b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.ufc.si.coletor.coletorads_b.R;
import br.ufc.si.coletor.coletorads_b.adapter.TabsAdapter;

/**
 * Created by Guilherme on 07/08/2015.
 */
public class MainTabFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TabsAdapter tabsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tab, container);

        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerTabs);
        mViewPager.setOffscreenPageLimit(1);
        //Adapter de TabLayout
        tabsAdapter = new TabsAdapter(getActivity(), getChildFragmentManager());

        mViewPager.setAdapter(tabsAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        int corUnselected = getActivity().getResources().getColor(R.color.secondary_text);
        int corSelected = getActivity().getResources().getColor(R.color.control_highlight);
        tabLayout.setTabTextColors(corUnselected,corSelected);//mudar cor de tab selecionada

        TabLayout.Tab tabPrincipal = tabLayout.newTab();
        tabPrincipal.setText(R.string.tab_principal);

        TabLayout.Tab tabSecundaria= tabLayout.newTab();
        tabSecundaria.setText(R.string.tab_secundaria);

        tabLayout.addTab(tabPrincipal);
        tabLayout.addTab(tabSecundaria);

        //Listener de tab selecionada
        tabLayout.setOnTabSelectedListener(this);

        //Listener pra ViewPager

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
