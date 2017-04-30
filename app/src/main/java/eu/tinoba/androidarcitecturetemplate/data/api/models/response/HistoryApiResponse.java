package eu.tinoba.androidarcitecturetemplate.data.api.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class HistoryApiResponse {

    @SerializedName("id")
    public final String id;

    @SerializedName("store")
    public final String store;

    @SerializedName("address")
    public final String address;

    @SerializedName("total")
    public final String total;

    @SerializedName("checkoutDate")
    public final String checkoutDate;

    @SerializedName("customerId")
    public final String customerId;

    @SerializedName("products")
    public final List<Products> products;

    public HistoryApiResponse(final String id, final String store, final String address, final String total, final String checkoutDate, final String customerId,
                              final List<Products> products) {
        this.id = id;
        this.store = store;
        this.address = address;
        this.total = total;
        this.checkoutDate = checkoutDate;
        this.customerId = customerId;
        this.products = products;
    }

    public final class Products {

        @SerializedName("name")
        public final String name;

        @SerializedName("description")
        public final String description;

        @SerializedName("url")
        public final String url;

        @SerializedName("quantity")
        public final String quantity;

        @SerializedName("price")
        public final String price;

        public Products(final String name, final String description, final String url, final String quantity, final String price) {
            this.name = name;
            this.description = description;
            this.url = url;
            this.quantity = quantity;
            this.price = price;
        }
    }
}
