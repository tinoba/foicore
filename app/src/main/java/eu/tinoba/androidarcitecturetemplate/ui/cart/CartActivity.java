package eu.tinoba.androidarcitecturetemplate.ui.cart;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.UnsupportedEncodingException;
import java.util.List;

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

    @BindView(R.id.connecting_progress_view)
    CircularProgressView connectingProgressView;

    @BindView(R.id.connecting_text_view)
    TextView connectingTextView;

    @BindView(R.id.activity_cart_list_of_products)
    RecyclerView cartList;

    @BindView(R.id.activity_cart_toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_cart_total_payment)
    TextView totalPayment;

    @BindView(R.id.activity_cart_pay_button)
    Button payButton;

    @BindView(R.id.activity_cart_empty_text)
    TextView emptyText;

    @BindView(R.id.activity_cart_empty_image)
    ImageView emptyImage;

    private static final int PERMISSION_CODE = 1;
    private static final String DEVICE_NAME_ID = "deviceName";

    private static final String PIN_ID = "pin";
    private static String pin;

    private static String deviceName;
    private ConnectThread connectThread;

    private CartListAdapter cartListAdapter;

    private LinearLayoutManager linearLayoutManager;
    private boolean isConnected = false;

    private int position;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    private BluetoothAdapter bluetoothAdapter;

    private IntentFilter filter;

    public static Intent createIntent(final Context context, final String deviceName, final String pin) {
        return new Intent(context, CartActivity.class).putExtra(DEVICE_NAME_ID, deviceName).putExtra(PIN_ID, pin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.kosarica);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        final IntentFilter filter2 = new IntentFilter();
        filter2.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

        registerReceiver(broadcastReceiver, filter);
        registerReceiver(pairingRecivier, filter2);

        cartListAdapter = new CartListAdapter(this);
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

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getName().equals(deviceName) && !isConnected) {
                    isConnected = true;
                    Timber.e("Start Auto Pairing. PIN = " + intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", 1234));
                    if (device.getUuids() != null) {
                        openBlueToothConnection(device);
                    } else {
                        device.createBond();
                    }
                }
                Timber.e("Found device " + device.getName());
                Timber.e(device.getAddress());
                Timber.e(String.valueOf(device.getUuids()));
            }
        }
    };

    void openBlueToothConnection(final BluetoothDevice device) {

        connectThread = new ConnectThread(device, bluetoothAdapter, presenter, stopWorker, readBufferPosition, readBuffer);
        connectThread.start();

        emptyText.setVisibility(View.VISIBLE);
        emptyImage.setVisibility(View.VISIBLE);
        connectingProgressView.setVisibility(View.GONE);
        connectingTextView.setVisibility(View.GONE);
        Timber.e("Bluetooth Opened");
    }

    private void closeBlueToothConnection() {
        isConnected = false;
        stopWorker = true;
        if (connectThread != null) {
            connectThread.cancel();
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
        emptyImage.setVisibility(View.GONE);
        emptyText.setVisibility(View.GONE);
        cartList.setVisibility(View.VISIBLE);
        totalPayment.setVisibility(View.VISIBLE);
        payButton.setVisibility(View.VISIBLE);
        cartListAdapter.setData(products);
        calculatePrice(products);
    }

    @Override
    public void openRemoveDialog(final int position) {
        this.position = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove this item?").setPositiveButton("Yes", onClickListener)
               .setNegativeButton("No", onClickListener).show();
    }

    @Override
    public void countChanged() {
        calculatePrice(cartListAdapter.products);
    }

    private void calculatePrice(List<Product> products) {
        double totalPrice = 0;
        for (final Product product : products) {
            totalPrice += product.getCount() * product.getPrice();
        }
        String roundedString = String.format("Ukupno: %.2f kn", totalPrice);
        totalPayment.setText(roundedString);
    }

    DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:

                presenter.removeProductFromMap(cartListAdapter.products.get(position).getName());
                cartListAdapter.products.remove(position);
                cartListAdapter.notifyDataSetChanged();

                if (cartListAdapter.products.isEmpty()) {
                    cartList.setVisibility(View.GONE);
                    totalPayment.setVisibility(View.GONE);
                    payButton.setVisibility(View.GONE);
                    emptyImage.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    calculatePrice(cartListAdapter.products);
                }

                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    };
}

