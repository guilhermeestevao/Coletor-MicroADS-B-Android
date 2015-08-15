package br.ufc.quixada.si.coletoradsbandroid.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import java.util.Map;

import br.ufc.quixada.si.coletoradsbandroid.activities.MainActivity;
import br.ufc.quixada.si.coletoradsbandroid.usb.CDCDevice;
import br.ufc.quixada.si.coletoradsbandroid.usb.UsbController;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServiceRequestPermissionUsb extends IntentService {

    private PendingIntent mPermissionIntent;
    private UsbController mUsbController;

    private static final String ACTION_USB_PERMISSION = "com.mobilemerit.usbhost.USB_PERMISSION";

    public ServiceRequestPermissionUsb(){
        super("task");
    }

    public ServiceRequestPermissionUsb(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        mUsbController = new UsbController(getApplicationContext());
        Map<String, UsbDevice> devices = mUsbController.getDevices();
        if (devices != null) {
            for (UsbDevice device : devices.values()) {
                if (device.getProductId() == 10 && (device.getVendorId() == 1240 || device.getVendorId() == 1002)) {
                    mUsbController.getManager().requestPermission(device, mPermissionIntent);

                    break;
                }
            }
        }

    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {

                    }
                }
            }
        }
    };

}
