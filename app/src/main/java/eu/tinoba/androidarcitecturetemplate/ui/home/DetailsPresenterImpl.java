package eu.tinoba.androidarcitecturetemplate.ui.home;

import eu.tinoba.androidarcitecturetemplate.data.api.models.response.HistoryApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.service.CustomNetworkService;
import eu.tinoba.androidarcitecturetemplate.ui.base.presenters.BasePresenter;
import io.reactivex.Scheduler;
import timber.log.Timber;

public class DetailsPresenterImpl extends BasePresenter implements DetailsPresenter {

    private final Scheduler subscribeScheduler;
    private final Scheduler observeScheduler;
    private final CustomNetworkService customNetworkService;

    public DetailsPresenterImpl(final Scheduler subscribeScheduler, final Scheduler observeScheduler, final CustomNetworkService customNetworkService) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.customNetworkService = customNetworkService;
    }

    private DetailsView view;

    @Override
    public void setView(final DetailsView view) {
        this.view = view;
    }

    @Override
    public void getDetails(final String id) {
        if (view != null) {
            addDisposable(customNetworkService.getHistory(id)
                                              .observeOn(observeScheduler)
                                              .subscribeOn(subscribeScheduler)
                                              .subscribe(this::onGetHistorySuccess, this::onGetHistoryFailure));
        }
    }

    private void onGetHistoryFailure(final Throwable throwable) {
        Timber.e(throwable);
    }

    private void onGetHistorySuccess(final HistoryApiResponse response) {
        if (view != null) {
            view.renderView(response);
        }
    }
}
