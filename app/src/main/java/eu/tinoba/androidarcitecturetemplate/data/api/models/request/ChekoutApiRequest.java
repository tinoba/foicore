package eu.tinoba.androidarcitecturetemplate.data.api.models.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public final class ChekoutApiRequest {

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

    public ChekoutApiRequest(final String id, final String store, final String address, final String total, final String checkoutDate, final String customerId,
                             final List<Products> products) {
        this.id = id;
        this.store = store;
        this.address = address;
        this.total = total;
        this.checkoutDate = checkoutDate;
        this.customerId = customerId;
        this.products = products;
    }

    public static final class Products {

        @SerializedName("id")
        public final String id;

        @SerializedName("quantity")
        public final String quantity;

        public Products(final String id, final String quantity) {
            this.id = id;
            this.quantity = quantity;
        }
    }
}
