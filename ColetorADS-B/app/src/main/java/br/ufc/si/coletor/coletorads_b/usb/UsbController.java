package br.ufc.si.coletor.coletorads_b.usb;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.HashMap;
import java.util.Map;

;

/**
 * Created by guilherme on 09/05/15.
 */
public class UsbController {

    private UsbManager manager;
    private Context context;
    private Intent intent;
    private UsbDevice microAdsb;
    private Map<String, UsbDevice> devices = new HashMap<String, UsbDevice>();
    

    public UsbController(Context context, Intent intent){
        this.context = context;
        this.intent = intent;
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        devices = manager.getDeviceList();
    }

    public UsbController(Context context){
        this.context = context;
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        devices = manager.getDeviceList();
    }

    public UsbDevice getMicroAdsb(){
        microAdsb = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        return microAdsb;
    }

    public UsbManager getManager(){
        return this.manager;
    }

    public Map<String, UsbDevice> getDevices(){
        return this.devices;
    }


}
