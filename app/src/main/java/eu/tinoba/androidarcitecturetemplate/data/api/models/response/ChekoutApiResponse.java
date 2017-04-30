package eu.tinoba.androidarcitecturetemplate.data.api.models.response;

import com.google.gson.annotations.SerializedName;

public final class ChekoutApiResponse {

    @SerializedName("success")
    public final String success;

    public ChekoutApiResponse(final String success) {
        this.success = success;
    }
}
