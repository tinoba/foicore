package eu.tinoba.androidarcitecturetemplate.device;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FoiCoreFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = FoiCoreFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "New Token: " + refreshedToken);
    }
}
