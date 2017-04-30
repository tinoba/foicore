package eu.tinoba.androidarcitecturetemplate.ui.home;

public interface DetailsPresenter {

    void setView(DetailsView view);

    void getDetails(String id);
}
