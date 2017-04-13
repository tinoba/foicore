package eu.tinoba.androidarcitecturetemplate.data.service;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.LoginApiRequest;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.AccessTokenResponse;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.LoginApiResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.AUTHORIZATION_HEADER;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.CONTENT_TYPE_HEADER;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.PATH_LOGIN;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.PATH_TOKEN;

public interface TemplateAPI {
/*
    @POST(PATH_LOGIN)
    Single<List<LoginResponse>> login(@Body UserInformation userInformation);

    @GET(PATH_HOTEL)
    Single<List<Hotel>> getHotel(@Path("id") long id);*/

    @Headers(CONTENT_TYPE_HEADER)
    @POST(PATH_LOGIN)
    Single<LoginApiResponse> login(@Header(AUTHORIZATION_HEADER) String authorization, @Body LoginApiRequest loginApiRequest);

    @FormUrlEncoded
    @POST(PATH_TOKEN)
    Single<AccessTokenResponse> getToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("scope") String scope
    );
}
