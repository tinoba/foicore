package eu.tinoba.androidarcitecturetemplate.ui.cart;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;
import eu.tinoba.androidarcitecturetemplate.injection.component.ActivityComponent;
import eu.tinoba.androidarcitecturetemplate.ui.base.activities.BaseActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

public class CartActivity extends BaseActivity implements CartView, EasyPermissions.PermissionCallbacks, CartListAdapter.Listener {

    @Inject
    CartPresenter presenter;

    private static final int PERMISSION_CODE = 1;
    private static final String DEVICE_NAME_ID = "deviceName";
    private static final String PIN_ID = "pin";

    private static String pin;
    private static String deviceName;

    private CartListAdapter cartListAdapter;

    private LinearLayoutManager linearLayoutManager;

    private boolean isConnected = false;
    private int position;

    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    private String realValue = "";
    volatile boolean stopWorker;
    private BluetoothAdapter bluetoothAdapter;
    private IntentFilter filter;

    @BindView(R.id.activity_cart_list_of_products)
    RecyclerView cartList;

    public static Intent createIntent(final Context context, final String deviceName, final String pin) {
        return new Intent(context, CartActivity.class).putExtra(DEVICE_NAME_ID, deviceName).putExtra(PIN_ID, pin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        final IntentFilter filter2 = new IntentFilter();
        filter2.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

        registerReceiver(broadcastReceiver, filter);
        registerReceiver(pairingRecivier, filter2);

        cartListAdapter = new CartListAdapter();
        cartListAdapter.setListener(this);
        linearLayoutManager = new LinearLayoutManager(this);

        cartList.setLayoutManager(linearLayoutManager);
        cartList.setAdapter(cartListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        pin = getIntent().getStringExtra(PIN_ID);
        deviceName = getIntent().getStringExtra(DEVICE_NAME_ID);
        if (pin != null && !isConnected) {
            requestPermission();
        }
        presenter.setView(this);
    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    /*
    @OnClick(R.id.mlijeko_button)
    public void mlijekoClicked() {
        presenter.addProductToCart("70379575");
    }

    @OnClick(R.id.bluetooth_button)
    public void searchBluetoothDevices() {
        requestPermission();
    }
*/
    @AfterPermissionGranted(PERMISSION_CODE)
    private void requestPermission() {
        final String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            closeBlueToothConnection();
            bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.startDiscovery();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant permissions",
                                               PERMISSION_CODE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(final int requestCode, final List<String> perms) {
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void onPermissionsDenied(final int requestCode, final List<String> perms) {

    }

    private final BroadcastReceiver pairingRecivier = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                //the pin in case you need to accept for an specific pin
                Timber.e("Start Auto Pairing. PIN = " + pin);
                byte[] pinBytes;
                try {
                    final BluetoothDevice device = intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    pinBytes = ("" + pin).getBytes("UTF-8");
                    device.setPin(pinBytes);
                    openBlueToothConnection(device);
                } catch (final UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getName().equals(deviceName) && !isConnected) {
                    isConnected = true;
                    Timber.e("Start Auto Pairing. PIN = " + intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", 1234));
                    if (device.getUuids() != null) {
                        openBlueToothConnection(device);
                    } else {
                        device.createBond();
                    }

                    //setPairing confirmation if neeeded
                    //device.setPairingConfirmation(true);

                }
                Timber.e("Found device " + device.getName());
                Timber.e(device.getAddress());
                Timber.e(String.valueOf(device.getUuids()));
            }
        }
    };

    void openBlueToothConnection(final BluetoothDevice device) {
        final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        try {
            bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();
        } catch (IOException e) {
            Timber.e(e);
        }

        beginListenForData();

        Timber.e("Bluetooth Opened");
    }

    private void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(() -> {
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

                                handler.post(() -> /*Timber.e("Data recieved: " + data + " Real value: " + realValue)*/
                                                     presenter.addProductToCart(realValue));
                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (IOException ex) {
                    stopWorker = true;
                }
            }
        });

        workerThread.start();
    }

    private void closeBlueToothConnection() {
        isConnected = false;
        stopWorker = true;
        try {
            if (bluetoothSocket != null) {
                outputStream.close();
                inputStream.close();
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            Timber.e(e);
        }

        Timber.e("Bluetooth Closed");
    }

    @Override
    protected void onDestroy() {
        closeBlueToothConnection();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(pairingRecivier);
        super.onDestroy();
    }

    @Override
    public void addItemToCart(final List<Product> products) {
        cartListAdapter.setData(products);
        cartListAdapter.notifyDataSetChanged();
    }

    @Override
    public void openRemoveDialog(final int position) {
        this.position = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove this item?").setPositiveButton("Yes", onClickListener)
               .setNegativeButton("No", onClickListener).show();
    }

    DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                cartListAdapter.products.remove(position);
                cartListAdapter.notifyDataSetChanged();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    };
}

