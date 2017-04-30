package eu.tinoba.androidarcitecturetemplate.ui.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.ChekoutApiRequest;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ChekoutApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ProductApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.service.CustomNetworkService;
import eu.tinoba.androidarcitecturetemplate.data.service.NetworkService;
import eu.tinoba.androidarcitecturetemplate.data.storage.TemplatePreferences;
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
    private final TemplatePreferences templatePreferences;
    private final CustomNetworkService customNetworkService;
    private final CartRouter cartRouter;

    private Map<String, Product> products = new HashMap<>();

    private CartView view;

    public CartPresenterImpl(final Scheduler subscribeScheduler, final Scheduler observeScheduler, final StringManager stringManager,
                             final NetworkService networkService, final TemplatePreferences templatePreferences, final CustomNetworkService customNetworkService,
                             final CartRouter cartRouter) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.stringManager = stringManager;
        this.networkService = networkService;
        this.templatePreferences = templatePreferences;
        this.customNetworkService = customNetworkService;
        this.cartRouter = cartRouter;
    }

    @Override
    public void setView(final CartView view) {
        this.view = view;
    }

    @Override
    public void addProductToCart(final String id) {
        if (view != null) {
            addDisposable(networkService.getProduct(templatePreferences.getBaseToken(), id)
                                        .observeOn(observeScheduler)
                                        .subscribeOn(subscribeScheduler)
                                        .subscribe(responses -> onGetProductSuccess(responses, id), this::onGetProductFailure));
        }
    }

    private void onGetProductSuccess(final List<ProductApiResponse> productApiResponses, final String id) {
        if (!productApiResponses.isEmpty()) {
            Timber.e(productApiResponses.get(0).name.en);
            if (view != null) {
                final Product product = products.get(productApiResponses.get(0).name.en);
                if (product != null) {
                    product.increaseCount();
                } else {
                    products.put(productApiResponses.get(0).name.en,
                                 new Product(productApiResponses.get(0).name.en, 1, productApiResponses.get(0).media.get(0).url, productApiResponses.get(0).description.en,
                                             productApiResponses.get(0).mixins.price.originalAmount, id));
                }
                view.addItemToCart(new ArrayList<>(products.values()));
            }
        }
    }

    private void onGetProductFailure(final Throwable throwable) {
        Timber.e(throwable);
    }

    @Override
    public void removeProductFromMap(final String name) {
        products.remove(name);
    }

    @Override
    public void checkout(final ChekoutApiRequest chekoutApiRequest) {
        if (view != null) {
            addDisposable(customNetworkService.checkout(chekoutApiRequest)
                                              .observeOn(observeScheduler)
                                              .subscribeOn(subscribeScheduler)
                                              .subscribe(this::onChekoutSuccess, this::onChekoutFailure));
        }
    }

    private void onChekoutFailure(final Throwable throwable) {
        Timber.e(throwable);
    }

    private void onChekoutSuccess(final ChekoutApiResponse chekoutApiResponse) {
        cartRouter.goToHomeActivity();
    }
}
