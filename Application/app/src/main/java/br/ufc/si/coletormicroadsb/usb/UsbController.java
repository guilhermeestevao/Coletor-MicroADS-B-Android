package br.ufc.si.coletormicroadsb.usb;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;

public class UsbController {

    private UsbManager manager;
    private Context context;
    private Intent intent;
    private UsbDevice microAdsb;

    public UsbController(Context context, Intent intent){
        this.context = context;
        this.intent = intent;
        this.manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }


    public UsbDevice getMicroAdsb(){
        microAdsb = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        return microAdsb;
    }
}
