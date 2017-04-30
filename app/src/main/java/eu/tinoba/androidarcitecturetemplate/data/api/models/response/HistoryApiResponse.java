package eu.tinoba.androidarcitecturetemplate.data.api.models.response;

import com.google.gson.annotations.SerializedName;

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

    public HistoryApiResponse(final String id, final String store, final String address, final String total, final String checkoutDate, final String customerId) {
        this.id = id;
        this.store = store;
        this.address = address;
        this.total = total;
        this.checkoutDate = checkoutDate;
        this.customerId = customerId;
    }
}
