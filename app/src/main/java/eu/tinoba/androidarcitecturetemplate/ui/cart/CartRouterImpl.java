package eu.tinoba.androidarcitecturetemplate.ui.cart;

import android.app.Activity;

public final class CartRouterImpl implements CartRouter {

    private final Activity activity;

    public CartRouterImpl(final Activity activity) {
        this.activity = activity;
    }
}
