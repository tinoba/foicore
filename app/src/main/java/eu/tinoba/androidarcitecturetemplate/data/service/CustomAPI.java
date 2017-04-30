package eu.tinoba.androidarcitecturetemplate.data.service;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.ChekoutApiRequest;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ChekoutApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.HistoryApiResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.PATH_CHECKOUT;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.PATH_HISTORY;

public interface CustomAPI {

    @POST(PATH_CHECKOUT)
    Single<ChekoutApiResponse> checkout(@Body ChekoutApiRequest chekoutApiRequest);

    @GET(PATH_HISTORY)
    Single<List<HistoryApiResponse>> getHistories();

    @GET(PATH_HISTORY)
    Single<HistoryApiResponse> getHistory(@Path("checkoutId") String checkoutId);

}
