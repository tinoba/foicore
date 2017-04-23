package eu.tinoba.androidarcitecturetemplate.ui.cart;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ListenForDataThread extends Thread {

    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private boolean stopWorker;
    private int readBufferPosition;
    private byte[] readBuffer;
    private String realValue;
    private final CartPresenter presenter;

    public ListenForDataThread(final BluetoothSocket socket, final CartPresenter presenter, final boolean stopWorker, final int readBufferPosition, final byte[] readBuffer) {
        this.socket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.presenter = presenter;
        this.stopWorker = stopWorker;
        this.readBufferPosition = readBufferPosition;
        this.readBuffer = readBuffer;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    public void run() {
        final byte delimiter = 10;

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        while (!Thread.currentThread().isInterrupted() && !stopWorker) {
            try {
                int bytesAvailable = inputStream.available();
                if (bytesAvailable > 0) {
                    byte[] packetBytes = new byte[bytesAvailable];
                    inputStream.read(packetBytes);
                    for (int i = 0; i < bytesAvailable; i++) {
                        byte b = packetBytes[i];
                        if (b == delimiter) {
                            byte[] encodedBytes = new byte[readBufferPosition];
                            System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            final String data = new String(encodedBytes, "US-ASCII");
                            readBufferPosition = 0;
                            int sizeOfData = data.length();
                            realValue = "";
                            for (int j = 0; j < sizeOfData; j++) {
                                if ((data.charAt(j) >= 'A' && data.charAt(j) <= 'Z') || data.charAt(j) <= '9' && data.charAt(j) >= '0') {
                                    realValue += data.charAt(j);
                                } else {
                                    if (data.charAt(j) != '\r') {
                                        realValue = "";
                                    }
                                }
                            }
                            presenter.addProductToCart(realValue);
                        } else {
                            readBuffer[readBufferPosition++] = b;
                        }
                    }
                }
            } catch (IOException ex) {
                stopWorker = true;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
        }
    }
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
