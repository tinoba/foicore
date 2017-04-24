package eu.tinoba.androidarcitecturetemplate.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;
import eu.tinoba.androidarcitecturetemplate.injection.component.ActivityComponent;
import eu.tinoba.androidarcitecturetemplate.ui.base.fragments.BaseFragment;

public class SearchProductResultFragment extends BaseFragment implements SearchProductRecyclerViewAdapter.Listener, SearchProductResultView{

    @Inject
    SearchProductResultPresenter presenter;

    private OnFragmentInteractionListener listener;
    private SearchProductRecyclerViewAdapter searchAdapter;
    private List<Product> searchResults = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.search_results)
    RecyclerView recyclerViewSearch;

    List<Product> filteredList = new ArrayList<>();

    public SearchProductResultFragment() {
    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        ButterKnife.bind(this, view);

        searchAdapter = new SearchProductRecyclerViewAdapter();
        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerViewSearch.setLayoutManager(linearLayoutManager);

        searchAdapter.setListener(this);
        recyclerViewSearch.setAdapter(searchAdapter);

        //TODO TEST DATA
/*
        searchResults.clear();
        searchResults.add(new Product("Mlijeko", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("Kupus", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("Ajvar", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("Sve", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("Kruh", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("AAAAAAAA SVE", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("Mlijeko", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("Kupus", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("Ajvar", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("Sve", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("Kruh", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchResults.add(new Product("AAAAAAAA SVE", 0, "https://en.opensuse.org/images/4/49/Amarok-logo-small.png", "vrlo fino", 5.45));
        searchAdapter.setData(searchResults);*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.setView(this);
        presenter.renderView();
    }

    @Override
    public void showData(final List<Product> products) {
        searchResults.clear();
        searchResults.addAll(products);
        searchAdapter.setData(products);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                               + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void searchProducts(final String filter) {
        filteredList.clear();

        if (searchResults != null) {
            for (final Product searchResult : searchResults) {
                if (searchResult.getName().toLowerCase().contains(filter.toLowerCase())) {
                    filteredList.add(searchResult);
                }
            }
            searchAdapter.setData(filteredList);
        }
        if (filter.isEmpty()) {
            searchAdapter.setData(searchResults);
        }
    }

    public interface OnFragmentInteractionListener {

        void onProductSelected(Product product);
    }

    @Override
    public void getProduct(final Product product) {
        if (listener != null) {
            listener.onProductSelected(product);
        }
    }
}
