package br.ufc.quixada.si.coletoradsbandroid.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.hardware.usb.UsbDevice;

import java.util.Map;

import br.ufc.quixada.si.coletoradsbandroid.usb.UsbController;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ReceiverUsbDetected extends BroadcastReceiver {

    private static String LOG = "ReceiverUsbDetected";
    private UsbController mUsbController;

    @Override
    public void onReceive(Context context, Intent intent) {

        mUsbController = new UsbController(context.getApplicationContext());
        Map<String, UsbDevice> devices = mUsbController.getDevices();
        if (devices != null) {
            for (UsbDevice device : devices.values()) {
                if (device.getProductId() == 10 && (device.getVendorId() == 1240 || device.getVendorId() == 1002)) {
                    Intent it = new Intent(context, ServiceRequestPermissionUsb.class);
                    context.startService(it);
                    break;
                }
            }
        }


    }
}
