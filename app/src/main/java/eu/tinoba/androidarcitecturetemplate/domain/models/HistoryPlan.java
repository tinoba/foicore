package eu.tinoba.androidarcitecturetemplate.domain.models;

public class HistoryPlan {

    String historyId;
    String shopName;
    String shopAddress;
    String dateOfShopping;
    String priceOfPurchase;

    public HistoryPlan(final String historyId, final String shopName, final String shopAddress, final String dateOfShopping, final String priceOfPurchase) {
        this.historyId = historyId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.dateOfShopping = dateOfShopping;
        this.priceOfPurchase = priceOfPurchase;
    }

    public String getHistoryId() {
        return historyId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getDateOfShopping() {
        return dateOfShopping;
    }

    public String getPriceOfPurchase() {
        return priceOfPurchase;
    }
}
