package eu.tinoba.androidarcitecturetemplate.injection.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import eu.tinoba.androidarcitecturetemplate.data.service.NetworkService;
import eu.tinoba.androidarcitecturetemplate.data.storage.TemplatePreferences;
import eu.tinoba.androidarcitecturetemplate.injection.scope.ForActivity;
import eu.tinoba.androidarcitecturetemplate.manager.StringManager;
import eu.tinoba.androidarcitecturetemplate.ui.cart.CartPresenter;
import eu.tinoba.androidarcitecturetemplate.ui.cart.CartPresenterImpl;
import eu.tinoba.androidarcitecturetemplate.ui.login.LoginPresenter;
import eu.tinoba.androidarcitecturetemplate.ui.login.LoginPresenterImpl;
import eu.tinoba.androidarcitecturetemplate.ui.login.LoginRouter;
import io.reactivex.Scheduler;

import static eu.tinoba.androidarcitecturetemplate.injection.module.ThreadingModule.OBSERVE_SCHEDULER;
import static eu.tinoba.androidarcitecturetemplate.injection.module.ThreadingModule.SUBSCRIBE_SCHEDULER;

@Module
public final class PresenterModule {

    @ForActivity
    @Provides
    CartPresenter provideCartPresenter(@Named(SUBSCRIBE_SCHEDULER) final Scheduler subscribeScheduler, final NetworkService networkService,
                                       @Named(OBSERVE_SCHEDULER) final Scheduler observeScheduler, final StringManager stringManager,
                                       final TemplatePreferences templatePreferences) {
        return new CartPresenterImpl(subscribeScheduler, observeScheduler, stringManager, networkService, templatePreferences);
    }

    @ForActivity
    @Provides
    LoginPresenter provideLoginPresenter(@Named(SUBSCRIBE_SCHEDULER) final Scheduler subscribeScheduler, final NetworkService networkService,
                                         @Named(OBSERVE_SCHEDULER) final Scheduler observeScheduler, final StringManager stringManager,
                                         final TemplatePreferences templatePreferences, final LoginRouter loginRouter) {
        return new LoginPresenterImpl(subscribeScheduler, observeScheduler, stringManager, networkService, templatePreferences, loginRouter);
    }
}
