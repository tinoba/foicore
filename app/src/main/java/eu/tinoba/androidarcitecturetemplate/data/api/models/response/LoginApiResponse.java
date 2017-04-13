package eu.tinoba.androidarcitecturetemplate.data.api.models.response;

import com.google.gson.annotations.SerializedName;

public final class LoginApiResponse {

    @SerializedName("accessToken")
    public final String accessToken;

    public LoginApiResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
