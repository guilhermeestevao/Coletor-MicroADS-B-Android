package coletor2microadsb.si.ufc.br.coletor2microadsb.usb;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.ArrayList;
import java.util.List;
;

/**
 * Created by guilherme on 09/05/15.
 */
public class UsbController {

    private UsbManager manager;
    private Context context;
    private Intent intent;
    private UsbDevice microAdsb;
    

    public UsbController(Context context, Intent intent){
        this.context = context;
        this.intent = intent;
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

    }

    public UsbDevice getMicroAdsb(){
        microAdsb = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        return microAdsb;
    }



    public UsbManager getManager(){
        return this.manager;
    }
}
