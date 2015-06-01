package si.ufc.br.coletor2microadsb.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import si.ufc.br.coletor2microadsb.modelo.Mensagem;

/**
 * Created by guilherme on 31/05/15.
 */
public class MensagemREST {

    private static final String URL_WS = "http://200.129.38.124:5000";

    public String[] enviarMensagensServidor(List<Mensagem> mensagens){
        JSONObject jo = new JSONObject();
        /*
        try {
            jo.put("id", compra.getId());
            jo.put("idVendedor", compra.getIdVendedor());
            jo.put("idComprador", compra.getIdComprador());
            jo.put("idProduto", compra.getIdProduto());
            jo.put("valorVenda", compra.getValorVenda());
            jo.put("quantidadeProduto", compra.getQuantidadeProduto());
            jo.put("data", compra.getData());

            String compraJson = jo.toString();
            String[] respostaServidor = new WebServiceCliente().post(URL_WS+"nova", compraJson);
            return respostaServidor[1];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
        return null;
    }

}
