package eu.tinoba.androidarcitecturetemplate.ui.cart;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.ChekoutApiRequest;

public interface CartPresenter {

    void setView(CartView view);

    void addProductToCart(String id);

    void removeProductFromMap(String name);

    void checkout(ChekoutApiRequest chekoutApiRequest);
}