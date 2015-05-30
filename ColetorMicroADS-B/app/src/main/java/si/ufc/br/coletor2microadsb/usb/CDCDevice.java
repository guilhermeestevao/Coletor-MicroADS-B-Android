package si.ufc.br.coletor2microadsb.usb;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by guilherme on 09/05/15.
 */
public class CDCDevice implements Serializable{

    private static final int PARITY_NONE = 0;
    private static final int PARITY_ODD = 1;
    private static final int PARITY_EVEN = 2;
    private static final int PARITY_MARK = 3;
    private static final int PARITY_SPACE = 4;
    private static final int STOPBITS_1 = 1;
    private static final int STOPBITS_1_5 = 3;
    private static final int STOPBITS_2 = 2;
    private static final int USB_RECIP_INTERFACE = 0x01;
    private static final int USB_RT_ACM = UsbConstants.USB_TYPE_CLASS | USB_RECIP_INTERFACE;
    private static final int SET_LINE_CODING = 0x20;
    private static final int GET_LINE_CODING = 0x21;
    private static final int SET_CONTROL_LINE_STATE = 0x22;
    private static final int SEND_BREAK = 0x23;
    private final UsbDeviceConnection connection;
    private final UsbInterface controlInterface;
    private final UsbInterface dataInterface;
    private final boolean forceClaim = true;
    private boolean mEnableAsyncReads = true;
    private final Object mReadBufferLock = new Object();
    private final Object mWriteBufferLock = new Object();
    private byte[] mReadBuffer = new byte[512];
    private byte[] mWriteBuffer = new byte[512];
    private final UsbEndpoint portIn;
    private final UsbEndpoint portOut;
    private final UsbEndpoint portControl;
    private final Context context;
    private int numBytesRead;

    public CDCDevice(UsbManager manger, UsbDevice device, Context context){
        connection = manger.openDevice(device);
        connection.claimInterface(device.getInterface(1), forceClaim);
        this.context = context;
        controlInterface = device.getInterface(0);
        dataInterface = device.getInterface(1);
        UsbEndpoint endpointIn = null;
        UsbEndpoint endpointOut = null;
        portControl = controlInterface.getEndpoint(0);
        for(int i = 0; i < 2; i++){
            UsbEndpoint endpoint = dataInterface.getEndpoint(i);
            if (endpoint.getDirection() == UsbConstants.USB_DIR_IN) {
                endpointIn = endpoint;
            } else {
                endpointOut = endpoint;
            }
        }
        portIn = endpointIn;
        portOut = endpointOut;
    }


    private int sendAcmControlMessage(int request, int value, byte[] buf) {
        return connection.controlTransfer(
                USB_RT_ACM, request, value, 0, buf, buf != null ? buf.length : 0, 5000);
    }

    public void close()  {
        if (connection != null)
            connection.close();

    }

    public int read(byte[] dest, int timeoutMillis) throws IOException {

        synchronized (mReadBufferLock) {
            int readAmt = Math.min(dest.length, mReadBuffer.length);
            numBytesRead = connection.bulkTransfer(portIn, mReadBuffer, readAmt,timeoutMillis);

            if (numBytesRead < 0) {
                if (timeoutMillis == Integer.MAX_VALUE) {
                    return -1;
                }
                return 0;
            }
            System.arraycopy(mReadBuffer, 0, dest, 0, numBytesRead);
        }

        return numBytesRead;
    }


    public int write(byte[] src, int timeoutMillis) throws IOException {

        int offset = 0;

        while (offset < src.length) {
            final int writeLength;
            final int amtWritten;

            synchronized (mWriteBufferLock) {
                final byte[] writeBuffer;

                writeLength = Math.min(src.length - offset, mWriteBuffer.length);
                if (offset == 0) {
                    writeBuffer = src;
                } else {
                    System.arraycopy(src, offset, mWriteBuffer, 0, writeLength);
                    writeBuffer = mWriteBuffer;
                }

                amtWritten = connection.bulkTransfer(portOut, writeBuffer, writeLength,
                        timeoutMillis);
            }
            if (amtWritten <= 0) {
                throw new IOException("Erro para escrever " + writeLength
                        + " bytes no offset " + offset + " length=" + src.length);
            }

            offset += amtWritten;
        }
        return offset;
    }

    public void setParameters(int baudRate, int dataBits, int stopBits, int parity) throws IllegalArgumentException {
        byte stopBitsByte;
        switch (stopBits) {
            case STOPBITS_1: stopBitsByte = 0; break;
            case STOPBITS_1_5: stopBitsByte = 1; break;
            case STOPBITS_2: stopBitsByte = 2; break;
            default: throw new IllegalArgumentException("Valor invalido para stopBits: " + stopBits);
        }

        byte parityBitesByte;
        switch (parity) {
            case PARITY_NONE: parityBitesByte = 0; break;
            case PARITY_ODD: parityBitesByte = 1; break;
            case PARITY_EVEN: parityBitesByte = 2; break;
            case PARITY_MARK: parityBitesByte = 3; break;
            case PARITY_SPACE: parityBitesByte = 4; break;
            default: throw new IllegalArgumentException("Valor invalido para parity: " + parity);
        }

        byte[] msg = {
                (byte) ( baudRate & 0xff),
                (byte) ((baudRate >> 8 ) & 0xff),
                (byte) ((baudRate >> 16) & 0xff),
                (byte) ((baudRate >> 24) & 0xff),
                stopBitsByte,
                parityBitesByte,
                (byte) dataBits};
        sendAcmControlMessage(SET_LINE_CODING, 0, msg);
    }
}