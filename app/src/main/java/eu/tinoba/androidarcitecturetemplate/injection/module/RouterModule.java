package eu.tinoba.androidarcitecturetemplate.injection.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import eu.tinoba.androidarcitecturetemplate.injection.scope.ForActivity;
import eu.tinoba.androidarcitecturetemplate.ui.home.HomeRouter;
import eu.tinoba.androidarcitecturetemplate.ui.home.HomeRouterImpl;
import eu.tinoba.androidarcitecturetemplate.ui.login.LoginRouter;
import eu.tinoba.androidarcitecturetemplate.ui.login.LoginRouterImpl;

@Module
public final class RouterModule {

    @ForActivity
    @Provides
    HomeRouter provideHomeRouter(final Activity activity) {
        return new HomeRouterImpl(activity);
    }

    @ForActivity
    @Provides
    LoginRouter provideLoginRouter(final Activity activity) {
        return new LoginRouterImpl(activity);
    }
}
