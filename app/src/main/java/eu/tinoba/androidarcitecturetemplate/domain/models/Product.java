package eu.tinoba.androidarcitecturetemplate.domain.models;

public final class Product {

    private String name;
    private int count;

    public Product(final String name, final int count) {
        this.name = name;
        this.count = count;
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

    public void decreaseCount(){
        count--;
    }
}
