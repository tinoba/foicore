package eu.tinoba.androidarcitecturetemplate.ui.home;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ProductApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.service.NetworkService;
import eu.tinoba.androidarcitecturetemplate.manager.StringManager;
import eu.tinoba.androidarcitecturetemplate.ui.base.presenters.BasePresenter;
import io.reactivex.Scheduler;
import timber.log.Timber;

public final class HomePresenterImpl extends BasePresenter implements HomePresenter {

    private final Scheduler subscribeScheduler;
    private final Scheduler observeScheduler;
    private final StringManager stringManager;
    private final NetworkService networkService;

    private HomeView view;

    public HomePresenterImpl(final Scheduler subscribeScheduler, final Scheduler observeScheduler, final StringManager stringManager,
                             final NetworkService networkService) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.stringManager = stringManager;
        this.networkService = networkService;
    }

    @Override
    public void setView(final HomeView view) {
        this.view = view;
    }

    @Override
    public void addProductToCart(final String id) {
        if (view != null) {
            addDisposable(networkService.getProduct(id)
                                        .observeOn(observeScheduler)
                                        .subscribeOn(subscribeScheduler)
                                        .subscribe(this::onGetProductSuccess, this::onGetProductFailure));
        }
    }

    private void onGetProductSuccess(final List<ProductApiResponse> productApiResponses) {
        Timber.e(productApiResponses.get(0).name.en);
    }

    private void onGetProductFailure(final Throwable throwable) {
        Timber.e(throwable);
    }
}
