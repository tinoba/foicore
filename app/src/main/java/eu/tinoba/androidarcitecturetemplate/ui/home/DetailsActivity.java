package eu.tinoba.androidarcitecturetemplate.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.HistoryApiResponse;
import eu.tinoba.androidarcitecturetemplate.injection.component.ActivityComponent;
import eu.tinoba.androidarcitecturetemplate.ui.base.activities.BaseActivity;

public class DetailsActivity extends BaseActivity implements DetailsView {

    @Inject
    DetailsPresenter presenter;

    private static final String ID_KEY = "id_key";
    private String id;

    public static Intent createIntent(final Context context, final String id) {
        return new Intent(context, DetailsActivity.class).putExtra(ID_KEY, id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        id = getIntent().getStringExtra(ID_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.setView(this);
        if (id != null) {
            presenter.getDetails(id);
        }
    }

    @Override
    public void renderView(final HistoryApiResponse response) {

    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
