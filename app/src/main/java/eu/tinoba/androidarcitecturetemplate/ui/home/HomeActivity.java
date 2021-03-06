package eu.tinoba.androidarcitecturetemplate.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.HistoryPlan;
import eu.tinoba.androidarcitecturetemplate.injection.component.ActivityComponent;
import eu.tinoba.androidarcitecturetemplate.ui.base.activities.BaseActivity;
import eu.tinoba.androidarcitecturetemplate.ui.discount.DiscountActivity;
import eu.tinoba.androidarcitecturetemplate.ui.map.ShopMapActivity;
import eu.tinoba.androidarcitecturetemplate.ui.qr.QRActivity;
import eu.tinoba.androidarcitecturetemplate.ui.search.SearchAndCreatePlanActivity;

public class HomeActivity extends BaseActivity implements HomeActivityRecyclerViewAdapter.Listener, HomeView {

    @Inject
    HomePresenter presenter;

    HomeActivityRecyclerViewAdapter recyclerViewAdapter;

    LinearLayoutManager linearLayoutManager;

    List<HistoryPlan> planInfoList = new ArrayList<>();

    @BindView(R.id.activity_home_list_of_plans)
    RecyclerView recyclerViewPlans;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.activity_home_toolbar)
    protected Toolbar toolbar;

    public static Intent createIntent(final Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.home_screen);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);

        recyclerViewAdapter = new HomeActivityRecyclerViewAdapter();
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerViewPlans.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter.setListener(this);
        recyclerViewPlans.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setData(planInfoList);

        addOnScrollListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.setView(this);
        presenter.getHistory();
    }

    private void addOnScrollListener() {
        recyclerViewPlans.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fab.hide();
                } else if (dy < 0) {
                    fab.show();
                }
            }
        });
    }

    @OnClick(R.id.fab)
    public void goToChart() {
        startActivity(new Intent(HomeActivity.this, QRActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.plan:
                startActivity(new Intent(HomeActivity.this, SearchAndCreatePlanActivity.class));
                break;
            case R.id.discount:
                startActivity(new Intent(HomeActivity.this, DiscountActivity.class));
                break;
            case R.id.map:
                startActivity(new Intent(HomeActivity.this, ShopMapActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public void getHistoryId(final String id) {
        startActivity(DetailsActivity.createIntent(this, id));
    }

    @Override
    public void renderView(final List<HistoryPlan> planList) {
        recyclerViewAdapter.setData(planList);
    }
}
