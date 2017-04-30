package eu.tinoba.androidarcitecturetemplate.ui.cart;

import android.app.Activity;
import android.content.Intent;

import eu.tinoba.androidarcitecturetemplate.ui.home.HomeActivity;

public final class CartRouterImpl implements CartRouter {

    private final Activity activity;

    public CartRouterImpl(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public void goToHomeActivity() {
        final Intent intent = HomeActivity.createIntent(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}
