package br.ufc.si.coletor.coletorads_b.interfaces;

import br.ufc.si.coletor.coletorads_b.modelo.Mensagem;

/**
 * Created by guilherme on 09/08/15.
 */
public interface NewMessageListener {

    public abstract void onNewMenssge(Mensagem msg);

}
