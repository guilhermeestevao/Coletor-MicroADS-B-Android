package br.ufc.si.coletormicroadsb.usb;


import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.Map;

public class UsbController {

    private UsbManager manager;
    private Map<String, UsbDevice> devices;
    private Context context;

    public UsbController(Context contecxt){
        this.context = contecxt;
        this.manager = (UsbManager) contecxt.getSystemService(Context.USB_SERVICE);
        this.devices = this.manager.getDeviceList();
    }

    public Map<String, UsbDevice> getDevices(){
        return this.devices;
    }

}
