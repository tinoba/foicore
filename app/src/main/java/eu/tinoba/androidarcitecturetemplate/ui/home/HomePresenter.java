package eu.tinoba.androidarcitecturetemplate.ui.home;

public interface HomePresenter {

    void setView(HomeView view);

    void addProductToCart(String id);
}
