package br.ufc.si.coletor.coletorads_b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.machinarius.preferencefragment.PreferenceFragment;

import br.ufc.si.coletor.coletorads_b.R;

/**
 * Created by guilherme on 09/08/15.
 */
public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment newInstance(int position){
        SettingsFragment frag = new SettingsFragment();
        return frag;
    }

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.preferences);
    }
}
