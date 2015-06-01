package si.ufc.br.coletor2microadsb.modelo;

import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guilherme on 21/05/15.
 */
public class Mensagem {

    public static String[] colunas = new String[] {Mensagens._ID, Mensagens.DATA, Mensagens.TIMESTAMP, Mensagens.SINC};
    public long id;
    public String data;
    public long timestamp;
    public int sinc;

    public Mensagem(){

    }

    public Mensagem(String data, long timestamp){
        this.data = data;
        this.timestamp = timestamp;
        this.sinc = 0;
    }

    public static final class Mensagens implements BaseColumns{

        public static final String DATA = "data";
        public static final String TIMESTAMP = "timestamp";
        public static final String SINC = "sinc";

        private Mensagens(){

        }

    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
        String dataString = dateFormat.format(new Date(this.timestamp));
        return dataString+"  "+data+" status: "+(sinc == 0 ? false:true);
    }
}
