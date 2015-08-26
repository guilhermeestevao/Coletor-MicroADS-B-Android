package br.ufc.si.coletor.coletorads_b.util;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;

import java.net.Socket;

/**
 * Created by guilherme on 15/08/15.
 */
public class ColetorApplication extends Application {

    public static Context CONTEXT;

    public static CoordinatorLayout COORDINATOR_LAYOUT;

    public static Socket SOCKET_SERVIDOR;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }
}
