package eu.tinoba.androidarcitecturetemplate.injection.component;

import eu.tinoba.androidarcitecturetemplate.ui.home.HomeActivity;
import eu.tinoba.androidarcitecturetemplate.ui.login.LoginActivity;
import eu.tinoba.androidarcitecturetemplate.ui.qr.QRActivity;

public interface ActivityComponentActivityInjects {

    void inject(HomeActivity homeActivity);

    void inject(LoginActivity loginActivity);

    void inject(QRActivity qrActivity);

}
