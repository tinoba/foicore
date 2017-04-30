package eu.tinoba.androidarcitecturetemplate.data.service;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.ChekoutApiRequest;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ChekoutApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.HistoryApiResponse;
import io.reactivex.Single;

public final class CustomNetworkServiceImpl implements CustomNetworkService {

    private final CustomAPI customAPI;

    public CustomNetworkServiceImpl(final CustomAPI customAPI) {
        this.customAPI = customAPI;
    }

    @Override
    public Single<ChekoutApiResponse> checkout(final ChekoutApiRequest chekoutApiRequest) {
        return Single.defer(() -> customAPI.checkout(chekoutApiRequest));
    }

    @Override
    public Single<List<HistoryApiResponse>> getHistories() {
        return Single.defer(customAPI::getHistories);
    }

    @Override
    public Single<HistoryApiResponse> getHistory(final String id) {
        return Single.defer(()-> customAPI.getHistory(id));
    }
}
