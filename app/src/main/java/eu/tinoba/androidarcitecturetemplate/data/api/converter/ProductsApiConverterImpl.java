package eu.tinoba.androidarcitecturetemplate.data.api.converter;

import java.util.ArrayList;
import java.util.List;

import eu.tinoba.androidarcitecturetemplate.data.api.models.response.ProductApiResponse;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;

public final class ProductsApiConverterImpl implements ProductsApiConverter {

    @Override
    public List<Product> convertApiProductsToProducts(final List<ProductApiResponse> response) {
        final List<Product> products = new ArrayList<>(response.size());

        for (ProductApiResponse apiResponse : response) {
            products.add(new Product(apiResponse.name.en, 1, apiResponse.media.get(0).url, apiResponse.description.en, apiResponse.mixins.price.originalAmount));
        }
        return products;
    }
}
