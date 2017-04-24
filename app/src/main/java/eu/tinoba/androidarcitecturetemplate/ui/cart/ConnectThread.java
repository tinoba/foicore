package eu.tinoba.androidarcitecturetemplate.ui.cart;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {

    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothSocket socket;
    private final BluetoothDevice device;
    private final BluetoothAdapter bluetoothAdapter;
    private ListenForDataThread listenForDataThread;
    private final CartPresenter presenter;
    private final boolean stopWorker;
    private final int readBufferPoistion;
    private final byte[] readBuffer;

    public ConnectThread(final BluetoothDevice device, final BluetoothAdapter bluetoothAdapter, final CartPresenter presenter, final boolean stopWorker,
                         final int readBufferPoistion, final byte[] readBuffer) {
        BluetoothSocket tmp = null;
        this.stopWorker = stopWorker;
        this.device = device;
        this.presenter = presenter;
        this.bluetoothAdapter = bluetoothAdapter;
        this.readBuffer = readBuffer;
        this.readBufferPoistion = readBufferPoistion;

        try {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
        }
        socket = tmp;
    }

    public void run() {
        bluetoothAdapter.cancelDiscovery();

        try {
            socket.connect();
        } catch (IOException connectException) {
            try {
                socket.close();
            } catch (IOException closeException) {
            }
            return;
        }
        new ListenForDataThread(socket, presenter, stopWorker, readBufferPoistion, readBuffer).start();
    }
    public void cancel() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }
}
