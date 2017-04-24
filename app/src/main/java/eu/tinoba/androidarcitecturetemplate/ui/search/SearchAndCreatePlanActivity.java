package eu.tinoba.androidarcitecturetemplate.ui.search;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;
import eu.tinoba.androidarcitecturetemplate.injection.component.ActivityComponent;
import eu.tinoba.androidarcitecturetemplate.ui.base.activities.BaseActivity;
import eu.tinoba.androidarcitecturetemplate.ui.cart.CartListAdapter;

public class SearchAndCreatePlanActivity extends BaseActivity implements SearchProductResultFragment.OnFragmentInteractionListener, CartListAdapter.Listener {

    public static final String RESULTS_BACK_STACK = "results";

    @BindView(R.id.search_bar)
    Toolbar toolbar;
    @BindView(R.id.search_product)
    TextInputEditText searchProduct;
    @BindView(R.id.search_and_create_plan_activity_main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.activity_search_and_plan_recycler_view)
    RecyclerView planRecyclerView;

    CartListAdapter cartListAdapter;

    List<Product> productList = new ArrayList<>();

    int position;

    SearchProductResultFragment searchFragment = new SearchProductResultFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_plan);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.home_screen);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cartListAdapter = new CartListAdapter(this);

        planRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartListAdapter.setListener(this);
        planRecyclerView.setAdapter(cartListAdapter);

        searchProduct.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_white_24dp, 0, 0, 0);

        setUpText();
        setOnFocusListener();
    }

    private void setOnFocusListener() {
        searchProduct.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                getSupportFragmentManager().beginTransaction()
                                           .add(R.id.content, searchFragment)
                                           .addToBackStack(RESULTS_BACK_STACK)
                                           .commit();
                planRecyclerView.setVisibility(View.GONE);
            } else {
                getSupportFragmentManager().beginTransaction().remove(searchFragment).commit();
                planRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpText() {
        searchProduct.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
                searchFragment.searchProducts(searchProduct.getText().toString());
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (editable.toString().length() > 0) {
                    searchProduct.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    searchProduct.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_white_24dp, 0, 0, 0);
                }
            }
        });
    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public void onProductSelected(final Product prod) {
        searchProduct.setText("");
        searchProduct.clearFocus();
        mainLayout.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchProduct.getWindowToken(), 0);

        boolean productInList = false;
        for (final Product product : productList) {
            if (product.getName().equals(prod.getName())) {
                product.increaseCount();
                productInList = true;
            }
        }
        if (!productInList) {
            productList.add(new Product(prod.getName(), prod.getCount(), prod.getImageUrl(), prod.getDescription(), prod.getPrice()));
        }
        cartListAdapter.setData(productList);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (searchFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().remove(searchFragment).commit();
            planRecyclerView.setVisibility(View.VISIBLE);
            searchProduct.setText("");
            searchProduct.clearFocus();
            mainLayout.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchProduct.getWindowToken(), 0);
        } else {
            finish();
        }
    }

    @Override
    public void openRemoveDialog(final int position) {
        this.position = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove this item?").setPositiveButton("Yes", onClickListener)
               .setNegativeButton("No", onClickListener).show();
    }

    @Override
    public void countChanged() {
        //todo izracunaj cijenu, spremi plan dodati
    }

    DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                productList.get(position).decreaseCount();
                productList.remove(position);
                cartListAdapter.setData(productList);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    };
}
