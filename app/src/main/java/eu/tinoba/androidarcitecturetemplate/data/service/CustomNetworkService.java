package eu.tinoba.androidarcitecturetemplate.data.service;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.ChekoutApiRequest;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ChekoutApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.HistoryApiResponse;
import io.reactivex.Single;

public interface CustomNetworkService {

    Single<ChekoutApiResponse> checkout(ChekoutApiRequest chekoutApiRequest);

    Single<List<HistoryApiResponse>> getHistories();

    Single<HistoryApiResponse> getHistory(String id);
}

