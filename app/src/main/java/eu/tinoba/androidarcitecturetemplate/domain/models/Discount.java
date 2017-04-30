package eu.tinoba.androidarcitecturetemplate.domain.models;

public class Discount {

    String name;
    String oldPrice;
    String newPrice;
    String description;
    String discountTillDate;
    int imageId;
    String discountPercentage;

    public Discount(final String name, final String oldPrice, final String newPrice, final String description, final String discountTillDate, final int imageId,
                    final String discountPercentage) {
        this.name = name;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.description = description;
        this.discountTillDate = discountTillDate;
        this.imageId = imageId;
        this.discountPercentage = discountPercentage;
    }

    public String getName() {
        return name;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getDiscountTillDate() {
        return discountTillDate;
    }

    public int getImageId() {
        return imageId;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }
}
