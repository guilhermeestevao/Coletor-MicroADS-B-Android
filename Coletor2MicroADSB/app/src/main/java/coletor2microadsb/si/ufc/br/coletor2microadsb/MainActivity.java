package coletor2microadsb.si.ufc.br.coletor2microadsb;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import coletor2microadsb.si.ufc.br.coletor2microadsb.usb.CDCDevice;
import coletor2microadsb.si.ufc.br.coletor2microadsb.usb.UsbController;



public class MainActivity extends ActionBarActivity {

    private TextView info;

    private UsbController controller;
    private CDCDevice cdcDevice;
    private TextView result;
    private EditText msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.info);
        result = (TextView) findViewById(R.id.recebido);
        msg = (EditText) findViewById(R.id.msg);
        checkInfo();

    }

    private void checkInfo() {
        controller = new UsbController(this, this.getIntent());
        final UsbDevice device = controller.getMicroAdsb();
         String i = "";
        String saida = "";
        if(device != null) {

            i += "\n" +
                    "DeviceID: " + device.getDeviceId() + "\n" +
                    "DeviceName: " + device.getDeviceName() + "\n" +
                    "DeviceClass: " + device.getDeviceClass() + " - " + translateDeviceClass(device.getDeviceClass()) + "\n" +
                    "DeviceSubClass: " + device.getDeviceSubclass() + "\n" +
                    "VendorID: " + device.getVendorId() + "\n" +
                    "ProductID: " + device.getProductId() + "\n" +
                    "Interfaces Quantidade:" +device.getInterfaceCount()+"\n";

            for(int j = 0; j<device.getInterfaceCount(); j++){
                UsbInterface usbInterface = device.getInterface(j);
                i+="Interface "+j+" com "+usbInterface.getEndpointCount()+" endPoints \n";

                for(int k = 0; k < usbInterface.getEndpointCount(); k++){
                    UsbEndpoint point = usbInterface.getEndpoint(k);
                    saida = point.getDirection() == UsbConstants.USB_DIR_IN ? "In":"Out";
                    i+="EndPoint "+k+ " ("+ saida+") " +  "com address "+point.getAddress()+"\n";
                }

            }

            cdcDevice = new CDCDevice(controller.getManager(), device, this);
            cdcDevice.setParameters(115200, 8, 1, 0);


        }else{
            i+="Micro ADS-B disconectado!";
        }
        info.setText(i);
    }

    private String translateDeviceClass(int deviceClass){
        switch(deviceClass){
            case UsbConstants.USB_CLASS_APP_SPEC:
                return "Application specific USB class";
            case UsbConstants.USB_CLASS_AUDIO:
                return "USB class for audio devices";
            case UsbConstants.USB_CLASS_CDC_DATA:
                return "USB class for CDC devices (communications device class)";
            case UsbConstants.USB_CLASS_COMM:
                return "USB class for communication devices";
            case UsbConstants.USB_CLASS_CONTENT_SEC:
                return "USB class for content security devices";
            case UsbConstants.USB_CLASS_CSCID:
                return "USB class for content smart card devices";
            case UsbConstants.USB_CLASS_HID:
                return "USB class for human interface devices (for example, mice and keyboards)";
            case UsbConstants.USB_CLASS_HUB:
                return "USB class for USB hubs";
            case UsbConstants.USB_CLASS_MASS_STORAGE:
                return "USB class for mass storage devices";
            case UsbConstants.USB_CLASS_MISC:
                return "USB class for wireless miscellaneous devices";
            case UsbConstants.USB_CLASS_PER_INTERFACE:
                return "USB class indicating that the class is determined on a per-interface basis";
            case UsbConstants.USB_CLASS_PHYSICA:
                return "USB class for physical devices";
            case UsbConstants.USB_CLASS_PRINTER:
                return "USB class for printers";
            case UsbConstants.USB_CLASS_STILL_IMAGE:
                return "USB class for still image devices (digital cameras)";
            case UsbConstants.USB_CLASS_VENDOR_SPEC:
                return "Vendor specific USB class";
            case UsbConstants.USB_CLASS_VIDEO:
                return "USB class for video devices";
            case UsbConstants.USB_CLASS_WIRELESS_CONTROLLER:
                return "USB class for wireless controller devices";
            default: return "Unknown USB class!";

        }
    }

    public void teste(View view){
        String text = msg.getText().toString();
        if(!text.equals("")) {
            msg.setText("");
            new TesteTask().execute(text);
        }else {
            return;
        }
    }

    class TesteTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected void onPostExecute(Integer integer) {
            byte[] bytes = new byte[64];
            String resultRead = null;
            int i;
            try {
                i = cdcDevice.read(bytes, 100);
                resultRead = new String(bytes, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

            result.setText(resultRead);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            String teste =  strings[0]+"\r\n";
            try {
                int i = cdcDevice.write(teste.getBytes(), 1000);
                return i;
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return null;
        }
    }
}
