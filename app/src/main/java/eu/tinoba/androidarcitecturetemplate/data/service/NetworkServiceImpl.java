package eu.tinoba.androidarcitecturetemplate.data.service;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.LoginApiRequest;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.AccessTokenResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.LoginApiResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ProductApiResponse;
import io.reactivex.Single;

import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.CLIENT_ID;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.CLIENT_SECRET;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.GRANT_TYPE;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.PRODUCT_QUERY;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.SCOPE;

public final class NetworkServiceImpl implements NetworkService {

    private final TemplateAPI templateAPI;

    public NetworkServiceImpl(final TemplateAPI templateAPI) {
        this.templateAPI = templateAPI;
    }

    @Override
    public Single<LoginApiResponse> login(final LoginApiRequest loginApiRequest, final String authorization) {
        return Single.defer(() -> templateAPI.login("Bearer " + authorization, loginApiRequest));
    }

    @Override
    public Single<AccessTokenResponse> getToken() {
        return Single.defer(() -> templateAPI.getToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, SCOPE));
    }

    @Override
    public Single<List<ProductApiResponse>> getProduct(final String authorization, final String id) {
        return Single.defer(() -> templateAPI.getProduct("Bearer " + authorization, PRODUCT_QUERY + id));
    }
}
