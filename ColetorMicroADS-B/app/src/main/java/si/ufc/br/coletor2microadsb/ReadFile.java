package si.ufc.br.coletor2microadsb;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by guilherme on 27/05/15.
 */
public class ReadFile {

    private static final String TAG = "ManageFile";
    private Context context;

    public ReadFile(Context context){
        this.context = context;
    }


    public String readFile() throws FileNotFoundException, IOException {
        File sdcard = android.os.Environment.getExternalStorageDirectory();

        File dir = new File(sdcard, "Download");

        String[] files = dir.list();

        for(int i = 0; i<files.length; i++){
            Log.i("TAG", files[i]);
        }

        File file = new File(dir,"messages.txt");
        FileInputStream in = new FileInputStream(file);

        byte[] buffer = new byte[(int)file.length()];

        in.read(buffer);

        return new String(buffer);
    }

}
