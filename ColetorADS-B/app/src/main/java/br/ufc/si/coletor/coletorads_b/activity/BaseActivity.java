package br.ufc.si.coletor.coletorads_b.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import br.ufc.si.coletor.coletorads_b.R;

/**
 * Created by Guilherme on 07/08/2015.
 */
public class BaseActivity extends DebugActivity {



    private static final String TAG = "livroandroid";

    protected Context getContext() {
        return this;
    }

    protected Activity getActivity() {
        return this;
    }

    protected void log(String msg) {
        Log.d(TAG, msg);
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void alert(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
