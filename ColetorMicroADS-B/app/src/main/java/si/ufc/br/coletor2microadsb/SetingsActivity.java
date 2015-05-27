package si.ufc.br.coletor2microadsb;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;

@SuppressWarnings("deprecation")
public class SetingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String AUTOEXCLUSAO = "autoexclusao";
    private static final String FORMA = "forma";
    private static final String INTERVALO_DE_TEMPO = "Intervalo de tempo";
    private static final String TEMPO = "tempo_tipo";
    private static final String QUANTIDADE = "quantidade";
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        iniciar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(FORMA)){
            ListPreference forma = (ListPreference) findPreference(FORMA);
            String sFotma = forma.getEntry().toString();
            forma.setSummary(s);
            int tipo = Integer.parseInt(forma.getValue());

            ListPreference tempo = (ListPreference) findPreference(TEMPO);
            if(tipo == 2){
                tempo.setEnabled(false);
            }else{
                tempo.setEnabled(true);
            }
        } else if (s.equals(TEMPO)) {
            ListPreference tempo = (ListPreference) findPreference(TEMPO);
            tempo.setSummary("Período: " + tempo.getEntry());
        }else if(s.equals(QUANTIDADE)){
            EditTextPreference eQuantidade = (EditTextPreference) findPreference(QUANTIDADE);
            int quantidade = Integer.parseInt(eQuantidade.getText());
            eQuantidade.setSummary("Quantidade: " + quantidade);
        }
    }

    private void iniciar(){
        ListPreference forma = (ListPreference) findPreference(FORMA);
        String sFotma = forma.getEntry().toString();
        forma.setSummary(sFotma);
        ListPreference tempo = (ListPreference) findPreference(TEMPO);
        tempo.setSummary("Período: " + tempo.getEntry());
        int tipo = Integer.parseInt(forma.getValue());

        if(tipo == 2){
            tempo.setEnabled(false);
        }

        EditTextPreference eQuantidade = (EditTextPreference) findPreference(QUANTIDADE);
        int quantidade = Integer.parseInt(eQuantidade.getText());
        eQuantidade.setSummary("Quantidade: " + quantidade);
    }
}