package eu.tinoba.androidarcitecturetemplate.ui.search;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.converter.ProductsApiConverter;
import eu.tinoba.androidarcitecturetemplate.data.service.NetworkService;
import eu.tinoba.androidarcitecturetemplate.data.storage.TemplatePreferences;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;
import eu.tinoba.androidarcitecturetemplate.ui.base.presenters.BasePresenter;
import io.reactivex.Scheduler;
import timber.log.Timber;

public final class SearchProductResultPresenterImpl extends BasePresenter implements SearchProductResultPresenter {

    private final Scheduler subscribeScheduler;
    private final Scheduler observeScheduler;
    private final NetworkService networkService;
    private final TemplatePreferences templatePreferences;
    private final ProductsApiConverter productsApiConverter;

    public SearchProductResultPresenterImpl(final Scheduler subscribeScheduler, final Scheduler observeScheduler, final NetworkService networkService,
                                            final TemplatePreferences templatePreferences, final ProductsApiConverter productsApiConverter) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.networkService = networkService;
        this.templatePreferences = templatePreferences;
        this.productsApiConverter = productsApiConverter;
    }

    private SearchProductResultView view;

    @Override
    public void setView(final SearchProductResultView view) {
        this.view = view;
    }

    @Override
    public void renderView() {
        addDisposable(networkService.getProducts(templatePreferences.getBaseToken())
                                    .observeOn(observeScheduler)
                                    .subscribeOn(subscribeScheduler)
                                    .map(productsApiConverter::convertApiProductsToProducts)
                                    .subscribe(this::onGetProductsSuccess, this::onGetProductsFailure));
    }

    private void onGetProductsSuccess(final List<Product> products) {
        if (view != null) {
            view.showData(products);
        }
    }

    private void onGetProductsFailure(final Throwable throwable) {
        Timber.e(throwable);
    }
}

