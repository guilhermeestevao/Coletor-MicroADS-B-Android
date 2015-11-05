package br.ufc.si.coletor.coletorads_b.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.machinarius.preferencefragment.PreferenceFragment;

import java.util.Calendar;

import br.ufc.si.coletor.coletorads_b.R;
import br.ufc.si.coletor.coletorads_b.service.ExcluiMensagens;

/**
 * Created by guilherme on 09/08/15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String AUTOEXCLUSAO = "autoexclusao";
    public static final String FORMA = "forma";
    public static final String INTERVALO_DE_TEMPO = "Intervalo de tempo";
    public static final String TEMPO = "tempo_tipo";
    public static final String QUANTIDADE = "quantidade";


    public static SettingsFragment newInstance(int position){
        SettingsFragment frag = new SettingsFragment();
        return frag;
    }

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.preferences);
        if(getPreferenceManager().getSharedPreferences().contains(QUANTIDADE)){
            iniciar();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void iniciar() {
        ListPreference forma = (ListPreference) findPreference(FORMA);
        String sFotma = forma.getEntry().toString();
        forma.setSummary(sFotma);
        ListPreference tempo = (ListPreference) findPreference(TEMPO);
        tempo.setSummary("Período: " + tempo.getEntry());
        int tipo = Integer.parseInt(forma.getValue());

        if (tipo == 2) {
            tempo.setEnabled(false);
        }

        EditTextPreference eQuantidade = (EditTextPreference) findPreference(QUANTIDADE);
        int quantidade = Integer.parseInt(eQuantidade.getText());
        eQuantidade.setSummary("Quantidade: " + quantidade);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d("s", s);

        if(s.equals(FORMA)){
            ListPreference forma = (ListPreference) findPreference(FORMA);
            String sFotma = forma.getEntry().toString();
            forma.setSummary(sFotma);
            int tipo = Integer.parseInt(forma.getValue());

            ListPreference tempo = (ListPreference) findPreference(TEMPO);
            if(tipo == 2){
                tempo.setEnabled(false);
            }else{
                tempo.setEnabled(true);
                agendarExclusao();
            }

        } else if (s.equals(TEMPO)) {
            ListPreference tempo = (ListPreference) findPreference(TEMPO);
            tempo.setSummary("Período: " + tempo.getEntry());
            int value = Integer.parseInt(tempo.getValue());
            agendarExclusao();

        }else if(s.equals(QUANTIDADE)){
            EditTextPreference eQuantidade = (EditTextPreference) findPreference(QUANTIDADE);
            int quantidade = Integer.parseInt(eQuantidade.getText());
            eQuantidade.setSummary("Quantidade: " + quantidade);
            agendarExclusao();
        }
    }

    private void agendarExclusao() {

        //cancel();

        SwitchPreference s = (SwitchPreference) findPreference(AUTOEXCLUSAO);
        boolean status = s.isChecked();

        ListPreference forma = (ListPreference) findPreference(FORMA);

        int tipo = Integer.parseInt(forma.getValue());

        ListPreference formaTempo = (ListPreference) findPreference(TEMPO);

        if (status && tipo == 1) {

            EditTextPreference eQuantidade = (EditTextPreference) findPreference(QUANTIDADE);
            int quantidade = Integer.parseInt(eQuantidade.getText());

            int valor = Integer.parseInt(formaTempo.getValue());

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);

            long tempo = 0;
            if (valor == 1) {
                //um minuto tem 60*1000 milisegundos
                tempo = 60 * 1000 * quantidade;

            } else if (valor == 2) {
                //uma hora tem 60*60*1000 milisegundos
                tempo = 60 * 60 * 1000 * quantidade;
            } else {
                //um dia tem 24*60*60*1000 milisegundos
                tempo = 24 * 60 * 60 * 1000 * quantidade;

            }

            Log.i("apagar", "empo: " + tempo);

            Intent it = new Intent(getActivity(), ExcluiMensagens.class);
            PendingIntent p = PendingIntent.getBroadcast(getActivity(), 0, it, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 5);
            long start = calendar.getTimeInMillis();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start, tempo, p);

        }


    }

    private void cancel(){
        Intent it = new Intent(getActivity(), ExcluiMensagens.class);
        PendingIntent p = PendingIntent.getBroadcast(getActivity(), 0, it, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        alarmManager.cancel(p);
    }



}
