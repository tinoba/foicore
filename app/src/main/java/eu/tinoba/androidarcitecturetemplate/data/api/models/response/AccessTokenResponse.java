package eu.tinoba.androidarcitecturetemplate.data.api.models.response;

import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse {

    @SerializedName("access_token")
    public final String accessToken;

    public AccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
