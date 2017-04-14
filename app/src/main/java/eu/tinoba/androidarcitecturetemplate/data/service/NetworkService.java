package eu.tinoba.androidarcitecturetemplate.data.service;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.LoginApiRequest;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.AccessTokenResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.LoginApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ProductApiResponse;
import io.reactivex.Single;

public interface NetworkService {

    Single<LoginApiResponse> login(LoginApiRequest loginApiRequest, String authorization);

    Single<AccessTokenResponse> getToken();

    Single<List<ProductApiResponse>> getProduct(String id);
}

