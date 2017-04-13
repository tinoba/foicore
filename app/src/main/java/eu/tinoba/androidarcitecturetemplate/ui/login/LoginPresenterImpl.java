package eu.tinoba.androidarcitecturetemplate.ui.login;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.LoginApiRequest;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.AccessTokenResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.LoginApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.service.NetworkService;
import eu.tinoba.androidarcitecturetemplate.data.storage.TemplatePreferences;
import eu.tinoba.androidarcitecturetemplate.manager.StringManager;
import eu.tinoba.androidarcitecturetemplate.ui.base.presenters.BasePresenter;
import io.reactivex.Scheduler;
import timber.log.Timber;

public final class LoginPresenterImpl extends BasePresenter implements LoginPresenter {

    private final Scheduler subscribeScheduler;
    private final Scheduler observeScheduler;
    private final StringManager stringManager;
    private final NetworkService networkService;
    private final TemplatePreferences templatePreferences;
    private final LoginRouter loginRouter;

    private LoginView view;

    public LoginPresenterImpl(final Scheduler subscribeScheduler, final Scheduler observeScheduler, final StringManager stringManager,
                              final NetworkService networkService, final TemplatePreferences templatePreferences, final LoginRouter loginRouter) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.stringManager = stringManager;
        this.networkService = networkService;
        this.templatePreferences = templatePreferences;
        this.loginRouter = loginRouter;
    }

    @Override
    public void setView(final LoginView view) {
        this.view = view;
    }

    @Override
    public void login(final LoginApiRequest loginApiRequest) {
        if (view != null) {

            addDisposable(networkService.getToken()
                                        .observeOn(observeScheduler)
                                        .subscribeOn(subscribeScheduler)
                                        .subscribe(accessTokenResponse -> onGetTokenSuccess(loginApiRequest, accessTokenResponse), this::onGetTokenFailure));
        }
    }

    private void onGetTokenSuccess(final LoginApiRequest loginApiRequest, final AccessTokenResponse accessTokenResponse) {
        if (view != null) {
            Timber.e(accessTokenResponse.accessToken);
            addDisposable(networkService.login(loginApiRequest, accessTokenResponse.accessToken)
                                        .subscribeOn(subscribeScheduler)
                                        .observeOn(observeScheduler)
                                        .subscribe(this::onLoginSuccess, this::onLoginFailure));
        }
    }

    private void onGetTokenFailure(final Throwable throwable) {
        Timber.e(throwable);
    }

    private void onLoginFailure(final Throwable throwable) {
        Timber.e(throwable);
    }

    private void onLoginSuccess(final LoginApiResponse loginApiResponse) {
        Timber.e(loginApiResponse.accessToken);
        templatePreferences.setAccessToken(loginApiResponse.accessToken);
        loginRouter.goToHomeScreen();
    }
}
