package eu.tinoba.androidarcitecturetemplate.data.api.converter;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ProductApiResponse;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;

public interface ProductsApiConverter {

    List<Product> convertApiProductsToProducts(List<ProductApiResponse> response);
}
