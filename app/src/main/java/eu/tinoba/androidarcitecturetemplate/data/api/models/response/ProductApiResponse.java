package eu.tinoba.androidarcitecturetemplate.data.api.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class ProductApiResponse {

    @SerializedName("name")
    public final Name name;

    @SerializedName("description")
    public final Description description;

    @SerializedName("media")
    public final List<Media> media;

    @SerializedName("mixins")
    public final Mixins mixins;

    public ProductApiResponse(final Name name, final Description description, final List<Media> media, final Mixins mixins) {
        this.name = name;
        this.description = description;
        this.media = media;
        this.mixins = mixins;
    }

    public final class Mixins {

        @SerializedName("price")
        public final Price price;

        public Mixins(final Price price) {
            this.price = price;
        }
    }

    public final class Price {

        @SerializedName("originalAmount")
        public final double originalAmount;

        public Price(final double originalAmount) {
            this.originalAmount = originalAmount;
        }
    }

    public final class Description {

        @SerializedName("en")
        public final String en;

        public Description(final String en) {
            this.en = en;
        }
    }

    public final class Name {

        @SerializedName("en")
        public final String en;

        public Name(final String en) {
            this.en = en;
        }
    }

    public final class Media {

        @SerializedName("url")
        public final String url;

        public Media(final String url) {
            this.url = url;
        }
    }
}
