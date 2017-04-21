package eu.tinoba.androidarcitecturetemplate.injection.component;

import eu.tinoba.androidarcitecturetemplate.ui.cart.CartActivity;
import eu.tinoba.androidarcitecturetemplate.ui.home.HomeActivity;
import eu.tinoba.androidarcitecturetemplate.ui.login.LoginActivity;
import eu.tinoba.androidarcitecturetemplate.ui.qr.QRActivity;

public interface ActivityComponentActivityInjects {

    void inject(CartActivity cartActivity);

    void inject(LoginActivity loginActivity);

    void inject(QRActivity qrActivity);

    void inject(HomeActivity homeActivity);

}
