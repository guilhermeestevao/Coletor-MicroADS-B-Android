package util;

import javax.swing.JFileChooser;

public class FileUtil {
	
	public static String abrirArquivo(){
		JFileChooser abrir = new JFileChooser();  
		int retorno = abrir.showOpenDialog(null);  
			if (retorno==JFileChooser.APPROVE_OPTION)  
				return abrir.getSelectedFile().getAbsolutePath();
			
		 return null;
	}
	
	

}
