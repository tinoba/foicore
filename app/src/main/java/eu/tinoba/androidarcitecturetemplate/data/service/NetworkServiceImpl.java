package eu.tinoba.androidarcitecturetemplate.data.service;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.LoginApiRequest;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.AccessTokenResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.LoginApiResponse;
import io.reactivex.Single;

import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.CLIENT_ID;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.CLIENT_SECRET;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.GRANT_TYPE;
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
}
