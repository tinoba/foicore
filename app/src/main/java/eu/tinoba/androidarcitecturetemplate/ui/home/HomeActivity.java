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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.HistoryPlan;
import eu.tinoba.androidarcitecturetemplate.injection.component.ActivityComponent;
import eu.tinoba.androidarcitecturetemplate.ui.base.activities.BaseActivity;
import eu.tinoba.androidarcitecturetemplate.ui.qr.QRActivity;

public class HomeActivity extends BaseActivity implements HomeActivityRecyclerViewAdapter.Listener {

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

        //TODO REMOVE TEST DATA

        planInfoList.add(new HistoryPlan("1", "Prodavonica br 2", "Pavliska 24 Varaždin", "11.4.2017", "240.45 kn"));
        planInfoList.add(new HistoryPlan("2", "Prodavonica br 21", "Pavliska 24 Varaždin", "11.4.2017", "240.45 kn"));
        planInfoList.add(new HistoryPlan("3", "Prodavonica br 4", "Pavliska 24 Varaždin", "11.4.2017", "240.45 kn"));
        planInfoList.add(new HistoryPlan("4", "Prodavonica br 15", "Pavliska 24 Varaždin", "11.4.2017", "240.45 kn"));
        planInfoList.add(new HistoryPlan("5", "Prodavonica br 56", "Pavliska 24 Varaždin", "11.4.2017", "240.45 kn"));
        planInfoList.add(new HistoryPlan("6", "Prodavonica br 41", "Pavliska 24 Varaždin", "11.4.2017", "240.45 kn"));
        planInfoList.add(new HistoryPlan("7", "Prodavonica br 56", "Pavliska 24 Varaždin", "11.4.2017", "240.45 kn"));
        planInfoList.add(new HistoryPlan("8", "Prodavonica br 142", "Pavliska 24 Varaždin", "11.4.2017", "240.45 kn"));
        planInfoList.add(new HistoryPlan("9", "Prodavonica br 6", "Pavliska 24 Varaždin", "11.4.2017", "240.45 kn"));

        recyclerViewAdapter.setData(planInfoList);

        addOnScrollListener();
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
        //todo add check if mobile is connected to a bluetooth, if it is then go directly to chart activity else go to qr scanner
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
                Toast.makeText(this, "OPEN PLAN ACTIVITY", Toast.LENGTH_SHORT).show();
                break;
            case R.id.discount:
                Toast.makeText(this, "OPEN DISCOUNT ACTIVITY", Toast.LENGTH_SHORT).show();
                break;
            case R.id.map:
                Toast.makeText(this, "OPEN MAP ACTIVITY", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "id kliknutog: " + id, Toast.LENGTH_SHORT).show();
    }
}
