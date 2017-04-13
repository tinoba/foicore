package eu.tinoba.androidarcitecturetemplate.data.api.models.request;

import com.google.gson.annotations.SerializedName;

public final class LoginApiRequest {

    @SerializedName("password")
    public final String password;

    @SerializedName("email")
    public final String email;

    public LoginApiRequest(final String password, final String email) {
        this.password = password;
        this.email = email;
    }
}
