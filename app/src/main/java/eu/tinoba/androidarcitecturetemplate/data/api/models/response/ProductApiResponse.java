package eu.tinoba.androidarcitecturetemplate.data.api.models.response;

import com.google.gson.annotations.SerializedName;

public final class ProductApiResponse {

    @SerializedName("name")
    public final Name name;

    public ProductApiResponse(final Name name) {
        this.name = name;
    }

    public final class Name {

        @SerializedName("en")
        public final String en;

        public Name(final String en) {
            this.en = en;
        }
    }
}
