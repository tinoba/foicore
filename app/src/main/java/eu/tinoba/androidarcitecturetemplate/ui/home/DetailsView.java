package eu.tinoba.androidarcitecturetemplate.ui.home;

import eu.tinoba.androidarcitecturetemplate.data.api.models.response.HistoryApiResponse;

public interface DetailsView {

    void renderView(HistoryApiResponse response);
}
