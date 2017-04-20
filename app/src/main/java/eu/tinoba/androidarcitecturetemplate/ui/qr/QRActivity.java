package eu.tinoba.androidarcitecturetemplate.ui.qr;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.injection.component.ActivityComponent;
import eu.tinoba.androidarcitecturetemplate.ui.base.activities.BaseActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class QRActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, ZXingScannerView.ResultHandler {

    private static final String NAME = "name";
    private static final String PIN = "pin";

    private static final int CAMERA_PERMISSION_CODE = 10;

    public ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestCameraPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (zXingScannerView != null) {
            zXingScannerView.stopCamera();
        }

    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
    @Override
    public void handleResult(final Result result) {
        try {
            final JSONObject jsonObject = new JSONObject(result.getText());
            Log.e("NAME", jsonObject.getString(NAME));
            Log.e("PIN", jsonObject.getString(PIN));

//            startActivity(createIntent(this, Long.parseLong(jsonObject.getString(ITEM))));
            finish();
        } catch (JSONException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(e.getMessage());
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                finish();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(CAMERA_PERMISSION_CODE)
    private void requestCameraPermission() {
        final String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            zXingScannerView = new ZXingScannerView(this);
            setContentView(zXingScannerView);
            zXingScannerView.setResultHandler(this);
            zXingScannerView.startCamera();
        } else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.camera_permission_request),
                                               CAMERA_PERMISSION_CODE, perms);
        }
    }

    @Override
    public void onPermissionsGranted(final int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(final int requestCode, List<String> perms) {
        finish();
    }
}
