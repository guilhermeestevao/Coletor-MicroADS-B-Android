package resultados;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Captura;
import model.Mensagens;
import model.Voo;

import org.opensky.libadsb.tools;
import org.opensky.libadsb.Decoder;
import org.opensky.libadsb.exceptions.BadFormatException;
import org.opensky.libadsb.msgs.ModeSReply;

import dao.CapturaDAO;
import dao.CapturaJpaDao;
import dao.MensagensDAO;
import dao.MensagensJpaDAO;
import dao.VooDAO;
import dao.VooJpaDAO;
import util.FileUtil;

public class ReadFileAndroid {

	private static Map<String, List<Mensagens>> flights;
	
	public static void main(String[] args) {
			
		flights = new HashMap<String, List<Mensagens>>(); 
		
		try{
			String pathRead = FileUtil.abrirArquivo();
			InputStream in = new FileInputStream(pathRead);
			InputStreamReader is = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(is);
			System.err.println("Lendo e processando arquivo arquivo!");
			while(br.ready()){
				String line =	br.readLine();
				String[] tokens = line.split(",");
				decodeMsg(new Long(tokens[0]), tokens[1]);
			}
		}catch(Exception e){
			System.out.println("Errona leitura doarquivo!");
		} 
		
		
		salvar();
		
	}
	
	private static void salvar(){
		CapturaDAO dao = new CapturaJpaDao();
		VooDAO daoVoo =  new VooJpaDAO();
		MensagensDAO daoMsg = new MensagensJpaDAO();
		
		
		Set<String> voos = flights.keySet();

		Captura captura = new Captura();
		
		dao.beginTransaction();
		
		dao.save(captura);
		
		for(String key : voos){
			Voo voo = new Voo();
			System.err.println("Salvando os dados de "+key);
			voo.setIcao(key);
			voo.setCaptura(captura);
			daoVoo.save(voo);
			
			for(Mensagens msg : flights.get(key)){
				
				msg.setVoo(voo);
				daoMsg.save(msg);
						
			}
			
			
		}
		dao.commit();
	
		dao.close();
		
		
	}
	
	public static void decodeMsg(long timestamp, String raw) throws Exception {
		
		ModeSReply msg;
		
		try {
			msg = Decoder.genericDecoder(raw);
		} catch (BadFormatException e) {
			return;
		}


		if (tools.isZero(msg.getParity()) || msg.checkParity()) {
			String icao24 = tools.toHexString(msg.getIcao24());
			
			Mensagens mensagem = new Mensagens();
			mensagem.setConteudo(raw);
			mensagem.setHora(new Date(timestamp));
			mensagem.setTipo(msg.getType().name());

			fillmapFlights(icao24, mensagem);
			
		}
	}
	
	private static void fillmapFlights(String icao, Mensagens msg){
		
		if(flights.containsKey(icao)){
			
			List<Mensagens> msgs = flights.get(icao);
			
			msgs.add(msg);
			
			flights.put(icao, msgs);
		
		}else{
			
			List<Mensagens> msgs = new ArrayList<Mensagens>();
			
			msgs.add(msg);
			
			flights.put(icao, msgs);
			
		}
		
	}
	

}
