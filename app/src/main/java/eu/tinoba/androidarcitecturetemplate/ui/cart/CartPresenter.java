package eu.tinoba.androidarcitecturetemplate.ui.cart;

public interface CartPresenter {

    void setView(CartView view);

    void addProductToCart(String id);
}
