package eu.tinoba.androidarcitecturetemplate.ui.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.models.response.HistoriesApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.service.CustomNetworkService;
import eu.tinoba.androidarcitecturetemplate.domain.models.HistoryPlan;
import eu.tinoba.androidarcitecturetemplate.ui.base.presenters.BasePresenter;
import io.reactivex.Scheduler;
import timber.log.Timber;

public final class HomePresenterImpl extends BasePresenter implements HomePresenter {

    private final Scheduler subscribeScheduler;
    private final Scheduler observeScheduler;
    private final CustomNetworkService customNetworkService;

    private HomeView view;

    public HomePresenterImpl(final Scheduler subscribeScheduler, final Scheduler observeScheduler, final CustomNetworkService customNetworkService) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.customNetworkService = customNetworkService;
    }

    @Override
    public void setView(final HomeView view) {
        this.view = view;
    }

    @Override
    public void getHistory() {
        if (view != null) {
            addDisposable(customNetworkService.getHistories()
                                              .observeOn(observeScheduler)
                                              .subscribeOn(subscribeScheduler)
                                              .subscribe(this::onGetHistorySuccess, this::onGetHistoryFailure));
        }
    }

    private void onGetHistoryFailure(final Throwable throwable) {
        Timber.e(throwable);
    }

    private void onGetHistorySuccess(final List<HistoriesApiResponse> historyApiResponses) {
        if (view != null) {
            final List<HistoryPlan> planList = new ArrayList<>(historyApiResponses.size());
            for (HistoriesApiResponse response : historyApiResponses) {
                planList.add(new HistoryPlan(response.id, response.store, response.address, response.checkoutDate, response.total));
            }
            Collections.reverse(planList);
            view.renderView(planList);
        }
    }
}
