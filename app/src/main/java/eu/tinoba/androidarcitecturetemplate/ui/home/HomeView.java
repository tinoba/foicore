package eu.tinoba.androidarcitecturetemplate.ui.home;

import java.util.List;

import eu.tinoba.androidarcitecturetemplate.domain.models.HistoryPlan;

public interface HomeView {

    void renderView(List<HistoryPlan> planList);
}
