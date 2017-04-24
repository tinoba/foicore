package eu.tinoba.androidarcitecturetemplate.ui.search;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.domain.models.Product;

public interface SearchProductResultView {

    void showData(List<Product> products);
}
