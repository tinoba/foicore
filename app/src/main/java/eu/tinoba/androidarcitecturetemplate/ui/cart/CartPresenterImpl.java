package eu.tinoba.androidarcitecturetemplate.ui.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ProductApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.service.NetworkService;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;
import eu.tinoba.androidarcitecturetemplate.manager.StringManager;
import eu.tinoba.androidarcitecturetemplate.ui.base.presenters.BasePresenter;
import io.reactivex.Scheduler;
import timber.log.Timber;

public final class CartPresenterImpl extends BasePresenter implements CartPresenter {

    private final Scheduler subscribeScheduler;
    private final Scheduler observeScheduler;
    private final StringManager stringManager;
    private final NetworkService networkService;

    private Map<String, Product> products = new HashMap<>();

    private CartView view;

    public CartPresenterImpl(final Scheduler subscribeScheduler, final Scheduler observeScheduler, final StringManager stringManager,
                             final NetworkService networkService) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.stringManager = stringManager;
        this.networkService = networkService;
    }

    @Override
    public void setView(final CartView view) {
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
        if (view != null) {
            final Product product = products.get(productApiResponses.get(0).name.en);
            if (product != null) {
                product.increaseCount();
            } else {
                products.put(productApiResponses.get(0).name.en, new Product(productApiResponses.get(0).name.en, 1));
            }
            view.addItemToCart(new ArrayList<>(products.values()));
        }
    }

    private void onGetProductFailure(final Throwable throwable) {
        Timber.e(throwable);
    }
}