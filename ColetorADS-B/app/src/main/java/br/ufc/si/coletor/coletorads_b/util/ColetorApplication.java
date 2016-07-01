package br.ufc.si.coletor.coletorads_b.util;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;

import java.net.Socket;
import java.util.List;

import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;

/**
 * Created by guilherme on 15/08/15.
 */
public class ColetorApplication extends Application {

    public static Context CONTEXT;

    public static CoordinatorLayout COORDINATOR_LAYOUT;

    public static Socket SOCKET_SERVIDOR;

    public static RecyclerView RECYCLEVIEW;

    public static List<Mensagem> MENSAGENS;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }
}
