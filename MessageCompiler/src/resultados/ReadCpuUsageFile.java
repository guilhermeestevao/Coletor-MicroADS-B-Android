package resultados;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import dao.CapturaDAO;
import dao.CapturaJpaDao;
import dao.ResultadoDAO;
import dao.ResultadoJpaDAO;
import model.Captura;
import model.Resultado;
import model.Voo;
import util.FileUtil;

public class ReadCpuUsageFile {
	
	private static Scanner sc;
	
	
	public static void main(String[] args) {
		
		sc = new Scanner(System.in);
		
		String path = FileUtil.abrirArquivo();
		
		System.out.println("Android (1) Desktop (2): ");
		int opc = sc.nextInt();
		
		try{
			if(opc == 1){
				readFileAndroid(path);
			}else{
				readFileDesktop(path);
			}
		}catch(IOException e){
			return;
		}
		
		
	}
	
	
	
	
	private static void readFileAndroid(String path) throws IOException{
		
		Map<Date, Double> values = new HashMap<Date, Double>();
		
		InputStream in = new FileInputStream(path);
		InputStreamReader is = new InputStreamReader(in, "UTF-8");
		BufferedReader br = new BufferedReader(is);
	
		String[] tokens;
		while(br.ready()){
			String line =	br.readLine();
			System.out.println(line);
			
			tokens = line.split("	");	
			
			long time = Long.parseLong(tokens[0]);
			
			double value = Double.parseDouble(tokens[1]);
			Date date = new Date(time);
			
			values.put(date, value);
		
		}
		
		compilar(values);
		
	}
		
	private static void readFileDesktop(String path) throws IOException{
		
		Map<Date, Double> values = new HashMap<Date, Double>();
		
		InputStream in = new FileInputStream(path);
		InputStreamReader is = new InputStreamReader(in, "UTF-8");
		BufferedReader br = new BufferedReader(is);
		while(br.ready()){
			String line =	br.readLine();
		
			String[] tokens = line.split(" +");	
			
			String time = tokens[0];
			String cpu = tokens[2];
		
			String[] horas =  time.split(":");
		
			int hora = Integer.parseInt(horas[0]);
			int minuto = Integer.parseInt(horas[1]);
			int segundo = Integer.parseInt(horas[2]);
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, hora);
			c.set(Calendar.MINUTE, minuto);
			c.set(Calendar.SECOND, segundo);
			
			Date date = c.getTime();		
			cpu = cpu.replace(",", ".");
			
			double value = Double.parseDouble(cpu);
			
			values.put(date, value);
			
		}
		
		compilar(values);
	}
	
	private static void compilar(Map<Date, Double> value){
		
		CapturaDAO dao = new CapturaJpaDao(); 
		
		System.out.println("Diigte o id da captura: ");
		long idCaptura = sc.nextLong();
		
		Captura captura = dao.find(idCaptura);
		List<Voo> voos = captura.getVoos();
		int contVoo = voos.size();
		int contMsgs = 0;
		
		for(Voo voo : voos){
			contMsgs +=voo.getMensagens().size();
		}
		
		
		Date fim = Collections.max(value.keySet());
		Date inicio = Collections.min(value.keySet());
		
		double media = avg(value.values());

		double maxCpu = Collections.max(value.values());
		double minCpu = Collections.min(value.values());
		
	
		Resultado resultado = new Resultado();
		resultado.setCaptura(captura);
		resultado.setQuantidadeMensagens(contMsgs);
		resultado.setQuantidadeVoos(contVoo);
		resultado.setFim(fim);
		resultado.setInicio(inicio);
		resultado.setUsoMaxCpu(maxCpu);
		resultado.setUsoMinCpu(minCpu);
		
		saveResult(resultado);
	}
	
	private static void saveResult(Resultado resultado){
		ResultadoDAO dao = new ResultadoJpaDAO(); 
		dao.beginTransaction();
		dao.save(resultado);
		dao.commit();
		dao.close();
	}
	
	private static double avg(Collection<Double> valores){
		double total = 0;
		double media = 0;
		
		for(double valor : valores){
			
			total += valor;
			
		}
		
		media = total/valores.size();
		
		
		return media;
	}

}
