package eu.tinoba.androidarcitecturetemplate.ui.cart;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.domain.models.Product;

public interface CartView {

    void addItemToCart(List<Product> productApiResponse);
}
