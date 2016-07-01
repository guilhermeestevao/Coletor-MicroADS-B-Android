package br.ufc.si.coletor.coletorads_b.util;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import br.ufc.si.coletor.coletorads_b.R;

/**
 * Created by guilherme on 15/08/15.
 */
public class SnackBarUtil {

    public static Snackbar getSuccessfulSnackbar(CoordinatorLayout layout, String message, int duration){
        return buildSnackbar(layout, message, duration, R.color.success);
    }

    public static Snackbar getUnsuccessfulSnackbar(CoordinatorLayout layout, String message, int duration){
        return buildSnackbar(layout, message, duration, R.color.unsuccess);
    }

    public static Snackbar getWarningSnackbar(CoordinatorLayout layout, String message, int duration){
        return buildSnackbar(layout, message, duration, R.color.warning);
    }

    private static Snackbar buildSnackbar(CoordinatorLayout layout, String message, int duration, int color){
        Snackbar snackbar = Snackbar.make(layout, message, duration);
        View view = snackbar.getView();
        view.setBackgroundColor(ColetorApplication.CONTEXT.getResources().getColor(color));

        return snackbar;
    }

}
