package br.ufc.si.coletor.coletorads_b.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by guilherme on 27/10/15.
 */
public class FileUtil {

    private static final String DIR = "dados";
    private static final String FILE = "mensagens.csv";

    public static void save(String text){

        File f = getSdkFile();

        writeFile(f, text.getBytes());

    }

    private static File getSdkFile(){
        File sdk = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(sdk, DIR);

        if(!dir.exists()){
            dir.mkdir();
        }

        File file = new File(dir, FILE);
        return file;
    }

    private static void writeFile(File file, byte[] bytes){
        if(file != null){
            try {
                FileOutputStream out = new FileOutputStream(file, true);
                out.write(bytes);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
