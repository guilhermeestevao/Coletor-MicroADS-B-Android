package br.ufc.quixada.si.coletoradsbandroid.usb;

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
    private Map<String, UsbDevice> devices = new HashMap<String, UsbDevice>();
    private UsbDevice device;

    public UsbController(Context context){
        this.context = context;
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        devices = manager.getDeviceList();
    }

    public UsbManager getManager(){
        return this.manager;
    }

    public Map<String, UsbDevice> getDevices(){
        return this.devices;
    }


    public UsbDevice getDevice() {
        return device;
    }

    public void setDevice(UsbDevice device) {
        this.device = device;
    }
}
