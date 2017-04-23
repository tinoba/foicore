package eu.tinoba.androidarcitecturetemplate.domain.models;

public final class Product {

    private String name;
    private int count;
    private String imageUrl;
    private String description;
    private double price;

    public Product(final String name, final int count, final String imageUrl, final String description, final double price) {
        this.name = name;
        this.count = count;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public void increaseCount() {
        count++;
    }

    public void decreaseCount() {
        count--;
    }
}
