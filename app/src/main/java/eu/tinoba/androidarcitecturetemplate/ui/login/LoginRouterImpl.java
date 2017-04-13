package eu.tinoba.androidarcitecturetemplate.ui.login;

import android.app.Activity;

import eu.tinoba.androidarcitecturetemplate.ui.home.HomeActivity;

public final class LoginRouterImpl implements LoginRouter {

    private final Activity activity;

    public LoginRouterImpl(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public void goToHomeScreen() {
        activity.startActivity(HomeActivity.createIntent(activity));
    }
}
