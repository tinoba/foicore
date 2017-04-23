package eu.tinoba.androidarcitecturetemplate.domain.models;

import com.google.android.gms.maps.model.LatLng;

public class MarkerModel {

    private LatLng latLng;
    private String shopName;
    private String shopAddress;
    private int shopImageId;

    public MarkerModel(final LatLng latLng, final String shopName, final String shopAddress, final int shopImageId) {
        this.latLng = latLng;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopImageId = shopImageId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public int getShopImageId() {
        return shopImageId;
    }
}
