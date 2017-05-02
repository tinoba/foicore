package eu.tinoba.androidarcitecturetemplate.ui.cart;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.data.api.models.request.ChekoutApiRequest;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;
import eu.tinoba.androidarcitecturetemplate.injection.component.ActivityComponent;
import eu.tinoba.androidarcitecturetemplate.ui.base.activities.BaseActivity;
import eu.tinoba.androidarcitecturetemplate.ui.search.SearchAndCreatePlanActivity;
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

//    @BindView(R.id.activity_cart_toolbar)
//    Toolbar toolbar;

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
    private SpeechRecognizer recognizer;

    private boolean shouldCheckout = true;
    private CartListAdapter cartListAdapter;

    private LinearLayoutManager linearLayoutManager;
    private boolean isConnected = false;

    private int position;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    private BluetoothAdapter bluetoothAdapter;
    private List<ChekoutApiRequest.Products> productsList;

    private IntentFilter filter;

    public static Intent createIntent(final Context context, final String deviceName, final String pin) {
        return new Intent(context, CartActivity.class).putExtra(DEVICE_NAME_ID, deviceName).putExtra(PIN_ID, pin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(this);

//        toolbar.setTitle(R.string.kosarica);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.kosarica);
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mic:
                startSpeaking();
            default:
                break;
        }
        return true;
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
        TSnackbar snackbar = TSnackbar
                .make(findViewById(android.R.id.content), "Uspješno povezano", TSnackbar.LENGTH_LONG);
        snackbar.setIconRight(R.drawable.ic_bluetooth_connected_white_24dp, 24);
        snackbar.setIconPadding(8);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#00FF00"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
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
        builder.setMessage("Jeste li sigurni da želite obrisati ovaj artikl?").setPositiveButton("Da", onClickListener)
               .setNegativeButton("Ne", onClickListener).show();
    }

    DialogInterface.OnClickListener onCheckoutListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                presenter.checkout(new ChekoutApiRequest("1", "Prodavaonica br 4", "Tomislavova 5. Zagreb", totalPayment.getText().toString(),
                                                         new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), "C7091480291", productsList));
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                shouldCheckout = false;
                break;
        }
    };

    @Override
    public void countChanged() {
        calculatePrice(cartListAdapter.products);
    }

    private void calculatePrice(final List<Product> products) {
        double totalPrice = 0;
        for (final Product product : products) {
            totalPrice += product.getCount() * product.getPrice();
        }
        String roundedString = String.format("Ukupno: %.2f kn", totalPrice);
        totalPayment.setText(roundedString);
    }

    @OnClick(R.id.activity_cart_pay_button)
    public void checkout() {
        productsList = new ArrayList<>(cartListAdapter.products.size());
        for (Product product : cartListAdapter.products) {
            productsList.add(new ChekoutApiRequest.Products(product.getId(), String.valueOf(product.getCount())));
        }
        if (SearchAndCreatePlanActivity.productList != null) {
            if (!SearchAndCreatePlanActivity.productList.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (SearchAndCreatePlanActivity.productList.size() != cartListAdapter.products.size()) {
                    builder.setMessage("Niste dodali sve proizvode iz planera, da li želite izvršiti plačanje?").setPositiveButton("Da", onCheckoutListener)
                           .setNegativeButton("Ne", onCheckoutListener).show();
                } else {
                    for (int i = 0; i < cartListAdapter.products.size(); i++) {
                        if (!cartListAdapter.products.get(i).getName().equals(SearchAndCreatePlanActivity.productList.get(i).getName())) {
                            builder.setMessage("Niste dodali sve proizvode iz planera, da li želite izvršiti plačanje?").setPositiveButton("Da", onCheckoutListener)
                                   .setNegativeButton("Ne", onCheckoutListener).show();
                            break;
                        }
                    }
                }
            } else {
                presenter.checkout(new ChekoutApiRequest("1", "Prodavaonica br 4", "Tomislavova 5. Zagreb", totalPayment.getText().toString(),
                                                         new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), "C7091480291", productsList));
            }
        } else {
            presenter.checkout(new ChekoutApiRequest("1", "Prodavaonica br 4", "Tomislavova 5. Zagreb", totalPayment.getText().toString(),
                                                     new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), "C7091480291", productsList));
        }
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

    private void startSpeaking() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"en-US"});
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        "combis.hackathon");

        recognizer = SpeechRecognizer
                .createSpeechRecognizer(this.getApplicationContext());
        RecognitionListener listener = new RecognitionListener() {

            @Override
            public void onResults(final Bundle results) {
                ArrayList<String> voiceResults = results
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (voiceResults == null) {
                    Timber.e("No voice results");
                } else {
                    Timber.e("Printing matches: ");
                    boolean found = false;
                    for (String match : voiceResults) {
                        Timber.e(match);
                        switch (match.toLowerCase()) {

                            case "dodaj opremu za pecanje":
                                presenter.addProductToCart("65EE25A6");
                                found = true;
                                break;
                            case "dodaj loptice za stolni tenis":
                                presenter.addProductToCart("6E584939");
                                found = true;
                                break;
                            default:
                                break;
                        }
                        if (found) {
                            break;
                        }
                    }
                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                Timber.e("Ready for speech");
            }

            @Override
            public void onError(int error) {
                Timber.e("Error listening for speech: " + error);
            }

            @Override
            public void onBeginningOfSpeech() {
                Timber.e("Speech starting");
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onEndOfSpeech() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // TODO Auto-generated method stub

            }
        };
        recognizer.setRecognitionListener(listener);
        recognizer.startListening(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recognizer != null) {
            recognizer.cancel();
        }
    }
}

